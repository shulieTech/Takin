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

package io.shulie.surge.data.common.aggregation.metrics;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import io.shulie.surge.data.common.utils.FormatUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.shulie.surge.data.common.utils.CommonUtils.divide;
import static io.shulie.surge.data.common.utils.CommonUtils.isNullEmpty;

/**
 * CallStat 相关通用操作
 *
 * @author pamirs
 */
public class CallStats {

    private static final Logger logger = Logger.getLogger(CallStats.class);

    private static final Comparator<CallStat> COMPARE_BY_FIRST_ITEM = new Comparator<CallStat>() {
        @Override
        public int compare(CallStat o1, CallStat o2) {
            return Longs.compare(o1.get(0), o2.get(0));
        }
    };

    private static final Comparator<CallStat> COMPARE_BY_TIMESTAMP = new Comparator<CallStat>() {
        @Override
        public int compare(CallStat o1, CallStat o2) {
            return Longs.compare(o1.getTimestamp(), o2.getTimestamp());
        }
    };

    public static final Comparator<CallStat> getFirstItemComparator() {
        return COMPARE_BY_FIRST_ITEM;
    }

    public static final Comparator<CallStat> getTimestampComparator() {
        return COMPARE_BY_TIMESTAMP;
    }

    public static final List<CallStat> sortedCopy(Iterable<CallStat> stats,
                                                  Comparator<CallStat> comparator, int count) {
        if (count > 0) {
            return Ordering.from(comparator).greatestOf(stats, count);
        } else {
            return Ordering.from(comparator).sortedCopy(stats);
        }
    }

    public static final <T extends CallStatBased> List<T> sortByStats(Iterable<T> csb,
                                                                      Comparator<T> comparator) {
        return Ordering.from(comparator).sortedCopy(csb);
    }

    /**
     * 数据清洗：对 stats 漏掉的时间点补充值，去掉不正确的时间点，
     */
    public static final List<CallStat> cleanup(
            List<CallStat> stats, long endTime, long totalTime, long intervalMillis) {
        if (isNullEmpty(stats)) {
            return Collections.emptyList();
        }

        int size = (int) (totalTime / intervalMillis) + 1;
        List<CallStat> ret = new ArrayList<CallStat>(size);

        long startTime = endTime - totalTime;
        int slen = stats.size(); // 可能包含多余的数据，也可能缺少数据点

        int si = 0;
        int ai = 0;
        // 跳掉前面多余的点
        while (si < slen) {
            CallStat cs = stats.get(si);
            long ts = cs.getTimestamp();
            if (ts >= startTime) {
                break;
            }
            ++si;
        }

        boolean unsorted = false;
        long nextTs = startTime;
        long prevTs = 0;
        CallStat prevStat = null;
        while (si < slen) {
            CallStat cs = stats.get(si);
            long ts = cs.getTimestamp();

            if (ts < nextTs) {
                if (ts < prevTs) {
                    unsorted = true;
                    break;
                } else {
                    // 数据间隔之间多余的点，或同一个时间点的多个版本
                    // 都聚集到第一个 CallStat 上面
                    if (prevStat != null) {
                        prevStat.aggregateFrom(cs);
                    }
                    ++si;
                    continue;
                }
            } else if (ts > endTime) {
                // 多读了后面的点
                break;
            }

            while (nextTs < ts && ai < size) {
                // nextTs < ts < endTime, 中间漏了数据点 nextTs，需要补上
                CallStat blank = new CallStat();
                blank.setTimestamp(nextTs);
                ret.add(blank);
                prevStat = blank;
                nextTs += intervalMillis;
                ++ai;
            }

            if (ts == nextTs && ai < size) {
                prevStat = cs;
                ret.add(cs);
                prevTs = nextTs;
                nextTs += intervalMillis;
                ++ai;
            }
            ++si;
        }

        if (unsorted) {
            logger.warn("the input List<CallStat> is not sorted");
            List<CallStat> sortedCopy = sortedCopy(stats, COMPARE_BY_TIMESTAMP, -1);
            return cleanup(sortedCopy, endTime, totalTime, intervalMillis);
        }

        // 补上最后缺少的数据
        while (ai < size) {
            CallStat blank = new CallStat();
            blank.setTimestamp(nextTs);
            ret.add(blank);
            ++ai;
        }

        return ret;
    }

