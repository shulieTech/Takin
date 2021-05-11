package io.shulie.amdb.model;

import io.shulie.amdb.common.ErrorInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class AmdbResult<T> implements Serializable {
    private static final long serialVersionUID = 45387487319877474L;
    private ErrorInfo error;
    private T data;
    private Long total;
    private Boolean success;
    private Boolean notSuccess;
}