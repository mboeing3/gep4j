package org.gep4j;

public class Value<T> {
	private ThreadLocal<T> value;

	public T getValue() {
		return value.get();
	}

	public void setValue(T value) {
		this.value.set(value);
	}
	
}
