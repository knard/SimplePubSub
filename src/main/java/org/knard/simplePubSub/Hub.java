package org.knard.simplePubSub;

import java.util.Set;

import org.knard.SimpleInject.Context;
import org.knard.SimpleInject.InvocationException;
import org.knard.SimpleInject.InvokerRepository;
import org.knard.SimpleInject.Invoker;
import org.knard.SimpleInject.InvokerRepositoryCached;

public class Hub {

	private final TreeNode root = new TreeNode();

	private final InvokerRepository invokerRepository = new InvokerRepositoryCached();

	public void subscribe(final String topic, final Object handler) {
		final Matcher[] matchers = createMatcher(topic);
		add(matchers, handler);
	}

	public void unsubscribe(final String topic, final Object handler) {
		final Matcher[] matchers = createMatcher(topic);
		remove(matchers, handler);
	}

	public void publish(final String topic, final Context ctx) throws PublishingException {
		final Set<Object> handlers = match(topic);
		for (final Object handler : handlers) {
			final Invoker invoker = this.invokerRepository.getInvoker(handler.getClass());
			try {
				invoker.invokeInContext(ctx, handler);
			} catch (InvocationException e) {
				throw new PublishingException(e);
			}
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
				matchers[i] = WildcardMatcher.INSTANCE;
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
