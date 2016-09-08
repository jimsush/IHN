package dima.config.view.switchconfig;

import twaver.Node;

public class IntegerNode extends Node {

	private static final long serialVersionUID = 9155423237749311496L;

	private int number;
	
	public IntegerNode(int number){
		super(number);
		this.setNumber(number);
	}
	
	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
}
