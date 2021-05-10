package io.shulie.surge.data.runtime.digest.handler;

import io.shulie.surge.data.runtime.digest.DigestContext;
import io.shulie.surge.data.runtime.disruptor.EventFactory;

public class DigestJob {
	public DigestContext context;

	public final static EventFactory<DigestJob> EVENT_FACTORY = new EventFactory<DigestJob>() {
		@Override
		public DigestJob newInstance() {
			return new DigestJob();
		}
	};
}
