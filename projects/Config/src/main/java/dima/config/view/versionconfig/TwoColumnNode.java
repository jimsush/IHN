package dima.config.view.versionconfig;

import twaver.Node;

public class TwoColumnNode<T, N> extends Node {

	private static final long serialVersionUID = 9155423237749311496L;

	private T value;
	private N value2;
	
	public TwoColumnNode(T value, N value2){
		super();
		this.setValue(value);
		this.setValue2(value2);
	}
	
	/**
	 * @return the number
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * @param number the number to set
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * @return the number
	 */
	public N getValue2() {
		return value2;
	}
	
	/**
	 * @param number the number to set
	 */
	public void setValue2(N value2) {
		this.value2 = value2;
	}
}
