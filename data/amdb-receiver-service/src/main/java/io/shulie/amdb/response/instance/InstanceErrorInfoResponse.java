package io.shulie.amdb.response.instance;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class InstanceErrorInfoResponse implements Serializable {
    String id;
    List<String> agentIds=new ArrayList<>();
    String description;
    String time;
}
