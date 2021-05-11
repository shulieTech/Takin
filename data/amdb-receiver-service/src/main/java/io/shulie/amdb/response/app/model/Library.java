package io.shulie.amdb.response.app.model;

import lombok.Data;

@Data
public class Library {
    String libraryName;

    public Library(String middleWareInfo) {
        this.libraryName = middleWareInfo;
    }
}
