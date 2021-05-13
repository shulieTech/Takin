package io.shulie.tro.definition.config.zk.bean;

import java.io.Serializable;
import java.util.List;

import io.shulie.tro.definition.config.zk.enums.AllowListType;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
public class AllowList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private AllowListType type;

    /**
     * 如果是DUBBO，对应的类路径，不包含方法名；
     * 如果是HTTP，对应的是uri，这块通过正则表达式进行判断，需要考虑restful类型接口；
     * 如果是Redis，对应的key的正则表达式；
     * 暂时没有类型
     */
    private String interfaceName;

    /**
     * 如果是DUBBO，对应的类方法；
     * 如果是HTTP,对应的是 Http Method；
     * 如果是Redis，暂时不知道对应啥
     */
    private List<String> methodName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllowListType getType() {
        return type;
    }

    public void setType(AllowListType type) {
        this.type = type;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<String> getMethodName() {
        return methodName;
    }

    public void setMethodName(List<String> methodName) {
        this.methodName = methodName;
    }
}
