package com.ihn.server.internal.parking;

import java.util.List;

import com.ihn.server.internal.parking.model.Element;
import com.ihn.server.internal.parking.model.PropertyAsset;

/**
 * parking
 *
 */
public interface ParkingService {
	
	public PropertyAsset getPropertyAsset(String propertyAssetId);
	
	public PropertyAsset createPropertyAsset(PropertyAsset propertyAsset);
	
	public List<Element> getElementsByParkId(String parkId,String category);
	
}
