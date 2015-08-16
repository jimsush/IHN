package com.ihn.server.internal.parking.model;

public class Stall extends Element{

	/** available, used, reserved */
	private String status;
	
	/** normal, smart lock */
	private String lockType;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLockType() {
		return lockType;
	}

	public void setLockType(String lockType) {
		this.lockType = lockType;
	}
	
	
}
