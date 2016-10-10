package dima.config.common.models.vo;

import java.util.HashMap;
import java.util.Map;

public class ReceiveInfo extends SendInfo {
	
	public static Map<String, Short> messageTypeMapping=new HashMap<>();
	static{
		// TODO to be confirmed
		messageTypeMapping.put("周期", (short)3);
		messageTypeMapping.put("事件", (short)1);
	}
	
	private String messageType;
	private int queueDepth;
	
	public short getRealMessageType() {
		Short typeId = messageTypeMapping.get(messageType);
		if(typeId==null){
			return (short)1;
		}
		return typeId;
	}
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public int getQueueDepth() {
		return queueDepth;
	}
	public void setQueueDepth(int queueDepth) {
		this.queueDepth = queueDepth;
	}
	
	

}
