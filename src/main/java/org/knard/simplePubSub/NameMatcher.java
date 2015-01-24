package org.knard.simplePubSub;

import java.util.Set;

public class NameMatcher implements Matcher {

	private final String topicPartToMatch;

	public NameMatcher(final String topicPart) {
		this.topicPartToMatch = topicPart;
	}

	@Override
	public void apply(final TreeNode treeNode, final ArrayIterator<String> topicPartIterator, final Set<Object> results) {
		final String topicPart = topicPartIterator.getNext();
		if (this.topicPartToMatch.equals(topicPart)) {
			treeNode.match(topicPartIterator, results);
		}
	}

}
