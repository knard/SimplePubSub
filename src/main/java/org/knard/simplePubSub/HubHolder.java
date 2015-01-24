package org.knard.simplePubSub;

import java.util.LinkedList;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.knard.SimpleInject.Context;

public class HubHolder {

	private static class QueueElement {
		private String topic;
		private Context ctx;
	}

	private static final LinkedList<QueueElement> queue = new LinkedList<HubHolder.QueueElement>();

	private static class HubThread extends Thread {
		public HubThread() {
			super("Hub Threat");
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				QueueElement e = null;
				synchronized (HubHolder.queue) {
					if (HubHolder.queue.size() > 0) {
						e = HubHolder.queue.pollFirst();
						HubHolder.hub.publish(e.topic, e.ctx);
					} else {
						try {
							HubHolder.queue.wait();
						} catch (final InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}

	private static final HubThread threadInstance = new HubThread();
	private static final Hub hub = new Hub();

	private HubHolder() {
	}

	public static void start() {
		HubHolder.threadInstance.start();
	}

	public static void stop() {
		HubHolder.threadInstance.interrupt();
	}

	public static void publish(final String topic, final Context ctx) {
		publish(topic, ctx, true);
	}

	public static void publish(final String topic, final Context ctx,
			final boolean synchronous) {
		if (synchronous) {
			HubHolder.hub.publish(topic, ctx);
		} else {
			final QueueElement e = new QueueElement();
			e.ctx = ctx;
			e.topic = topic;
			synchronized (HubHolder.queue) {
				HubHolder.queue.add(e);
				HubHolder.queue.notify();
			}
		}
	}

	public static void subscribe(final String topic, final Object handler) {
		HubHolder.hub.subscribe(topic, handler);
	}

	public static void unsubscribe(final String topic, final Object handler) {
		HubHolder.hub.unsubscribe(topic, handler);
	}

}
