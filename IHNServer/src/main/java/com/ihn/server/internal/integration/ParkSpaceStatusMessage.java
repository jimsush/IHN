package com.ihn.server.internal.integration;

import java.io.Serializable;

public class ParkSpaceStatusMessage implements Serializable{

	private static final long serialVersionUID = -7430626851347107521L;

	private String parkSpaceId;
	
	/** O: occupied, N: no occupied */
	private String type;

	public String getParkSpaceId() {
		return parkSpaceId;
	}

	public void setParkSpaceId(String parkSpaceId) {
		this.parkSpaceId = parkSpaceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
