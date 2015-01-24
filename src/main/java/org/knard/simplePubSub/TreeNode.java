package org.knard.simplePubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// TODO synchronize this class
public class TreeNode {

	final Map<Matcher, TreeNode> nodeEntries = new HashMap<Matcher, TreeNode>();

	final Set<Object> handlers = new HashSet<Object>();

	public void addToTree(final ArrayIterator<Matcher> matcherIterator, final Object handler) {
		if (matcherIterator.hasNext()) {
			final Matcher m = matcherIterator.getNext();
			TreeNode node = this.nodeEntries.get(m);
			if (node == null) {
				node = new TreeNode();
				this.nodeEntries.put(m, node);
			}
			node.addToTree(matcherIterator, handler);
		}
		else {
			this.handlers.add(handler);
		}
	}

	public void removeFromTree(final ArrayIterator<Matcher> matcherIterator, final Object handler) {
		if (matcherIterator.hasNext()) {
			final Matcher m = matcherIterator.getNext();
			final TreeNode node = this.nodeEntries.get(m);
			if (node != null) {
				node.removeFromTree(matcherIterator, handler);
				doCleanUp(m, node);
			}
		}
		else {
			this.handlers.remove(handler);
		}
	}

	private void doCleanUp(final Matcher m, final TreeNode node) {
		if (!node.hasHandler() && !node.hasEntries()) {
			this.nodeEntries.remove(m);
		}
	}

	private boolean hasHandler() {
		return !this.handlers.isEmpty();
	}

	private boolean hasEntries() {
		return !this.nodeEntries.isEmpty();
	}

	public Set<Object> match(final ArrayIterator<String> topicPartIterator) {
		final Set<Object> results = new HashSet<Object>();
		match(topicPartIterator, results);
		return results;
	}

	void match(final ArrayIterator<String> topicPartIterator, final Set<Object> results) {
		if (topicPartIterator.hasNext()) {
			for (final Entry<Matcher, TreeNode> e : this.nodeEntries.entrySet()) {
				e.getKey().apply(e.getValue(), topicPartIterator.clone(), results);
			}
		}
		else {
			results.addAll(this.handlers);
		}
	}
}
