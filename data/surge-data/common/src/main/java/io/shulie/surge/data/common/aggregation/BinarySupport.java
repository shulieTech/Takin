package io.shulie.surge.data.common.aggregation;

import java.io.IOException;

/**
 * 接口支持输出为简单的字节数组，或者从完整的字节数组中还原数据。 这个接口对于每个对象都是用单独的字节数组来存储的场景更合适。
 * 如果需要将多个对象流式地输出或读入，建议使用 {@link Writable} 接口。
 * 
 * @author pamirs
 */
public interface BinarySupport {
	/**
	 * 输出为 bytes
	 * 
	 * @return
	 * @throws IOException
	 */
	byte[] toBytes() throws IOException;

	/**
	 * 从 bytes 读入数据
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	void fromBytes(byte[] bytes) throws IOException;
}