    /**
     * 重新计算时间间隔，保证时间间隔内的点数小于 maxPlottingCount
     */
    public static final int calPlottingInterval(List<CallStat> stats, int intervalMillis,
                                                int maxPlottingCount) {
        if (stats.size() < maxPlottingCount) {
            return intervalMillis;
        }
        int half = maxPlottingCount / 2;
        int factor = stats.size() / half;
        return intervalMillis * factor;
    }

    /**
     * 数据合并：时间点太多，通过增大时间间隔，把时间点合并起来
     */
    public static final List<CallStat> mergeByNewInterval(
            List<CallStat> stats, long intervalMillis, long newIntervalMillis) {
        int factor = (int) (newIntervalMillis / intervalMillis);
        if (factor < 1) {
            return stats;
        }
        int oldLen = stats.size();
        int newLen = oldLen / factor;

        // remove tail
        oldLen = newLen * factor;

        CallStat newStat = null;
        List<CallStat> newStats = Lists.newArrayListWithCapacity(newLen);
        for (int i = 0; i < oldLen; ++i) {
            CallStat stat = stats.get(i);
            if (i % factor == 0) {
                if (newStat != null) {
                    newStats.add(newStat);
                }
                newStat = new CallStat();
                newStat.setTimestamp(stat.getTimestamp());
            }
            newStat.aggregateFrom(stat);
        }
        newStats.add(newStat);

        return newStats;
    }

    /**
     * 数据清洗：对 stats 漏掉的时间点补充值，去掉不正确的时间点，
     */
    public static final List<CallStat> cleanup(
            List<CallStat> stats, List<Long> timestamps) {
        if (isNullEmpty(stats)) {
            return Collections.emptyList();
        }

        List<CallStat> ret = new ArrayList<CallStat>(timestamps.size());
        int len = Math.min(stats.size(), timestamps.size());
        int i = 0, j = 0;
        while (i < len && j < len) {
            CallStat a = stats.get(i);
            long timestamp = timestamps.get(j);
            if (a.getTimestamp() == timestamp) {
                ret.add(a);
                i += 1;
                j += 1;
            } else if (a.getTimestamp() < timestamp) {
                i += 1;
            } else {
                CallStat callStat = new CallStat();
                callStat.setTimestamp(timestamp);
                ret.add(callStat);
                j += 1;
            }
        }

        while (j < timestamps.size()) {
            CallStat callStat = new CallStat();
            callStat.setTimestamp(timestamps.get(j));
            ret.add(callStat);
            j += 1;
        }
        return ret;
    }

    public static void aggregate(List<CallStat> dst, List<CallStat> src) {
        int len = dst.size();
        for (int i = 0; i < len; ++i) {
            dst.get(i).aggregateFrom(src.get(i));
        }
    }

