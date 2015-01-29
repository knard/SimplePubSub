package org.knard.simplePubSub;

import java.util.Set;

public class WildcardMatcher implements Matcher {

	public final static WildcardMatcher INSTANCE = new WildcardMatcher();
	
	private WildcardMatcher() {
	}
	
	@Override
	public void apply(final TreeNode treeNode, final ArrayIterator<String> topicPartIterator, final Set<Object> results) {
		treeNode.match(topicPartIterator.clone(), results);
		while (topicPartIterator.hasNext()) {
			topicPartIterator.getNext();
			treeNode.match(topicPartIterator.clone(), results);
		}
	}

}
