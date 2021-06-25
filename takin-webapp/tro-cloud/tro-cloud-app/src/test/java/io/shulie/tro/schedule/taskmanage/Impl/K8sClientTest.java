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

package io.shulie.tro.schedule.taskmanage.Impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class K8sClientTest {

    @Test
    public void deleteJobTest() {
        KubernetesClient k8sClient = null;
        try {
            // 由于模块之间，包无法引入进来，直接上绝对路径
            File file = new File(
                "/Users/hezhongqi/workspace/tro-cloud/tro-cloud-app/src/main/resources/config/admin.conf");
            InputStream stream = new FileInputStream(file);
            Config config = Config.fromKubeconfig(IOUtils.toString(stream));
            config.setHttp2Disable(true);
            k8sClient = new DefaultKubernetesClient(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建一个
        String jobName = "scene-task-3-95-9725";
        //Job tempJob = k8sClient.batch().jobs().inNamespace("default").withName(jobName).get();
        //PodList pod = k8sClient.pods().inNamespace("default").list();
        //ConfigMap configMap = k8sClient.configMaps().inNamespace("default").withName("engine-config-2-96.json").get();
        //Map<String, String> data = configMap.getData();
        //Gson gson = new Gson();
        //JsonObject param = gson.fromJson(data.get("engine.conf"), JsonObject.class);
        //param.addProperty("stop",true);
        //data.put("engine.conf",gson.toJson(param));
        //configMap.setData(data);
        //k8sClient.configMaps().inNamespace("default").withName("engine-config-2-96.json").replace(configMap);
        //
        //ConfigMap newConfigMap = k8sClient.configMaps().inNamespace("default").withName("engine-config-2-96.json")
        // .get();

        //k8sClient.pods().inNamespace("default").
        //.withName(jobName + "-4zkqk").get();
        //System.out.println( "pod");
        String json = "{\n"
            + "  \"apiVersion\": \"batch/v1\",\n"
            + "  \"kind\": \"Job\",\n"
            + "  \"metadata\": {\n"
            + "    \"finalizers\": [],\n"
            + "    \"managedFields\": [],\n"
            + "    \"name\": \"scene-task-123-1328\",\n"
            + "    \"ownerReferences\": [],\n"
            + "    \"additionalProperties\": {}\n"
            + "  },\n"
            + "  \"spec\": {\n"
            + "    \"activeDeadlineSeconds\": 180,\n"
            + "    \"parallelism\": 1,\n"
            + "    \"template\": {\n"
            + "      \"spec\": {\n"
            + "        \"containers\": [\n"
            + "          {\n"
            + "            \"args\": [],\n"
            + "            \"command\": [],\n"
            + "            \"env\": [\n"
            + "              {\n"
            + "                \"name\": \"engineType\",\n"
            + "                \"value\": \"jmeter\",\n"
            + "                \"additionalProperties\": {}\n"
            + "              },\n"
            + "              {\n"
            + "                \"name\": \"confPath\",\n"
            + "                \"value\": \"/etc/engine/config/engine.conf\",\n"
            + "                \"additionalProperties\": {}\n"
            + "              }\n"
            + "            ],\n"
            + "            \"envFrom\": [],\n"
            + "            \"image\": \"forcecop/pressure-engine:v4.2.2\",\n"
            + "            \"imagePullPolicy\": \"IfNotPresent\",\n"
            + "            \"lifecycle\": {\n"
            + "              \"postStart\": {\n"
            + "                \"exec\": {\n"
            + "                  \"command\": [\n"
            + "                    \"sh\",\n"
            + "                    \"-c\",\n"
            + "                    \" echo export POD_NUMBER\\u003d$(curl http://10.0.1"
            + ".4:10010/tro-cloud/api/scene/task/taskResultNotify?taskId\\u003d1328\\\\\\u0026sceneId\\u003d123"
            + "\\\\\\u0026status\\u003dstarted ) \\u003e\\u003e /etc/profile\"\n"
            + "                  ],\n"
            + "                  \"additionalProperties\": {}\n"
            + "                },\n"
            + "                \"additionalProperties\": {}\n"
            + "              },\n"
            + "              \"preStop\": {\n"
            + "                \"exec\": {\n"
            + "                  \"command\": [\n"
            + "                    \"sh\",\n"
            + "                    \"-c\",\n"
            + "                    \" echo export POD_NUMBER\\u003d$(curl http://10.0.1"
            + ".4:10010/tro-cloud/api/scene/task/taskResultNotify?taskId\\u003d1328\\\\\\u0026sceneId\\u003d123"
            + "\\\\\\u0026status\\u003dfinished ) \\u003e\\u003e /etc/profile\"\n"
            + "                  ],\n"
            + "                  \"additionalProperties\": {}\n"
            + "                },\n"
            + "                \"additionalProperties\": {}\n"
            + "              },\n"
            + "              \"additionalProperties\": {}\n"
            + "            },\n"
            + "            \"name\": \"pressure-engine\",\n"
            + "            \"ports\": [],\n"
            + "            \"volumeDevices\": [],\n"
            + "            \"volumeMounts\": [\n"
            + "              {\n"
            + "                \"mountPath\": \"/etc/engine/config\",\n"
            + "                \"name\": \"engine-conf\",\n"
            + "                \"additionalProperties\": {}\n"
            + "              },\n"
            + "              {\n"
            + "                \"mountPath\": \"/etc/engine/script/\",\n"
            + "                \"name\": \"script-file\",\n"
            + "                \"additionalProperties\": {}\n"
            + "              }\n"
            + "            ],\n"
            + "            \"additionalProperties\": {}\n"
            + "          }\n"
            + "        ],\n"
            + "        \"ephemeralContainers\": [],\n"
            + "        \"hostAliases\": [],\n"
            + "        \"imagePullSecrets\": [],\n"
            + "        \"initContainers\": [],\n"
            + "        \"readinessGates\": [],\n"
            + "        \"restartPolicy\": \"Never\",\n"
            + "        \"tolerations\": [],\n"
            + "        \"topologySpreadConstraints\": [],\n"
            + "        \"volumes\": [\n"
            + "          {\n"
            + "            \"configMap\": {\n"
            + "              \"items\": [],\n"
            + "              \"name\": \"engine-config-123-1328.json\",\n"
            + "              \"additionalProperties\": {}\n"
            + "            },\n"
            + "            \"name\": \"engine-conf\",\n"
            + "            \"additionalProperties\": {}\n"
            + "          },\n"
            + "          {\n"
            + "            \"name\": \"script-file\",\n"
            + "            \"nfs\": {\n"
            + "              \"path\": \"/mnt/resource/nfs\",\n"
            + "              \"server\": \"10.0.1.4\",\n"
            + "              \"additionalProperties\": {}\n"
            + "            },\n"
            + "            \"additionalProperties\": {}\n"
            + "          }\n"
            + "        ],\n"
            + "        \"additionalProperties\": {}\n"
            + "      },\n"
            + "      \"additionalProperties\": {}\n"
            + "    },\n"
            + "    \"additionalProperties\": {}\n"
            + "  },\n"
            + "  \"additionalProperties\": {}\n"
            + "}\n";

        System.out.println(json);
        Gson gson = new Gson();

        Job job = gson.fromJson(json, Job.class);
        try {
            Job tempJob = k8sClient.batch().jobs().inNamespace("default").create(job);
        } catch (Exception e) {
            System.out.println(((KubernetesClientException)e).getStatus().getMessage());
        }

        //Thread.sleep(1000 * 10);
        //进行删除，循环打印出时间
        //Job tempJob = null;
        //Pod pod  = null;
        //long start = System.currentTimeMillis();
        //do {
        //    tempJob = k8sClient.batch().jobs().inNamespace("default").withName(jobName).get();
        //    pod = k8sClient.pods().inNamespace("default").withName(jobName).get();
        //    System.out.println( "job" + gson.toJson(tempJob));
        //    System.out.println( "pod" + gson.toJson(pod));
        //} while (tempJob != null  && tempJob.getStatus().getActive() != null && tempJob.getStatus().getActive()
        // .intValue() > 0);
        //long end = System.currentTimeMillis();

        //System.out.println("运行时间：" + (end - start));
        k8sClient.batch().jobs().inNamespace("default").withName(jobName).delete();
    }

}
