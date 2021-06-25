/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
