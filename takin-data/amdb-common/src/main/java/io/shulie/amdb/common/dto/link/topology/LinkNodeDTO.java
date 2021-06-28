package io.shulie.amdb.common.dto.link.topology;

import lombok.Data;

@Data
public class LinkNodeDTO {
    String nodeId;
    String nodeName;
    boolean root;
    String nodeType;
    String nodeTypeGroup;
    Object extendInfo;
}
