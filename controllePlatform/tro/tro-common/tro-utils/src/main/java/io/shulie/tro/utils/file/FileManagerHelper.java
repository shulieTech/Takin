package io.shulie.tro.utils.file;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.shulie.tro.utils.linux.LinuxHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

/**
 * 文件操作工具类
 *
 * @author zhaoyong
 */
@Slf4j
public final class FileManagerHelper {

    private FileManagerHelper() { /* no instance */ }

    private static final int DEFAULT_READ_BUFFER_SIZE = 4096;

    /**
     * 读取文件内容
     * @param file
     * @param encoding
     * @return
     */
    public static String readFileToString(File file,String encoding) throws IOException {
       return FileUtils.readFileToString(file,encoding);
    }

    /**
     * 将对应的文件复制到目标文件目录
     *
     * @param sourcePaths
     * @param targetPath
     * @return
     */
    public static void copyFiles(List<String> sourcePaths, String targetPath) throws IOException {
        File targetFileDir = new File(targetPath);
        //目标文件夹不存在时，创建改文件夹
        if (!targetFileDir.exists()) {
            targetFileDir.mkdirs();
        }
        for (String sourcePath : sourcePaths) {
            String targetFilePath;
            if (sourcePath.contains(File.separator)) {
                String substring = sourcePath.substring(sourcePath.lastIndexOf(File.separator));
                targetFilePath = targetPath + substring;
            } else {
                targetFilePath = targetPath + sourcePath;
            }
            //当目标文件不存在时，复制文件
            File targetFile = new File(targetFilePath);
            if (!targetFile.exists()) {
                Files.copy(new File(sourcePath).toPath(), targetFile.toPath());
            }
        }
    }

    /**
     * 根据路径删除文件
     *
     * @param paths
     * @return
     */
    public static Boolean deleteFiles(List<String> paths) {
        for (String path : paths) {
            if (!deleteFilesByPath(path)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 循环删除该目录下的所有文件
     *
     * @param path
     * @return
     */
    public static Boolean deleteFilesByPath(String path) {
        if (!new File(path).exists()) {
            return Boolean.TRUE;
        }
        return LinuxHelper.executeLinuxCmd("rm -rf " + path);
    }

    /**
     * 将字符串创建为指定文件
     */
    public static Boolean createFileByPathAndString(String filePath,String fileContent){
        String substring = filePath.substring(0, filePath.lastIndexOf("/"));
        File file = new File(substring);
        if (!file.exists()){
            file.mkdirs();
        }
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(filePath));
            bf.write(fileContent);
            bf.flush();
            bf.close();
            return Boolean.TRUE;
        } catch (IOException e) {
            log.error("字符串转换为文件操作失败！",e);
            return Boolean.FALSE;
        }finally {
            try {
                if (bf != null){
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将原文件打包到目标路径
     *
     * @param sourcePaths 源路径
     * @param targetPath  目标文件路径
     * @param zipFileName zip文件名称
     * @param isCovered   是否覆盖，如果目标文件已存在，为true，删除原文件，重新打包；为false，不打包直接返回
     * @throws IOException
     */
    public static void zipFiles(List<String> sourcePaths, String targetPath, String zipFileName, boolean isCovered)
        throws IOException {
        zipFiles(sourcePaths, targetPath, zipFileName, isCovered,"/tmp");
    }

    /**
     * 将原文件打包到目标路径
     *
     * @param sourcePaths 源路径
     * @param targetPath  目标文件路径
     * @param zipFileName zip文件名称
     * @param isCovered   是否覆盖，如果目标文件已存在，为true，删除原文件，重新打包；为false，不打包直接返回
     * @throws IOException
     */
    public static void zipFiles(List<String> sourcePaths, String targetPath, String zipFileName, boolean isCovered,String tmpFilePath)
        throws IOException {
        File zipFilePath = new File(targetPath);
        if (!zipFilePath.exists()) {
            zipFilePath.mkdirs();
        }
        File zipFile = new File(targetPath + zipFileName);
        if (zipFile.exists()) {
            //如果文件已存在
            if (!isCovered) {
                return;
            } else {
                deleteFilesByPath(targetPath + zipFileName);
            }
        } else {
            zipFile.createNewFile();
        }
        //FIXME 替换成 File.createTempFile 这种方式
        UUID uuid = UUID.randomUUID();
        String tmpPath = tmpFilePath+ File.separator + uuid;
        File tmpPathFile = new File(tmpPath);
        if (!tmpPathFile.exists()) {
            tmpPathFile.mkdirs();
        }
        copyFiles(sourcePaths, tmpPath);
        compress(tmpPath, zipFile);
        deleteFilesByPath(tmpPath);
    }

    private static void compress(String srcPathName, File zipFile) throws IOException {
        File file = new File(srcPathName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            log.error("压缩文件失败！srcPathName={} targetPath={} zipFileName={}", srcPathName, zipFile, e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            System.out.println("压缩：" + basedir + file.getName());
            compressDirectory(file, out, basedir);
        } else {
            System.out.println("压缩：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            /* 递归 */
            compress(file, out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte[] data = new byte[DEFAULT_READ_BUFFER_SIZE];
            while ((count = bis.read(data, 0, DEFAULT_READ_BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            log.error("压缩单个文件失败! fileName={}", file.getName(), e);
            //用于递归的阻断
            throw new RuntimeException(e);
        }
    }

}
