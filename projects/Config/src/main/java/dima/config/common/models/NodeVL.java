package dima.config.common.models;

import twaver.Node;

public class NodeVL extends Node{
	
	private static final long serialVersionUID = 1682500568637161945L;
	private String nodeName;
	private int VLID;
	private int type;  //1-BE¡¢2-RC¡¢3-TT
	private int bag;
	private int jitter;
	private int ttInterval;
	private int ttWindow;
	private int networkType; //both
	private int completeCheck=0;
	private int redudanceType=0;
	private int useOfLink=0;
	private int rtcInterval=0;
	
	public NodeVL(String nodeName, int VLID){
		super(nodeName+"_"+VLID);
		this.nodeName=nodeName;
		this.VLID=VLID;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getVLID() {
		return VLID;
	}
	public void setVLID(int vLID) {
		VLID = vLID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getBag() {
		return bag;
	}
	public void setBag(int bag) {
		this.bag = bag;
	}
	public int getJitter() {
		return jitter;
	}
	public void setJitter(int jitter) {
		this.jitter = jitter;
	}
	public int getTtInterval() {
		return ttInterval;
	}
	public void setTtInterval(int ttInterval) {
		this.ttInterval = ttInterval;
	}
	public int getTtWindow() {
		return ttWindow;
	}
	public void setTtWindow(int ttWindow) {
		this.ttWindow = ttWindow;
	}
	public int getNetworkType() {
		return networkType;
	}
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}
	public int getRedudanceType() {
		return redudanceType;
	}
	public void setRedudanceType(int redudanceType) {
		this.redudanceType = redudanceType;
	}
	public int getUseOfLink() {
		return useOfLink;
	}
	public void setUseOfLink(int useOfLink) {
		this.useOfLink = useOfLink;
	}
	public int getRtcInterval() {
		return rtcInterval;
	}
	public void setRtcInterval(int rtcInterval) {
		this.rtcInterval = rtcInterval;
	}
	public int getCompleteCheck() {
		return completeCheck;
	}
	public void setCompleteCheck(int completeCheck) {
		this.completeCheck = completeCheck;
	}
	
	

}
