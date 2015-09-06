package com.ihn.server.internal.parking.service;

import java.util.List;

import com.ihn.server.internal.parking.ParkingService;
import com.ihn.server.internal.parking.dao.ElementDao;
import com.ihn.server.internal.parking.dao.ParkingDao;
import com.ihn.server.internal.parking.model.Element;
import com.ihn.server.internal.parking.model.PropertyAsset;

public class ParkingServiceImpl implements ParkingService {
	
	private ParkingDao parkingDao;
	private ElementDao elementDao;

	public void setParkingDao(ParkingDao parkingDao) {
		this.parkingDao = parkingDao;
	}

	@Override
	public PropertyAsset getPropertyAsset(String propertyAssetId) {
		return parkingDao.getByKey(propertyAssetId);
	}

	@Override
	public PropertyAsset createPropertyAsset(PropertyAsset propertyAsset) {
		if(propertyAsset.getId()==null || propertyAsset.getId().length()==0){
			propertyAsset.setId(propertyAsset.getCorp()+"_P"+System.currentTimeMillis());
		}
		parkingDao.insert(propertyAsset);
		return propertyAsset;
	}

	public void setElementDao(ElementDao elementDao) {
		this.elementDao = elementDao;
	}
	
	@Override
	public List<Element> getElementsByParkId(String parkId, String category){
		return this.elementDao.getElementsByParkId(parkId, category);
	}

}
