package dima.config.view.versionconfig;

import twaver.Node;

public class SingleColumnNode<T> extends Node {

	private static final long serialVersionUID = 9155423237749311496L;

	private T value;
	
	public SingleColumnNode(T value){
		super(value);
		this.setValue(value);
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
}
