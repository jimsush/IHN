package com.ihn.server.internal.integration;

import java.io.Serializable;
import java.util.Set;

public class ParkSpaceStatusBatchMessage implements Serializable{

	private static final long serialVersionUID = -8254612670273128976L;

	private Set<String> parkSpaceIds;
	
	/** O: occupied, N: no occupied */
	private String type;

	public Set<String> getParkSpaceIds() {
		return parkSpaceIds;
	}

	public void setParkSpaceIds(Set<String> parkSpaceIds) {
		this.parkSpaceIds = parkSpaceIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
