package org.knard.simplePubSub;

import java.util.Set;

public interface Matcher {

	void apply(TreeNode treeNode, ArrayIterator<String> topicPartIterator, Set<Object> results);

}
