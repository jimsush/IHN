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
	
	private int type=0; //0 node 1 NMU
	
	private int version=1;
	private int date;
	private int fileNo=1;

	private int locationId;
	
	private int roleOfNM;
	private int roleOfNetworkLoad;
	private int roleOfTimeSync;
	private int rtcSendInterval;
	
	private int portNoToA=0;
	private int portNoToB=0;
	
	private List<NodeDeviceCfg> cfgs=null;

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
	public int getPortNoToA() {
		return portNoToA;
	}
	public void setPortNoToA(int portNoToA) {
		this.portNoToA = portNoToA;
	}
	public int getPortNoToB() {
		return portNoToB;
	}
	public void setPortNoToB(int portNoToB) {
		this.portNoToB = portNoToB;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}	
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getFileNo() {
		return fileNo;
	}
	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}
	
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	
	
	public int getPortNo(){
		if(this.portNoToA>0){
			return this.portNoToA;
		}else{
			return this.portNoToB;
		}
	}

	public List<NodeDeviceCfg> getCfgs() {
		if(cfgs==null){
			cfgs=new ArrayList<>();
		}
		return cfgs;
	}
	
	public void addCfg(NodeDeviceCfg cfg){
		List<NodeDeviceCfg> cfgs2 = getCfgs();
		cfgs2.add(cfg);
	}

	public void setCfgs(List<NodeDeviceCfg> cfgs) {
		this.cfgs = cfgs;
	}
	
	@Override
	public String toString(){
		return nodeName+" type:"+type
				+" cfgs:"+getCfgs().size()+" portno:"+getPortNo();
	}
	
}
