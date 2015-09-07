package com.ihn.server.internal.parking.dao;

import java.util.List;

import com.ihn.server.internal.parking.model.Element;

public interface ElementDao {
	
	public List<Element> getElementsByParkId(String parkId, String category);

}
