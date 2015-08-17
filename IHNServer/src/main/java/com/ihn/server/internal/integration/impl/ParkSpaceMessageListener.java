package com.ihn.server.internal.integration.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ihn.server.internal.integration.ParkSpaceMessageHandler;
import com.ihn.server.internal.integration.ParkSpaceStatusBatchMessage;
import com.ihn.server.internal.integration.ParkSpaceStatusMessage;
import com.ihn.server.internal.launch.BizContext;

public class ParkSpaceMessageListener {

	private Map<String,ParkSpaceMessageHandler> handlers=new ConcurrentHashMap<String,ParkSpaceMessageHandler>();
	
	public void registerHandler(String name,ParkSpaceMessageHandler handler){
		handlers.put(name,handler);
	}
	
	public void registerHandler(ParkSpaceMessageHandler handler){
		handlers.put(handler.getClass().getName(), handler);
	}
	
	public void onMessage(Object object){
		if(handlers.size()==0){
			BizContext.getLogger().warn("no handler.");
			return;
		}
		
		if(object instanceof ParkSpaceStatusBatchMessage){
			for(Map.Entry<String,ParkSpaceMessageHandler> entry : handlers.entrySet()){
				entry.getValue().batchUpdate((ParkSpaceStatusBatchMessage) object);
			}
		}else if(object instanceof ParkSpaceStatusMessage){
			for(Map.Entry<String,ParkSpaceMessageHandler> entry : handlers.entrySet()){
				entry.getValue().update((ParkSpaceStatusMessage) object);
			}
		}else{
			BizContext.getLogger().warn("can't recognize the message object:"+object.getClass().getSimpleName());
		}
	}
	
}
