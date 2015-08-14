package com.ihn.server.internal.parking.service;

import com.ihn.server.internal.parking.ParkingService;
import com.ihn.server.internal.parking.dao.ParkingDao;

public class ParkingServiceImpl implements ParkingService {
	
	private ParkingDao parkingDao;

	public void setParkingDao(ParkingDao parkingDao) {
		this.parkingDao = parkingDao;
	}

	@Override
	public void test() {
		this.parkingDao.getClass();
	}

	
	
}
