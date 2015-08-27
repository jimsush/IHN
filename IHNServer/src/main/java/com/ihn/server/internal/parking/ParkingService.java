package com.ihn.server.internal.parking;

import com.ihn.server.internal.parking.model.PropertyAsset;

/**
 * parking
 *
 */
public interface ParkingService {
	
	public PropertyAsset getPropertyAsset(String propertyAssetId);
	
	public PropertyAsset createPropertyAsset(PropertyAsset propertyAsset);
	
}
