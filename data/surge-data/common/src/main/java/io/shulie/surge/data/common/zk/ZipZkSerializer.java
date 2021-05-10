package io.shulie.surge.data.common.zk;

import com.google.common.io.ByteStreams;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipZkSerializer implements ZkSerializer {

	private SerializableSerializer serializer = new SerializableSerializer();

	@Override
	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ZipInputStream zipIn = new ZipInputStream(in);
			zipIn.getNextEntry();
			byte[] finalBytes = ByteStreams.toByteArray(zipIn);
			zipIn.closeEntry();
			zipIn.close();
			return serializer.deserialize(finalBytes);
		} catch (IOException e) {
			throw new ZkMarshallingError(e);
		}
	}

	@Override
	public byte[] serialize(Object data) throws ZkMarshallingError {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(out));
			zipOut.putNextEntry(new ZipEntry("content"));
			zipOut.write(serializer.serialize(data));
			zipOut.closeEntry();
			zipOut.close();
			return out.toByteArray();
		} catch (IOException e) {
			throw new ZkMarshallingError(e);
		}
	}
}
