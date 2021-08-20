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

package io.shulie.tro.eventcenter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author vincent
 */
@Component
public class EventCenterTemplate {

    @Autowired
    private ListenerContainer listenerContainer;

    public EventCenterTemplate(ListenerContainer listenerContainer) {
        this.listenerContainer = listenerContainer;
    }

    /**
     * @param event
     */
    public void doEvents(Event event) {
        Map<String,ListenerContainer.Listener> map = listenerContainer.getListeners().get(event.getEventName());
        List<ListenerContainer.Listener> list = new ArrayList(map.values());
        Collections.sort(list, new Comparator<ListenerContainer.Listener>() {
            @Override
            public int compare(ListenerContainer.Listener o1, ListenerContainer.Listener o2) {
                return o1.getIntrestFor().order() > o1.getIntrestFor().order() ? 1 : -1;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        for (ListenerContainer.Listener entry : list) {
            try {
                entry.getMethod().invoke(entry.getObject(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得监听容器的数量
     * @param eventName
     * @return
     */
    public int getListenerContainerNum(String eventName) {
        Map<String,ListenerContainer.Listener> total = listenerContainer.getListeners().get(eventName);
        if(total != null){
            return total.size();
        }
        return 0;
    }
}
