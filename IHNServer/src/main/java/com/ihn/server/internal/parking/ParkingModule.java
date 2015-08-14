package com.ihn.server.internal.parking;

import java.util.Map;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.modules.CommonModule;

public class ParkingModule implements CommonModule{

	@Override
	public String getModuleName() {
		return "Parking";
	}

	@Override
	public void init(Map<String, Object> params) {
		
	}

	@Override
	public void start() {
		BizContext.getLogger().info("start SecurityModule");
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void destroy() {
		
	}

}
