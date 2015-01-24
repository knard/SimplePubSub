package org.knard.simplePubSub;

public class ArrayIterator<T> implements Cloneable {

	private final T[] array;
	private int index;

	public ArrayIterator(final T[] array) {
		this.array = array;
		this.index = 0;
	}

	private ArrayIterator(final T[] array, final int index) {
		super();
		this.array = array;
		this.index = index;
	}

	public boolean hasNext() {
		return this.index < this.array.length;
	}

	public T getNext() {
		return this.array[this.index++];
	}

	@Override
	protected ArrayIterator<T> clone() {
		return new ArrayIterator<T>(this.array, this.index);
	}

}
