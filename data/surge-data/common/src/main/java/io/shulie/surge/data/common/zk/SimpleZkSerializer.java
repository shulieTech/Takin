package io.shulie.surge.data.common.zk;

import com.google.common.base.Charsets;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class SimpleZkSerializer implements ZkSerializer {
    /**
     * 序列化，将对象转化为字节数组
     */
    @Override
    public byte[] serialize(Object obj) throws ZkMarshallingError {
        return String.valueOf(obj).getBytes(Charsets.UTF_8);
    }

    /**
     * 反序列化，将字节数组转化为UTF_8字符串
     */
    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes, Charsets.UTF_8);
    }
}