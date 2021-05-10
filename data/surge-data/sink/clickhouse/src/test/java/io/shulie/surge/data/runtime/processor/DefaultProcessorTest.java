package io.shulie.surge.data.runtime.processor;


import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

public class DefaultProcessorTest {

    @Test
    public void publish() {
        List<String> list = Lists.newArrayList();
        list.add("a1");
        list.add("a2");
        list.add("a3");
        list.add("a4");
        list.add("a5");
        list.add("a6");
        list.add("a7");
        Iterator<String> iterator = list.iterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        System.out.println(list);
    }
}