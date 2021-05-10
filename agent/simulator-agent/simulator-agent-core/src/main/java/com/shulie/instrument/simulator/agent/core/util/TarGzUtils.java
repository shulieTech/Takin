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
package com.shulie.instrument.simulator.agent.core.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 10:10 下午
 */
public class TarGzUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(TarGzUtils.class);

    public static void writeFile(InputStream in, String dirPath, String fileName, int fileLength, Logger logger) throws IOException {
        File file = new File(dirPath, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            int percent = -1;
            while ((size = in.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                int newPercent = (int) Math.round((len*1.0 / fileLength) * 100);
                if (percent < newPercent) {
                    logger.info("Downloading file {} -------> {}%\n", fileName, newPercent);
                    percent = newPercent;
                }
            }
        } finally {
            in.close();
            out.close();
        }

        deCompressGZipFile(file.getAbsolutePath(), file.getParent());
        file.delete();
    }

    /**
     * 解压Zip文件
     *
     * @param destDir 文件目录
     */
    public static void deCompressGZipFile(String tarGzFile, String destDir) {
        // 建立输出流，用于将从压缩文件中读出的文件流写入到磁盘
        TarArchiveEntry entry = null;
        TarArchiveEntry[] subEntries = null;
        File subEntryFile = null;
        FileInputStream fis = null;
        GZIPInputStream gis = null;
        TarArchiveInputStream taris = null;
        try {
            fis = new FileInputStream(tarGzFile);
            gis = new GZIPInputStream(fis);
            taris = new TarArchiveInputStream(gis);
            while ((entry = taris.getNextTarEntry()) != null) {
                StringBuilder entryFileName = new StringBuilder();
                entryFileName.append(destDir).append(File.separator).append(entry.getName());
                File entryFile = new File(entryFileName.toString());
                if (entry.isDirectory()) {
                    if (!entryFile.exists()) {
                        entryFile.mkdir();
                    }
                    subEntries = entry.getDirectoryEntries();
                    for (int i = 0; i < subEntries.length; i++) {
                        OutputStream out = null;
                        try {
                            out = new FileOutputStream(subEntryFile);
                            subEntryFile = new File(entryFileName + File.separator + subEntries[i].getName());
                            IOUtils.copy(taris, out);
                        } catch (Exception e) {
                            LOGGER.error("deCompressing file failed: {} in {}", subEntries[i].getName(), tarGzFile);
                            throw new RuntimeException("deCompressing file failed:" + subEntries[i].getName() + "in" + tarGzFile);
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }
                } else {
                    checkFileExists(entryFile);
                    OutputStream out = new FileOutputStream(entryFile);
                    IOUtils.copy(taris, out);
                    out.close();
                    //如果是gz文件进行递归解压
                    if (entryFile.getName().endsWith(".gz")) {
                        deCompressGZipFile(entryFile.getPath(), destDir);
                    }
                }
            }
            //如果需要刪除之前解压的gz文件，在这里进行

        } catch (Exception e) {
            LOGGER.warn("decompress failed: {}", tarGzFile, e);
            throw new RuntimeException("decompress failed: " + tarGzFile);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            if (gis != null) {
                try {
                    gis.close();
                } catch (IOException e) {
                }
            }

            if (taris != null) {
                try {
                    taris.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void checkFileExists(File file) {
        //判断是否是目录
        if (file.isDirectory()) {
            if (!file.exists()) {
                file.mkdir();
            }
        } else {
            //判断父目录是否存在，如果不存在，则创建
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.warn("create new file err: {}", file.getAbsolutePath(), e);
            }
        }
    }
}
