package io.shulie.amdb.adaptors.connector;

import java.util.List;

/**
 * @author vincent
 */
public class DataContext<T> {

    private String path;

    private List<String> childPaths;

    private T model;

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getChildPaths() {
        return childPaths;
    }

    public void setChildPaths(List<String> childPaths) {
        this.childPaths = childPaths;
    }

    @Override
    public String toString() {
        return "DataContext{" +
                "path='" + path + '\'' +
                ", model=" + model +
                '}';
    }
}
