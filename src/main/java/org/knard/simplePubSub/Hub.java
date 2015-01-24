package org.knard.simplePubSub;

import java.util.Set;

import org.knard.SimpleInject.Context;
import org.knard.SimpleInject.DI;
import org.knard.SimpleInject.Invoker;

public class Hub {

	private final TreeNode root = new TreeNode();

	private final DI di = new DI();

	public void subscribe(final String topic, final Object handler) {
		final Matcher[] matchers = createMatcher(topic);
		add(matchers, handler);
	}

	public void unsubscribe(final String topic, final Object handler) {
		final Matcher[] matchers = createMatcher(topic);
		remove(matchers, handler);
	}

	public void publish(final String topic, final Context ctx) {
		final Set<Object> handlers = match(topic);
		for (final Object handler : handlers) {
			final Invoker invoker = this.di.getInvoker(handler.getClass());
			invoker.invokeInContext(ctx, handler);
		}
	}

	private Set<Object> match(final String topic) {
		final String[] topicParts = topic.split("/");
		return this.root.match(new ArrayIterator<String>(topicParts));
	}

	private Matcher[] createMatcher(final String topic) {
		final String[] topicParts = topic.split("/");
		final Matcher[] matchers = new Matcher[topicParts.length];
		for (int i = 0; i < topicParts.length; i++) {
			final String topicPart = topicParts[i];
			if ("*".equals(topicPart)) {
				matchers[i] = new WildcardMatcher();
			}
			else {
				matchers[i] = new NameMatcher(topicPart);
			}
		}
		return matchers;
	}

	private void add(final Matcher[] matchers, final Object handler) {
		synchronized (this.root) {
			this.root.addToTree(new ArrayIterator<Matcher>(matchers), handler);
		}
	}

	private void remove(final Matcher[] matchers, final Object handler) {
		synchronized (this.root) {
			this.root.removeFromTree(new ArrayIterator<Matcher>(matchers), handler);
		}
	}
}
