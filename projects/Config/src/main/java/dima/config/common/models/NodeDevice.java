package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

import dima.config.common.ConfigUtils;
import twaver.Node;

/**
 * node or SwitchNetworkManagementUnit
 *
 */
public class NodeDevice extends Node{

	private static final long serialVersionUID = 7903874619290964484L;
	
	private String nodeName;
	
	/** ConfigUtils.TYPE_NODE, TYPE_NMU */
	private int type=0; //0 node 1 NMU
	
	private String version;
	private String date;
	private short fileNo=1;

	private int locationId;
	
	private int roleOfNM;
	private int roleOfNetworkLoad;
	
	/**  13 */
	private int roleOfTimerSync;
	
	/** 15 */
	private int roleOfTimeSync;
	
	private int rtcSendInterval;
	
	private int overallInterval;
	
	private short clusterInterval;
	
	private int portNo=0;
	
	private List<NodeMessage> rxMsgs;
	private List<NodeMessage> txMsgs;
	private List<NodeVL> rxVls;
	private List<NodeVL> txVls;
	
	public List<NodeMessage> getRxMsgs() {
		if(rxMsgs==null){
			rxMsgs=new ArrayList<>();
		}
		return rxMsgs;
	}

	public void setRxMsgs(List<NodeMessage> rxMsgs) {
		this.rxMsgs = rxMsgs;
	}

	public List<NodeMessage> getTxMsgs() {
		if(txMsgs==null){
			txMsgs=new ArrayList<>();
		}
		return txMsgs;
	}

	public void setTxMsgs(List<NodeMessage> txMsgs) {
		this.txMsgs = txMsgs;
	}

	public List<NodeVL> getRxVls() {
		if(rxVls==null){
			rxVls=new ArrayList<>();
		}
		return rxVls;
	}

	public void setRxVls(List<NodeVL> rxVls) {
		this.rxVls = rxVls;
	}

	public List<NodeVL> getTxVls() {
		if(txVls==null){
			txVls=new ArrayList<>();
		}
		return txVls;
	}

	public void setTxVls(List<NodeVL> txVls) {
		this.txVls = txVls;
	}

	public NodeDevice(String nodeName, int type){
		super(ConfigUtils.makeNodeTwaverID(nodeName, type));
		this.type=type;
		this.nodeName=nodeName;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getRoleOfNM() {
		return roleOfNM;
	}
	public void setRoleOfNM(int roleOfNM) {
		this.roleOfNM = roleOfNM;
	}
	public int getRoleOfNetworkLoad() {
		return roleOfNetworkLoad;
	}
	public void setRoleOfNetworkLoad(int roleOfNetworkLoad) {
		this.roleOfNetworkLoad = roleOfNetworkLoad;
	}
	public int getRoleOfTimeSync() {
		return roleOfTimeSync;
	}
	public void setRoleOfTimeSync(int roleOfTimeSync) {
		this.roleOfTimeSync = roleOfTimeSync;
	}
	public int getRtcSendInterval() {
		return rtcSendInterval;
	}
	public void setRtcSendInterval(int rtcSendInterval) {
		this.rtcSendInterval = rtcSendInterval;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public short getFileNo() {
		return fileNo;
	}
	public void setFileNo(short fileNo) {
		this.fileNo = fileNo;
	}
	
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getPortNo(){
		return this.portNo;
	}
	public void setPortNo(int portNo){
		this.portNo=portNo;
	}

//	public List<NodeDeviceCfg> getCfgs() {
//		if(cfgs==null){
//			cfgs=new ArrayList<>();
//		}
//		return cfgs;
//	}
//	
//	public void addCfg(NodeDeviceCfg cfg){
//		List<NodeDeviceCfg> cfgs2 = getCfgs();
//		cfgs2.add(cfg);
//	}
//
//	public void setCfgs(List<NodeDeviceCfg> cfgs) {
//		this.cfgs = cfgs;
//	}
	
	@Override
	public String toString(){
		return nodeName+" type:"+type
				+" portno:"+getPortNo();
	}

	public int getRoleOfTimerSync() {
		return roleOfTimerSync;
	}

	public void setRoleOfTimerSync(int roleOfTimerSync) {
		this.roleOfTimerSync = roleOfTimerSync;
	}

	public short getClusterInterval() {
		return clusterInterval;
	}

	public void setClusterInterval(short clusterInterval) {
		this.clusterInterval = clusterInterval;
	}

	public int getOverallInterval() {
		return overallInterval;
	}

	public void setOverallInterval(int overallInterval) {
		this.overallInterval = overallInterval;
	}
	
}
