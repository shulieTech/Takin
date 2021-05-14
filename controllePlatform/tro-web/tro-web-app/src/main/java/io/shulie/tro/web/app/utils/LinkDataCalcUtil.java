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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import com.pamirs.tro.entity.domain.risk.LinkCount;
import io.shulie.tro.web.data.result.risk.LinkDataResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * @ClassName LinkDataCalcUtil
 * @Description
 * @Author qianshui
 * @Date 2020/7/31 上午11:43
 */
@Component
public class LinkDataCalcUtil {

    private final static Integer KEEP_SCALE = 8;

    private static final BigDecimal ZERO = new BigDecimal("0");

    public static void main(String[] args) {
        LinkDataResult data1 = new LinkDataResult();
        data1.setEvent("/1");
        data1.setTps(1000D);
        data1.setRt(100D);
        data1.setErrorCount(3);

        LinkDataResult data2 = new LinkDataResult();
        data2.setEvent("/2");
        data2.setTps(1200D);
        data2.setRt(150D);
        data2.setErrorCount(7);

        LinkDataResult data3 = new LinkDataResult();
        data3.setEvent("/3");
        data3.setTps(1600D);
        data3.setRt(200D);
        data3.setErrorCount(18);

        LinkDataResult data31 = new LinkDataResult();
        data31.setEvent("/3/1");
        data31.setTps(1000D);
        data31.setRt(100D);
        data31.setErrorCount(12);

        LinkDataResult data32 = new LinkDataResult();
        data32.setEvent("/3/2");
        data32.setTps(600D);
        data32.setRt(50D);
        data32.setErrorCount(14);

        data3.setSubLink(Arrays.asList(data31, data32));

        LinkDataCalcUtil calcUtil = new LinkDataCalcUtil();
        LinkCount linkCount = new LinkCount();
        calcUtil.calcLinkWeight(linkCount, null, Arrays.asList(data1, data2, data3));
        System.out.println("------- num=" + linkCount.getCount());
    }

    /**
     * 返回瓶颈接口列表
     *
     * @param interfaceList
     * @param count
     * @param avg
     * @param bottleneckList
     */
    public void getBottleneckInterfaces(List<LinkDataResult> interfaceList, Integer count, BigDecimal avg,
        List<LinkDataResult> bottleneckList) {
        if (CollectionUtils.isEmpty(interfaceList)) {
            return;
        }
        for (LinkDataResult data : interfaceList) {
            data.setNodeCount(count);
            if (data.getRealWeight().compareTo(avg) >= 0) {
                bottleneckList.add(data);
            }
            getBottleneckInterfaces(data.getSubLink(), count, avg, bottleneckList);
        }
    }

    /**
     * @param linkCount 接口数量
     * @param parent    父节点
     * @param sonList   子节点列表
     */
    public void calcLinkWeight(LinkCount linkCount, LinkDataResult parent, List<LinkDataResult> sonList) {
        if (CollectionUtils.isEmpty(sonList)) {
            return;
        }
        linkCount.setCount(linkCount.getCount() + sonList.size());
        if (parent == null) {
            calcSonList(sonList);
        } else {
            calcParentAndSonList(parent, sonList);
        }
        //递归 继续计算
        for (LinkDataResult son : sonList) {
            calcLinkWeight(linkCount, son, son.getSubLink());
        }
    }

    private void calcSonList(List<LinkDataResult> sonList) {
        BigDecimal totalTps = sonList.stream().map(data -> new BigDecimal(data.getTps())).reduce(BigDecimal::add).get();
        BigDecimal totalRt = sonList.stream().map(data -> new BigDecimal(data.getRt())).reduce(BigDecimal::add).get();
        BigDecimal totalErrors = sonList.stream().map(data -> new BigDecimal(data.getErrorCount())).reduce(
            BigDecimal::add).get();
        for (LinkDataResult data : sonList) {
            //计算权重
            data.setCalcWeight(avg(
                rate(new BigDecimal(data.getTps()), totalTps),
                rate(new BigDecimal(data.getRt()), totalRt),
                rate(new BigDecimal(data.getErrorCount()), totalErrors),
                totalErrors));
            //实际权重
            data.setRealWeight(data.getCalcWeight());
        }
    }

    private void calcParentAndSonList(LinkDataResult parent, List<LinkDataResult> sonList) {
        BigDecimal totalTps = sonList.stream().map(data -> new BigDecimal(data.getTps())).reduce(BigDecimal::add).get();
        totalTps = totalTps.add(new BigDecimal(parent.getTps()));
        BigDecimal totalRt = new BigDecimal(parent.getRt());
        BigDecimal totalErrors = sonList.stream().map(data -> new BigDecimal(data.getErrorCount())).reduce(
            BigDecimal::add).get();
        totalErrors = totalErrors.add(new BigDecimal(parent.getErrorCount()));
        for (LinkDataResult data : sonList) {
            //计算权重
            data.setCalcWeight(avg(
                rate(new BigDecimal(data.getTps()), totalTps),
                rate(new BigDecimal(data.getRt()), totalRt),
                rate(new BigDecimal(data.getErrorCount()), totalErrors),
                totalErrors));
            //分parent权重
            data.setCalcWeight(weight(data.getCalcWeight(), parent.getCalcWeight()));
            //实际权重
            data.setRealWeight(data.getCalcWeight());
        }
        BigDecimal totalSonRt = sonList.stream().map(data -> new BigDecimal(data.getRt())).reduce(BigDecimal::add)
            .get();
        parent.setRealWeight(avg(rate(new BigDecimal(parent.getTps()), totalTps),
            rate(new BigDecimal(parent.getRt()).subtract(totalSonRt), totalRt),
            rate(new BigDecimal(parent.getErrorCount()), totalErrors),
            totalErrors));
        //更新parent节点实际比重
        parent.setRealWeight(weight(parent.getRealWeight(), parent.getCalcWeight()));
    }

    /**
     * 计算占比
     *
     * @param num1
     * @param num2
     * @return
     */
    private BigDecimal rate(BigDecimal num1, BigDecimal num2) {
        if (num1.compareTo(ZERO) <= 0 || num2.compareTo(ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return num1.divide(num2, KEEP_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 计算（tps， rt，错误数）平均占比
     *
     * @param num1
     * @param num2
     * @return
     */
    private BigDecimal avg(BigDecimal num1, BigDecimal num2, BigDecimal num3, BigDecimal totalErrors) {
        if (totalErrors.compareTo(ZERO) >= 1) {
            return num1.add(num2).add(num3).divide(new BigDecimal("3"), KEEP_SCALE, RoundingMode.HALF_UP);
        }
        return num1.add(num2).divide(new BigDecimal("2"), KEEP_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 权重再分裂
     *
     * @param num1
     * @param num2
     * @return
     */
    private BigDecimal weight(BigDecimal num1, BigDecimal num2) {
        return num1.multiply(num2).setScale(KEEP_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 接口平均占比
     *
     * @param count
     * @return
     */
    public BigDecimal calcAvgRate(Integer count) {
        return new BigDecimal("1").divide(new BigDecimal(count), KEEP_SCALE, RoundingMode.HALF_UP);
    }
}
