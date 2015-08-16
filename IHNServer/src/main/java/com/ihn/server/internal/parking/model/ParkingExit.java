package com.ihn.server.internal.parking.model;

import java.util.Set;

/**
 * exit in the parking, such Exit 1, Exit 2...
 *
 */
public class ParkingExit extends Element{

	private Set<String> shops;

	public Set<String> getShops() {
		return shops;
	}

	public void setShops(Set<String> shops) {
		this.shops = shops;
	}
	
	
}
