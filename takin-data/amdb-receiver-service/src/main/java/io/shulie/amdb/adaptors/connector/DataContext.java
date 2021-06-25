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
