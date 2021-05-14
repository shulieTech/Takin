package io.shulie.tro.channel.router.zk;

import java.util.List;

import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import io.shulie.tro.channel.router.zk.bean.CreateMode;


/**
 * @Description ZooKeeper 客户端实现封装
 * @Author guohz
 * @mail guohaozhu@shulie.io
 * @Date 2020/12/29 20:11
 */
public interface ZkClient extends Stoppable {

	/**
	 * 创建目录节点
	 * @param path
	 * @param createMode
	 * @throws Exception
	 */
	void createDirectory(String path, CreateMode createMode) throws Exception;

	/**
	 * 如果目录节点不存在，创建之
	 * @param path
	 * @throws Exception
	 */
	void ensureDirectoryExists(String path) throws Exception;

	/**
	 * 如果节点的父节点不存在，创建之
	 * @param path
	 * @throws Exception
	 */
	void ensureParentExists(String path) throws Exception;

	/**
	 * 创建节点
	 * @param path
	 * @param data
	 * @param createMode
	 * @throws Exception
	 * @return 写入的节点数据大小
	 */
	int createNode(String path, byte[] data, CreateMode createMode) throws Exception;

	/**
	 * 更新已存在的节点的数据
	 * @param path
	 * @param data
	 * @throws Exception
	 * @return 写入的节点数据大小
	 */
	int updateData(String path, byte[] data) throws Exception;

	/**
	 * 把数据保存到节点中，如果节点不存在，则创建之
	 * @param path
	 * @param data
	 * @param createMode
	 * @throws Exception
	 * @return 写入的节点数据大小
	 */
	int saveData(String path, byte[] data, CreateMode createMode) throws Exception;

	/**
	 * 创建节点，数据使用压缩
	 * @param path
	 * @param data
	 * @param createMode
	 * @throws Exception
	 * @return 写入的节点数据大小
	 */
	int createCompressedNode(String path, byte[] data, CreateMode createMode) throws Exception;

	/**
	 * 更新已存在的节点的数据，数据使用压缩
	 * @param path
	 * @param data
	 * @throws Exception
	 * @return 写入的节点数据大小
	 */
	int updateCompressedData(String path, byte[] data) throws Exception;

	/**
	 * 把数据保存到节点中，如果节点不存在，则创建之，数据使用压缩
	 * @param path
	 * @param data
	 * @param createMode
	 * @throws Exception
	 * @return 写入的节点数据大小
	 */
	int saveCompressedData(String path, byte[] data, CreateMode createMode) throws Exception;

	/**
	 * 检查节点是否存在
	 * @param path
	 * @return
	 * @throws Exception
	 */
	boolean exists(String path) throws Exception;

	/**
	 * 获取节点信息
	 * @param path
	 * @return
	 * @throws Exception
	 */
	ZkNodeStat getStat(String path) throws Exception;

	/**
	 * 获取节点的数据
	 * @param path
	 * @return
	 * @throws Exception
	 */
	byte[] getData(String path) throws Exception;

	/**
	 * 获取节点的数据，同时对数据进行解压缩
	 * @param path
	 * @return
	 * @throws Exception
	 */
	byte[] getDecompressedData(String path) throws Exception;

	/**
	 * 获取节点的数据，如果获取失败返回 <code>null</code>，不抛出异常
	 * @param path
	 * @return
	 */
	byte[] getDataQuietly(String path);

	/**
	 * 获取节点的数据，同时对数据进行解压缩，如果获取失败或无此节点，返回 <code>null</code>，不抛出异常
	 * @param path
	 * @return
	 */
	byte[] getDecompressedDataQuietly(String path);

	/**
	 * 返回节点下一级的子节点
	 * @param path
	 * @return
	 * @throws Exception
	 */
	List<String> listChildren(String path) throws Exception;

	/**
	 * 返回节点下一级的子节点，如果获取失败或无此节点，返回 <code>null</code>，不抛出异常
	 * @param path
	 * @return
	 */
	List<String> listChildrenQuietly(String path);

	/**
	 * 删除节点，recursive 表示是否递归删除下一级子节点
	 * @param path
	 * @param recursive
	 * @throws Exception
	 */
	void delete(String path, boolean recursive) throws Exception;

	/**
	 * 删除节点，删除成功返回 <code>true</code>，否则返回 <code>false</code>，不抛出异常
	 * @param path
	 * @return
	 */
	boolean deleteQuietly(String path);

	/**
	 * 删除节点，删除成功返回 <code>true</code>，否则返回 <code>false</code>，不抛出异常
	 * @param path
	 * @param recursive
	 * @return
	 */
	boolean deleteQuietly(String path, boolean recursive);

	/**
	 * 删除节点下面的所有子节点，删除成功返回 <code>true</code>，
	 * 否则返回 <code>false</code>，不抛出异常
	 * @param path
	 * @return
	 */
	boolean cleanDirectoryQuietly(String path);

	/**
	 * 获取当前的 Zk 服务器列表
	 * @return
	 */
	String getZkServers();

	/**
	 * 创建节点的本地缓存 ZkNodeCache
	 * @param path
	 * @param isDataCompressed
	 * @return
	 */
	ZkNodeCache createZkNodeCache(String path, boolean isDataCompressed);

	/**
	 * 创建监控子节点的增减变化的本地缓存 ZkPathChildrenCache
	 * @param path zk路径
	 * @param listener 子节点回调监听器
	 * @return
	 */
	ZkPathChildrenCache createPathChildrenCache(String path,ZkChildListener listener);

	/**
	 * 创建心跳节点
	 * @param path
	 * @return
	 */
	ZkHeartbeatNode createHeartbeatNode(String path);

	/**
	 * 创建一个分布式锁
	 * @param path
	 * @return
	 */
	InterProcessMutex createLock(String path);
}
