package io.shulie.surge.data.runtime.disruptor.dsl;

import io.shulie.surge.data.runtime.disruptor.Sequence;
import io.shulie.surge.data.runtime.disruptor.SequenceBarrier;

import java.util.concurrent.Executor;

interface ConsumerInfo
{
	Sequence[] getSequences();

	SequenceBarrier getBarrier();

	boolean isEndOfChain();

	void start(Executor executor);

	void halt();

	void markAsUsedInBarrier();

	boolean isRunning();
}
