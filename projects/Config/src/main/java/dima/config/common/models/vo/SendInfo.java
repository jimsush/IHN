package dima.config.common.models.vo;

public class SendInfo {
	protected int messageID;
	protected int maxMessageLength;
	protected String netDevice;
	
	public int getMessageID() {
		return messageID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	public int getMaxMessageLength() {
		return maxMessageLength;
	}
	public void setMaxMessageLength(int maxMessageLength) {
		this.maxMessageLength = maxMessageLength;
	}
	public String getNetDevice() {
		return netDevice;
	}
	public void setNetDevice(String netDevice) {
		this.netDevice = netDevice;
	}
	
}
