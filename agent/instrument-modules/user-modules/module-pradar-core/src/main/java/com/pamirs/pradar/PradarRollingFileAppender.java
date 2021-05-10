/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.*;
import java.nio.channels.FileLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 支持多进程文件滚动的实现。注意：这个类的实现没有做并发保护，
 * 使用时必须保证单线程操作。一般搭配 {@link SyncAppender} 或者 {@link AsyncAppender}
 * 使用。一般配合 {@link PradarLogDaemon} 使用。
 */
class PradarRollingFileAppender extends PradarAppender {

    /**
     * 日志刷新间隔，buffer 时间超过间隔，则把缓存的日志数据刷新出去
     */
    private static final long LOG_FLUSH_INTERVAL = TimeUnit.SECONDS.toMillis(1);

    /**
     * 默认输出缓冲大小，write(2) 在  O_APPEND 模式下，写入内容小于一个 page(一般是 4K)，
     * 可以保证多进程并发写同一个文件不会写乱。
     * <p>
     * see UNIX环境高级编程(第2版) 第3章3.11节
     */
    private static final int DEFAULT_BUFFER_SIZE = 4 * 1024; // 4KB

    /**
     * 最大备份数
     */
    private int maxBackupIndex = 3;

    /**
     * 单个 Pradar 日志文件的大小
     */
    private final long maxFileSize;

    /**
     * 日志缓存大小
     */
    private final int bufferSize = DEFAULT_BUFFER_SIZE;

    private final String filePath;

    private final AtomicBoolean isRolling = new AtomicBoolean(false);

    private BufferedOutputStream bos = null;

    private long nextFlushTime = 0L;

    /**
     * 累计输出到文件的字节大小，在多进程时不能保证安全，需要定时更新
     */
    private long outputByteSize = 0L;

    // Pradar selfLog 自己打印日志时，不能递归输出，避免重入
    private final boolean selfLogEnabled;

    /**
     * 标记发现了多进程同时写日志。在这种情况，需要避免一次写的日志超过
     * 4KB，否则按这种实现会出现日志内容交织的问题
     */
    private boolean multiProcessDetected = false;

    private long lastFileSuffix = 0;

    /**
     * 要被清理的日志文件后缀
     */
    private static final String DELETE_FILE_SUBFIX = ".deleted";

    public PradarRollingFileAppender(String filePath, long maxFileSize) {
        this(filePath, maxFileSize, true);
    }

    public PradarRollingFileAppender(String filePath, long maxFileSize, boolean selfLogEnabled) {
        initCurrentSize(filePath);
        this.filePath = filePath;
        this.maxFileSize = maxFileSize;
        this.selfLogEnabled = selfLogEnabled;
        setFile();
        addShutdownHook();
    }

