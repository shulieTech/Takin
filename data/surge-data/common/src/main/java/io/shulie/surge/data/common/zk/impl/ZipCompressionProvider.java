package io.shulie.surge.data.common.zk.impl;

import com.google.common.io.ByteStreams;
import io.shulie.surge.data.common.utils.FormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
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
        if (logger.isTraceEnabled()) {
            logger.trace("compressed data of path {}, original={}, compressed={}",
                    new Object[]{
                            path,
                            FormatUtils.humanReadableByteSize(data.length),
                            FormatUtils.humanReadableByteSize(result.length)
                    });
        }
        return result;
    }

    public static byte[] decompress(String path, byte[] compressedData) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        ZipInputStream zipIn = new ZipInputStream(in);
        zipIn.getNextEntry();
        byte[] result = ByteStreams.toByteArray(zipIn);
        zipIn.closeEntry();
        zipIn.close();
        if (logger.isTraceEnabled()) {
            logger.trace("decompressed data of path {}, original={}, decompressed={}",
                    new Object[]{
                            path,
                            FormatUtils.humanReadableByteSize(compressedData.length),
                            FormatUtils.humanReadableByteSize(result.length)
                    });
        }
        return result;
    }
}
