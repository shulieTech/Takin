package io.shulie.amdb.common.dto.link.detail;

import lombok.Data;

import java.util.List;

@Data
public class LinkDetailDTO {
    String appName;
    List<LinkInstanceDTO> instances;
    List<ProviderServiceDTO> providerServices;
    List<CallServiceDTO> callServices;
    List<DownStreamDTO> downStreams;
}