package dima.config.common.models;

import twaver.Node;

public class NodeVL extends Node{
	
	private static final long serialVersionUID = 1682500568637161945L;
	private String nodeName;
	
	private short type;  //1-BE¡¢2-RC¡¢3-TT
	
	private int VLID;
	private int bag;
	private int jitter;
	
	/** 49 */
	private int ttInterval;
	
	private int ttSentInterval;
	
	private int ttWindowStart;
	private int ttWindowOffset;
	private int ttWindowEnd;
	
	private short networkType; //both
	
	private short completeCheck=0;
	private short redudanceType=0;
	
	private short useOfLink=0;
	private int rtcInterval=0;
	
	private int switchPortNo;
	
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
	public short getType() {
		return type;
	}
	public void setType(short type) {
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
	public int getTtWindowStart() {
		return ttWindowStart;
	}
	public void setTtWindowStart(int ttWindow) {
		this.ttWindowStart = ttWindow;
	}
	public short getNetworkType() {
		return networkType;
	}
	public void setNetworkType(short networkType) {
		this.networkType = networkType;
	}
	public short getRedudanceType() {
		return redudanceType;
	}
	public void setRedudanceType(short redudanceType) {
		this.redudanceType = redudanceType;
	}
	public short getUseOfLink() {
		return useOfLink;
	}
	public void setUseOfLink(short useOfLink) {
		this.useOfLink = useOfLink;
	}
	public int getRtcInterval() {
		return rtcInterval;
	}
	public void setRtcInterval(int rtcInterval) {
		this.rtcInterval = rtcInterval;
	}
	public short getCompleteCheck() {
		return completeCheck;
	}
	public void setCompleteCheck(short completeCheck) {
		this.completeCheck = completeCheck;
	}

	public int getTtSentInterval() {
		return ttSentInterval;
	}

	public void setTtSentInterval(int ttSentInterval) {
		this.ttSentInterval = ttSentInterval;
	}

	public int getTtWindowOffset() {
		return ttWindowOffset;
	}

	public void setTtWindowOffset(int ttWindowOffset) {
		this.ttWindowOffset = ttWindowOffset;
	}

	public int getTtWindowEnd() {
		return ttWindowEnd;
	}

	public void setTtWindowEnd(int ttWindowEnd) {
		this.ttWindowEnd = ttWindowEnd;
	}

	public int getSwitchPortNo() {
		return switchPortNo;
	}

	public void setSwitchPortNo(int switchPortNo) {
		this.switchPortNo = switchPortNo;
	}
	
	

}