    public void shutdown() {
        close();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });
    }

    private File getSmalllestFile() {
        final String fileName = new File(filePath).getName();
        File file = new File(filePath + '.' + lastFileSuffix).getParentFile();
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String suffix) {
                if (suffix.indexOf(".") == -1) {
                    return false;
                }
                String lastSize = suffix.substring(suffix.lastIndexOf('.') + 1);
                return suffix.startsWith(fileName) && NumberUtils.isDigits(lastSize);
            }
        });
        if (ArrayUtils.isEmpty(files)) {
            return null;
        }
        if (files.length <= maxBackupIndex) {
            return null;
        }
        long maxSize = Long.MAX_VALUE;
        File leastFile = null;
        for (File f : files) {
            if (!f.isFile()) {
                continue;
            }
            String fName = f.getName();
            if (fName.indexOf('.') == -1) {
                continue;
            }
            String suffix = fName.substring(fName.lastIndexOf('.') + 1);
            if (!NumberUtils.isDigits(StringUtils.trim(suffix))) {
                continue;
            }
            Long size = Long.valueOf(StringUtils.trim(suffix));
            if (size < maxSize) {
                maxSize = size;
                leastFile = f;
            }
        }
        if (maxSize == Long.MAX_VALUE || leastFile == null) {
            return null;
        }
        return leastFile;
    }

    private void initCurrentSize(String filePath) {
        File file = new File(filePath).getParentFile();
        final String fileName = new File(filePath).getName();
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String suffix) {
                if (suffix.indexOf(".") == -1) {
                    return false;
                }
                String lastSize = suffix.substring(suffix.lastIndexOf('.') + 1);
                return suffix.startsWith(fileName) && NumberUtils.isDigits(lastSize);
            }
        });
        if (ArrayUtils.isEmpty(files)) {
            lastFileSuffix = 0;
            return;
        }
        long maxSize = 0;
        for (File f : files) {
            if (!f.isFile()) {
                continue;
            }
            String fName = f.getName();
            String suffix = fName.substring(fName.lastIndexOf('.') + 1);
            Long size = Long.valueOf(StringUtils.trim(suffix));
            if (size > maxSize) {
                maxSize = size;
            }
        }
        this.lastFileSuffix = maxSize;
    }

    private void setFile() {
        try {
            File logFile = new File(filePath + '.' + lastFileSuffix);
            if (!logFile.exists()) {
                File parentFile = logFile.getParentFile();
                if (!parentFile.exists() && !parentFile.mkdirs()) {
                    doSelfLog("[ERROR] Fail to mkdirs: " + parentFile.getAbsolutePath());
                    return;
                }
                try {
                    if (!logFile.createNewFile()) {
                        doSelfLog("[ERROR] Fail to create file, it exists: " + logFile.getAbsolutePath());
                    }
                } catch (IOException e) {
                    doSelfLog("[ERROR] Fail to create file: " + logFile.getAbsolutePath() +
                            ", error=" + e.getMessage());
                }
            }
            if (!logFile.isFile() || !logFile.canWrite()) {
                doSelfLog("[ERROR] Invalid file, exists=" + logFile.exists() +
                        ", isFile=" + logFile.isFile() +
                        ", canWrite=" + logFile.canWrite() +
                        ", path=" + logFile.getAbsolutePath());
                return;
            }
            FileOutputStream ostream = new FileOutputStream(logFile, true); // 必须 true 保证 O_APPEND
            this.bos = new BufferedOutputStream(ostream, bufferSize);
            this.outputByteSize = logFile.length();
        } catch (Throwable e) {
            doSelfLog("[ERROR] Fail to create file to write: " + filePath + ", error=" + e.getMessage());
        }
    }

    @Override
    public void append(String log) {
        BufferedOutputStream bos = this.bos;
        if (bos != null) {
            try {
                waitUntilRollFinish();

                byte[] bytes = log.getBytes(Pradar.DEFAULT_CHARSET);
                int len = bytes.length;
                if (len > DEFAULT_BUFFER_SIZE && this.multiProcessDetected) {
                    len = DEFAULT_BUFFER_SIZE;
                    bytes[len - 1] = '\n';
                }
                bos.write(bytes, 0, len);
                outputByteSize += len;

                if (outputByteSize >= maxFileSize) {
                    rollOver();
                } else {
                    // 超过指定刷新时间没刷新，就刷新一次
                    if (System.currentTimeMillis() >= nextFlushTime) {
                        flush();
                    }
                }
            } catch (Exception e) {
                doSelfLog("[ERROR] fail to write log to file " + filePath + ", error=" + e.getMessage());
                close();
                setFile();
            }
        }
    }

    @Override
    public void flush() {
        final BufferedOutputStream bos = this.bos;
        if (bos != null) {
            try {
                bos.flush();
                nextFlushTime = System.currentTimeMillis() + LOG_FLUSH_INTERVAL;
            } catch (Throwable e) {
                doSelfLog("[WARN] Fail to flush OutputStream: " + filePath + ", " + e.getMessage());
            }
        }
    }

    @Override
    public void rollOver() {
        final String lockFilePath = filePath + ".lock";

        RandomAccessFile raf = null;
        FileLock fileLock = null;

        if (!isRolling.compareAndSet(false, true)) {
            return;
        }

        try {
            raf = new RandomAccessFile(lockFilePath, "rw");
            fileLock = raf.getChannel().tryLock();

            if (fileLock != null) {
                // 重新更新一下文件大小
                reload();
                if (outputByteSize >= maxFileSize) {
                    File leastFile = null;
                    while ((leastFile = getSmalllestFile()) != null) {
                        File target = new File(leastFile.getAbsolutePath() + DELETE_FILE_SUBFIX);
                        if (!leastFile.renameTo(target) && !leastFile.delete()) {
                            doSelfLog("[ERROR] Fail to delete or rename file: " +
                                    leastFile.getAbsolutePath() + " to " + target.getAbsolutePath());
                        }
                    }
                    close();
                    this.lastFileSuffix += (new File(filePath + "." + lastFileSuffix).length());
                    setFile();
                }
            }
        } catch (IOException e) {
            doSelfLog("[ERROR] Fail rollover file: " + filePath +
                    ", error=" + e.getMessage());
        } finally {
            isRolling.set(false);

            if (fileLock != null) {
                try {
                    fileLock.release();
                } catch (IOException e) {
                    doSelfLog("[ERROR] Fail to release file lock: " + lockFilePath +
                            ", error=" + e.getMessage());
                }
            }

            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    doSelfLog("[WARN] Fail to close file lock: " + lockFilePath +
                            ", error=" + e.getMessage());
                }
            }

            if (fileLock != null) {
                if (!new File(lockFilePath).delete()) {
                    doSelfLog("[WARN] Fail to delete file lock: " + lockFilePath);
                }
            }
        }
    }

    @Override
    public void close() {
        BufferedOutputStream bos = this.bos;
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
                doSelfLog("[WARN] Fail to close OutputStream: " + e.getMessage());
            }
            this.bos = null;
        }
    }

    /**
     * 如果文件变小，或者被删除，重新加载文件
     */
    @Override
    public void reload() {
        flush();
        File logFile = new File(filePath + '.' + lastFileSuffix);
        long fileSize = logFile.length();
        if (this.bos == null || fileSize < outputByteSize) {
            // 可以判断文件已经滚动，或已删除
            doSelfLog("[INFO] Log file rolled over by outside: " + filePath + ", force reload");
            close();
            setFile();
        } else if (fileSize > outputByteSize) {
            // 如果文件大小和预期不相等，但是在增加，一般是多进程并发写的情况，
            // 为了避免多进程反复打开文件，不做 reload。
            // 代价是最坏情况可能给滚动后的文件多写 maxFileSize 字节的数据。
            this.outputByteSize = fileSize;
            if (!this.multiProcessDetected) {
                this.multiProcessDetected = true;
                if (selfLogEnabled) {
                    doSelfLog("[WARN] Multi-process file write detected: " + filePath);
                }
            }
        } else {
            // 一般是单进程写的情况，符合预期
        }
    }

    /**
     * 自动清理日志，以 .deleted 结尾的文件要清理掉，
     * 单独把删除文件的动作剥离出来，是因为 Linux 删除比较大的文件耗时比较长，
     * 避免影响正常写逻辑
     */
    @Override
    public void cleanup() {
        try {
            File logFile = new File(filePath + '.' + lastFileSuffix);
            File parentDir = logFile.getParentFile();
            if (parentDir != null && parentDir.isDirectory()) {
                final String baseFileName = new File(filePath).getName();
                File[] filesToDelete = parentDir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name != null && name.startsWith(baseFileName)
                                && name.endsWith(DELETE_FILE_SUBFIX)) {
                            return true;
                        }
                        return false;
                    }
                });
                if (filesToDelete != null && filesToDelete.length > 0) {
                    for (File f : filesToDelete) {
                        boolean success = f.delete();
                        if (success) {
                            doSelfLog("[INFO] Deleted log file: " + f.getAbsolutePath());
                        } else if (f.exists()) {
                            doSelfLog("[ERROR] Fail to delete log file: " +
                                    f.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (Throwable e) {
            doSelfLog("[ERROR] Fail to cleanup log file, error=" + e.getMessage());
        }
    }

    void waitUntilRollFinish() {
        while (isRolling.get()) {
            try {
                Thread.sleep(1L);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void doSelfLog(String log) {
        if (selfLogEnabled) {
            //Pradar.selfLog(log);
        } else {
            System.out.println("SIMULATOR:" + log);
        }
    }

    public int getMaxBackupIndex() {
        return maxBackupIndex;
    }

    public void setMaxBackupIndex(int maxBackupIndex) {
        if (maxBackupIndex < 1) {
            throw new IllegalArgumentException("maxBackupIndex < 1: " + maxBackupIndex);
        }
        this.maxBackupIndex = maxBackupIndex;
    }

    @Override
    public String toString() {
        return "PradarRollingFileAppender [filePath=" + filePath + "]";
    }

}