    public static JSONArray toJSONArray(List<CallStat> stats, int pos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            ret.add(cs.get(pos));
        }
        return ret;
    }

    public static JSONArray toJSONArraySample(List<CallStat> stats, int pos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            if (cs == null) {
                continue;
            }
            final JSONArray jsonArray = new JSONArray();
            jsonArray.add(cs.getTraceId());
            jsonArray.add(cs.get(pos));
            ret.add(jsonArray);
        }
        return ret;
    }

    public static JSONArray divideConstAndToJSONArray(List<CallStat> stats, int dividendPos, long divisor) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            ret.add(FormatUtils.roundx0(divide(cs.get(dividendPos), divisor)));
        }
        return ret;
    }

    public static JSONArray divideAndToJSONArray(List<CallStat> stats, int dividendPos, int divisorPos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            ret.add(FormatUtils.roundx0(divide(cs.get(dividendPos), cs.get(divisorPos))));
        }
        return ret;
    }

    public static JSONArray divideAndToJSONArraySample(List<CallStat> stats, int dividendPos, int divisorPos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            if (cs == null) {
                continue;
            }
            final JSONArray jsonArray = new JSONArray();
            jsonArray.add(cs.getTraceId());
            jsonArray.add(FormatUtils.roundx0(divide(cs.get(dividendPos), cs.get(divisorPos))));
            ret.add(jsonArray);
        }
        return ret;
    }

    public static JSONArray percentAndToJSONArray(List<CallStat> stats, int dividendPos, int divisorPos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            ret.add(FormatUtils.roundx0(100 * divide(cs.get(dividendPos), cs.get(divisorPos))));
        }
        return ret;
    }

    public static JSONArray substractAndToJSONArray(List<CallStat> stats, int subtrahendPos, int subtractorPos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            ret.add(Math.max(0, cs.get(subtrahendPos) - cs.get(subtractorPos)));
        }
        return ret;
    }

    public static JSONArray substractAndToJSONArraySample(List<CallStat> stats, int subtrahendPos, int subtractorPos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            final JSONArray jsonArray = new JSONArray();
            jsonArray.add(cs.getTraceId());
            jsonArray.add(Math.max(0, cs.get(subtrahendPos) - cs.get(subtractorPos)));
            ret.add(jsonArray);
        }
        return ret;
    }

    public static JSONArray substractAndDivideConstAndToJSONArray(List<CallStat> stats, int subtrahendPos,
                                                                  int subtractorPos, long divisor) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            final long diff = Math.max(0, cs.get(subtrahendPos) - cs.get(subtractorPos));
            ret.add(FormatUtils.roundx0(divide(diff, divisor)));
        }
        return ret;
    }

    public static JSONArray substractAndDivideConstAndToJSONArraySample(List<CallStat> stats, int subtrahendPos,
                                                                        int subtractorPos, long divisor) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            final JSONArray jsonArray = new JSONArray();
            jsonArray.add(cs.getTraceId());
            final long diff = Math.max(0, cs.get(subtrahendPos) - cs.get(subtractorPos));
            jsonArray.add(FormatUtils.roundx0(divide(diff, divisor)));
            ret.add(jsonArray);
        }
        return ret;
    }

    public static JSONArray substractAndDivideAndToJSONArray(List<CallStat> stats, int subtrahendPos,
                                                             int subtractorPos, int divisorPos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            final long diff = Math.max(0, cs.get(subtrahendPos) - cs.get(subtractorPos));
            ret.add(FormatUtils.roundx2(divide(diff, cs.get(divisorPos))));
        }
        return ret;
    }

    public static JSONArray substractAndDivideAndToJSONArraySample(List<CallStat> stats, int subtrahendPos,
                                                                   int subtractorPos, int divisorPos) {
        final JSONArray ret = new JSONArray(stats.size());
        for (CallStat cs : stats) {
            final JSONArray jsonArray = new JSONArray();
            jsonArray.add(cs.getTraceId());
            final long diff = Math.max(0, cs.get(subtrahendPos) - cs.get(subtractorPos));
            jsonArray.add(FormatUtils.roundx2(divide(diff, cs.get(divisorPos))));
            ret.add(jsonArray);
        }
        return ret;
    }

    public static long sum(List<CallStat> stats, int pos) {
        long sum = 0;
        for (CallStat cs : stats) {
            sum += cs.get(pos);
        }
        return sum;
    }

    public static long max(List<CallStat> stats, int pos) {
        long max = Long.MIN_VALUE;
        for (CallStat cs : stats) {
            max = Math.max(max, cs.get(pos));
        }
        return max;
    }

    public static long min(List<CallStat> stats, int pos) {
        long min = Long.MAX_VALUE;
        for (CallStat cs : stats) {
            min = Math.min(min, cs.get(pos));
        }
        return min;
    }
}
