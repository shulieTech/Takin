package io.shulie.surge.data.runtime.common;

import com.google.inject.Module;

/**
 * 提供给 {@link Module} 使用，用于获取当前 {@link DataBootstrap}
 * 辅助安装过程
 *
 * @author pamirs
 */
public interface DataBootstrapAware {
    void setDataBootstrap(DataBootstrap bootstrap);
}
