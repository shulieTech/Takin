package io.shulie.tro.channel.router.zk.impl;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 封装了 Netflix Curator 的实现
 * @author pamirs
 */
public class ZipCompressionProvider {

    private static final Logger logger = LoggerFactory.getLogger(ZipCompressionProvider.class);

    public static byte[] compress(String path, byte[] data) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(out));
        zipOut.putNextEntry(new ZipEntry("content"));
        zipOut.write(data);
        zipOut.closeEntry();
        zipOut.close();
        byte[] result = out.toByteArray();

        return result;
    }

    public static byte[] decompress(String path, byte[] compressedData) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        ZipInputStream zipIn = new ZipInputStream(in);
        zipIn.getNextEntry();
        byte[] result = ByteStreams.toByteArray(zipIn);
        zipIn.closeEntry();
        zipIn.close();

        return result;
    }
}
