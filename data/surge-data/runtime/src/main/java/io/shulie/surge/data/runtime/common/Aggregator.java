/*
package io.shulie.surge.data.runtime.common;

import com.google.inject.Singleton;
import com.pamirs.pradar.metric.Metric;
import com.pamirs.pradar.rt.PradarRtConstant;
import com.pamirs.pradar.rt.model.CallStat;
import com.pamirs.pradar.rt.storm.PradarReduceBolt;
import com.pamirs.pradar.scheduler.Scheduler;
import com.pamirs.pradar.utils.FormatUtils;
import com.pamirs.pradar.utils.Pair;
import org.apache.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.pamirs.pradar.utils.CommonUtils.divide;

*/
/**
 * 聚集器，基于 Storm 流式的聚集逻辑，封装了调度和一致哈希数据到  reducer 的分配策略。
 * @author pamirs
 *//*

@Singleton
public class Aggregator {

	private static final Logger logger = Logger.getLogger(Aggregator.class);

	private final com.pamirs.pradar.rt.aggregate.Aggregation<Metric, CallStat> aggregation = new com.pamirs.pradar.rt.aggregate.Aggregation<Metric, CallStat>(
			PradarRtConstant.AGGREGATE_SECONDS_INTERVAL,
			PradarRtConstant.AGGREGATE_SECONDS_LOWER_LIMIT);

	public void init(Scheduler scheduler,
					  final SpoutOutputCollector collector,
					  final TopologyContext topologyContext) {
		aggregation.start(scheduler, new com.pamirs.pradar.rt.aggregate.Aggregation.CommitAction<Metric, CallStat>() {
			@Override
			@SuppressWarnings("unchecked")
			public void commit(long slotKey, com.pamirs.pradar.rt.aggregate.AggregateSlot<Metric, CallStat> slot) {
				int size = slot.size();
				if (size > 0) {
					Map<Metric, CallStat> map = slot.toMap();

					// 每个 reducer 负责处理一组 Pair<Metric, CallStat> 的汇总任务 Job
					// 直接 emit 出去由 Storm 进行分配，会导致消息丢失率很高，所以打包发送。
					List<Integer> reducerIds = topologyContext.getComponentTasks(
							PradarReduceBolt.class.getSimpleName());
					final int reducerCount = reducerIds.size();
					List<Pair<Metric, CallStat>>[] jobs = new List[reducerCount];
					int jobSize = (int) divide(size, reducerCount - 1); // 估值
					for (int i = 0; i < reducerCount; ++i) {
						jobs[i] = new ArrayList<Pair<Metric, CallStat>>(jobSize);
					}

					// 将汇总任务按照 reducer 的数量哈希分配
					// 哈希策略必须全局一致，使同一个 metric 落在同一个 reducer 上面
					for (Entry<Metric, CallStat> entry : map.entrySet()) {
						int jobId = Math.abs(entry.getKey().hashCode()) % reducerCount;
						List<Pair<Metric, CallStat>> job = jobs[jobId];
						job.add(new Pair<Metric, CallStat>(entry.getKey(), entry.getValue()));
					}

					// 将 Job 直接发送给 reducer
					final String slotKeyTime = FormatUtils.toSecondTimeString(slotKey * 1000);
					for (int i = 0; i < reducerCount; ++i) {
						int reducerId = reducerIds.get(i).intValue();
						List<Pair<Metric, CallStat>> job = jobs[i];
						if (!job.isEmpty()) {
							collector.emitDirect(reducerId, PradarRtConstant.REDUCE_STREAM_ID,
									new Values(slotKey, job));
						}
					}
					logger.info("emit " + slotKeyTime + " to " + reducerIds.size() +
							" reducers, size=" + size);
				}
			}
		});
	}

	public void init(Scheduler scheduler,
			final OutputCollector collector,
			final TopologyContext topologyContext) {
		aggregation.start(scheduler, new com.pamirs.pradar.rt.aggregate.Aggregation.CommitAction<Metric, CallStat>() {
			@Override
			@SuppressWarnings("unchecked")
			public void commit(long slotKey, com.pamirs.pradar.rt.aggregate.AggregateSlot<Metric, CallStat> slot) {
				int size = slot.size();
				if (size > 0) {
					Map<Metric, CallStat> map = slot.toMap();

					// 每个 reducer 负责处理一组 Pair<Metric, CallStat> 的汇总任务 Job
					// 直接 emit 出去由 Storm 进行分配，会导致消息丢失率很高，所以打包发送。
					List<Integer> reducerIds = topologyContext.getComponentTasks(
							PradarReduceBolt.class.getSimpleName());
					final int reducerCount = reducerIds.size();
					List<Pair<Metric, CallStat>>[] jobs = new List[reducerCount];
					int jobSize = (int) divide(size, reducerCount - 1); // 估值
					for (int i = 0; i < reducerCount; ++i) {
						jobs[i] = new ArrayList<Pair<Metric, CallStat>>(jobSize);
					}

					// 将汇总任务按照 reducer 的数量哈希分配
					// 哈希策略必须全局一致，使同一个 metric 落在同一个 reducer 上面
					for (Entry<Metric, CallStat> entry : map.entrySet()) {
						int jobId = Math.abs(entry.getKey().hashCode()) % reducerCount;
						List<Pair<Metric, CallStat>> job = jobs[jobId];
						job.add(new Pair<Metric, CallStat>(entry.getKey(), entry.getValue()));
					}

					// 将 Job 直接发送给 reducer
					final String slotKeyTime = FormatUtils.toSecondTimeString(slotKey * 1000);
					for (int i = 0; i < reducerCount; ++i) {
						int reducerId = reducerIds.get(i).intValue();
						List<Pair<Metric, CallStat>> job = jobs[i];
						if (!job.isEmpty()) {
							collector.emitDirect(reducerId, PradarRtConstant.REDUCE_STREAM_ID,
									new Values(slotKey, job));
						}
					}
					logger.info("emit " + slotKeyTime + " to " + reducerIds.size() +
							" reducers, size=" + size);
				}
			}
		});
	}

	// ---------- 代理 Aggregation 的方法 ----------

	public int size() {
		return aggregation.size();
	}

	public void stop() {
		aggregation.stop();
	}

	public int getInterval() {
		return aggregation.getInterval();
	}

	public int getLowerLimit() {
		return aggregation.getDelay();
	}

	public long getLowerBound() {
		return aggregation.getSlotKeyToCommit();
	}

	public long slotKeyToTimestamp(long key) {
		return aggregation.slotKeyToTimestamp(key);
	}

	public long timestampToSlotKey(long timestamp) {
		return aggregation.timestampToSlotKey(timestamp);
	}

	public com.pamirs.pradar.rt.aggregate.AggregateSlot<Metric, CallStat> getSlotBySlotKey(long key) {
		return aggregation.getSlotBySlotKey(key);
	}

	public com.pamirs.pradar.rt.aggregate.AggregateSlot<Metric, CallStat> getSlotByTimestamp(long timestamp) {
		return aggregation.getSlotByTimestamp(timestamp);
	}
}
*/
