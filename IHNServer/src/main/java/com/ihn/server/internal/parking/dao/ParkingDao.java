package com.ihn.server.internal.parking.dao;

import com.ihn.server.internal.parking.model.PropertyAsset;

public interface ParkingDao {
	
	public PropertyAsset getByKey(String propertyAssetId);

}
