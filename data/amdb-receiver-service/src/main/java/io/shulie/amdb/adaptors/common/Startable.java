package io.shulie.amdb.adaptors.common;

import java.io.Serializable;

public interface Startable extends Serializable {

    void start() throws Exception;
}
