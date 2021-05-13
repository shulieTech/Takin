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

package io.shulie.tro.web.app.utils;

import com.alibaba.fastjson.JSON;

import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.protocal.ChannelProtocol;

/**
 * @Author: mubai
 * @Date: 2021-01-04 16:49
 * @Description:
 */
public class JsonChannelProtocol implements ChannelProtocol {

    @Override
    public byte[] serialize(CommandPacket packet) {
        String json = JSON.toJSONString(packet);
        byte[] bytes = json.getBytes();
        return bytes;
    }

    @Override
    public String serializeJson(CommandPacket commandPacket) {
        return JSON.toJSONString(commandPacket);
    }

    @Override
    public CommandPacket deserialize(byte[] data) {
        String jsonStr = new String(data);
        CommandPacket commandPacket = JSON.parseObject(jsonStr, CommandPacket.class);
        return commandPacket;
    }
}
