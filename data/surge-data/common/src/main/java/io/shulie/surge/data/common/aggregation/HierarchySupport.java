package io.shulie.surge.data.common.aggregation;

import java.util.List;

/**
 * 支持记录嵌套
 * @author pamirs
 */
public interface HierarchySupport<T extends HierarchySupport<T>> {
	/**
	 * 设置父记录
	 * @param parent
	 * @return this
	 */
	T parent(T parent);

	/**
	 * 添加子记录
	 * @param child
	 * @return this
	 */
	T addChild(T child);

	/**
	 * 设置子记录
	 * @param children
	 * @return this
	 */
	T children(List<T> children);

	/**
	 * 获取父记录
	 * @return
	 */
	T getParent();

	/**
	 * 获取子记录
	 * @return
	 */
	List<T> getChildren();
}
