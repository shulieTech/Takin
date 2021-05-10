package io.shulie.surge.data.runtime.disruptor.dsl;

import io.shulie.surge.data.runtime.disruptor.Sequence;
import io.shulie.surge.data.runtime.disruptor.SequenceBarrier;
import io.shulie.surge.data.runtime.disruptor.WorkerPool;

import java.util.concurrent.Executor;

class WorkerPoolInfo<T> implements ConsumerInfo
{
	private final WorkerPool<T> workerPool;
	private final SequenceBarrier sequenceBarrier;
	private boolean endOfChain = true;

	public WorkerPoolInfo(final WorkerPool<T> workerPool, final SequenceBarrier sequenceBarrier)
	{
		this.workerPool = workerPool;
		this.sequenceBarrier = sequenceBarrier;
	}

	@Override
	public Sequence[] getSequences()
	{
		return workerPool.getWorkerSequences();
	}

	@Override
	public SequenceBarrier getBarrier()
	{
		return sequenceBarrier;
	}

	@Override
	public boolean isEndOfChain()
	{
		return endOfChain;
	}

	@Override
	public void start(final Executor executor)
	{
		workerPool.start(executor);
	}

	@Override
	public void halt()
	{
		workerPool.halt();
	}

	@Override
	public void markAsUsedInBarrier()
	{
		endOfChain = false;
	}

	@Override
	public boolean isRunning()
	{
		return workerPool.isRunning();
	}
}
