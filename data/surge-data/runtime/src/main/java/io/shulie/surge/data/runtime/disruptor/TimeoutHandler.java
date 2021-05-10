package io.shulie.surge.data.runtime.disruptor;

public interface TimeoutHandler
{
	void onTimeout(long sequence) throws Exception;
}
