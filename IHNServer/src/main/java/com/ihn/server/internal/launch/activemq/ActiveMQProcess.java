package com.ihn.server.internal.launch.activemq;

import com.ihn.server.internal.launch.service.ExternalService;

public class ActiveMQProcess implements ExternalService{

	@Override
	public String getServiceName() {
		return "ActiveMQ";
	}

	@Override
	public boolean checkIsRun() {
		return false;
	}

	@Override
	public void start() {
		
	}

	@Override
	public boolean isInited() {
		return false;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void stop() {
		
	}
	

}
