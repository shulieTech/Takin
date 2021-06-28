package io.shulie.amdb.common.dto.link.topology;

import io.shulie.amdb.common.dto.link.detail.LinkDetailDTO;
import lombok.Data;

import java.util.List;

@Data
public class LinkTopologyDTO {
    List<LinkNodeDTO> nodes;
    List<LinkEdgeDTO> edges;
//    List<LinkDetailDTO> detailDTO;
    String latestUpdateTime;
}
