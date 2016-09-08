package dima.config.common.models;

import twaver.Node;

public class NodeMessage extends Node{

	private static final long serialVersionUID = -5026696581098902850L;

	private String nodeName;
	
	private int messageID;
	private String messageName;
	private int vl;
	private int maxOfLen;
	private int useOfMessage=0;
	private int snmpID;
	private int loadID;
	
	public NodeMessage(String nodeName, int messageID){
		super(nodeName+"_"+messageID);
		this.nodeName=nodeName;
		this.messageID=messageID;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getMessageID() {
		return messageID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	public String getMessageName() {
		return messageName;
	}
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	public int getMaxOfLen() {
		return maxOfLen;
	}
	public void setMaxOfLen(int maxOfLen) {
		this.maxOfLen = maxOfLen;
	}
	public int getUseOfMessage() {
		return useOfMessage;
	}
	public void setUseOfMessage(int useOfMessage) {
		this.useOfMessage = useOfMessage;
	}
	public int getSnmpID() {
		return snmpID;
	}
	public void setSnmpID(int snmpID) {
		this.snmpID = snmpID;
	}
	public int getLoadID() {
		return loadID;
	}
	public void setLoadID(int loadID) {
		this.loadID = loadID;
	}
	public int getVl() {
		return vl;
	}
	public void setVl(int vl) {
		this.vl = vl;
	}
	
	
}
