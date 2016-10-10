package dima.config.common.models;

import twaver.Node;

public class NodeMessage extends Node{

	private static final long serialVersionUID = -5026696581098902850L;

	private String nodeName;
	
	private int messageID;
	private String messageName;
	private int vl;
	private int maxOfLen;
	private short useOfMessage=0;
	private short snmpID;
	private short loadID;
	
	/*1-BE¡¢2-RC¡¢3-TT*/
	private short type=1;
	
	private int sID;
	private int dID;
	
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
	public short getUseOfMessage() {
		return useOfMessage;
	}
	public void setUseOfMessage(short useOfMessage) {
		this.useOfMessage = useOfMessage;
	}
	public short getSnmpID() {
		return snmpID;
	}
	public void setSnmpID(short snmpID) {
		this.snmpID = snmpID;
	}
	public short getLoadID() {
		return loadID;
	}
	public void setLoadID(short loadID) {
		this.loadID = loadID;
	}
	public int getVl() {
		return vl;
	}
	public void setVl(int vl) {
		this.vl = vl;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getSID() {
		return sID;
	}

	public void setSID(int sID) {
		this.sID = sID;
	}

	public int getDID() {
		return dID;
	}

	public void setDID(int dID) {
		this.dID = dID;
	}
	
	
}
