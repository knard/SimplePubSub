package org.knard.simplePubSub;

import java.util.Set;

public class NameMatcher implements Matcher {

	private final String topicPartToMatch;

	public NameMatcher(final String topicPart) {
		this.topicPartToMatch = topicPart;
	}

	@Override
	public void apply(final TreeNode treeNode,
			final ArrayIterator<String> topicPartIterator,
			final Set<Object> results) {
		final String topicPart = topicPartIterator.getNext();
		if (this.topicPartToMatch.equals(topicPart)) {
			treeNode.match(topicPartIterator, results);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((topicPartToMatch == null) ? 0 : topicPartToMatch.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NameMatcher other = (NameMatcher) obj;
		if (topicPartToMatch == null) {
			if (other.topicPartToMatch != null)
				return false;
		} else if (!topicPartToMatch.equals(other.topicPartToMatch))
			return false;
		return true;
	}

}
