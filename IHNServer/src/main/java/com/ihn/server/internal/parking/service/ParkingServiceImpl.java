package com.ihn.server.internal.parking.service;

import com.ihn.server.internal.parking.ParkingService;
import com.ihn.server.internal.parking.dao.ParkingDao;
import com.ihn.server.internal.parking.model.PropertyAsset;

public class ParkingServiceImpl implements ParkingService {
	
	private ParkingDao parkingDao;

	public void setParkingDao(ParkingDao parkingDao) {
		this.parkingDao = parkingDao;
	}

	@Override
	public PropertyAsset getPropertyAsset(String propertyAssetId) {
		return parkingDao.getByKey(propertyAssetId);
	}

	@Override
	public PropertyAsset createPropertyAsset(PropertyAsset propertyAsset) {
		parkingDao.insert(propertyAsset);
		return propertyAsset;
	}

	
	
	
}
