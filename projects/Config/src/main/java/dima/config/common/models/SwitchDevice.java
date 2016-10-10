package dima.config.common.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twaver.Node;

public class SwitchDevice extends Node{
	
	private static final long serialVersionUID = -2155055500631955470L;
	
	private String version="";
	private String date="";
	private short fileNo=1;
	
	private String switchName;
	
	private int locationId;
	
	private int portNumber;
	
	private int localDomainID;
	
	private int eportNumber;
	private List<Integer> eportNos;
	
	//private int eportFEPortNos;
	
	private boolean enableTimeSyncVL;
	
	private short timeSyncVL1;
	private short timeSyncVL2;
	
	private short pcfVL;
	
	private int timeSyncRole; //0-SM 1-SC
	
	private int overallInterval;
	
	private int clusterInterval;
	
	private short defaultPlanNo=0;
	private short planNum=1;
	private short planNo=0;
	private short vlFwdConfigNum=1;
	
	private int portsStatus;
	
	private List<SwitchVL> vls;
	
	private NodeDevice nmu;
	
	public SwitchDevice(){
	}
	
	public SwitchDevice(String switchName){
		super(switchName);
		this.switchName = switchName;
	}
	
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public int getLocalDomainID() {
		return localDomainID;
	}
	public void setLocalDomainID(int localDomainID) {
		this.localDomainID = localDomainID;
	}
	public int getEportNumber() {
		return eportNumber;
	}
	public void setEportNumber(int eportNumber) {
		this.eportNumber = eportNumber;
	}
	
	public void setEportNos(List<Integer> eportNos) {
		this.eportNos = eportNos;
	}
	
	public List<Integer> getEportNos(){
		if(eportNos==null){
			eportNos=new ArrayList<>();
		}else{
			Collections.sort(this.eportNos);
		}
		return eportNos;
	}
	
	public Set<Integer> getEportSet(){
		Set<Integer> ports=new HashSet<Integer>();
		if(eportNos!=null){
			ports.addAll(eportNos);
		}
		return ports;
	}
	
	public boolean isEnableTimeSyncVL() {
		return enableTimeSyncVL;
	}
	public void setEnableTimeSyncVL(boolean enableTimeSyncVL) {
		this.enableTimeSyncVL = enableTimeSyncVL;
	}
	public short getTimeSyncVL1() {
		return timeSyncVL1;
	}
	public void setTimeSyncVL1(short syncTimeVL1) {
		this.timeSyncVL1 = syncTimeVL1;
	}
	public int getTimeSyncRole() {
		return timeSyncRole;
	}
	public void setTimeSyncRole(int timeSyncRole) {
		this.timeSyncRole = timeSyncRole;
	}
	public int getOverallInterval() {
		return overallInterval;
	}
	public void setOverallInterval(int overallInterval) {
		this.overallInterval = overallInterval;
	}
	public int getClusterInterval() {
		return clusterInterval;
	}
	public void setClusterInterval(int clusterInterval) {
		this.clusterInterval = clusterInterval;
	}
	
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
		
	@Override
	public String toString(){
		return switchName+" portNum:"+portNumber;
	}

	public short getTimeSyncVL2() {
		return timeSyncVL2;
	}

	public void setTimeSyncVL2(short timeSyncVL2) {
		this.timeSyncVL2 = timeSyncVL2;
	}

	public short getPcfVL() {
		return pcfVL;
	}

	public void setPcfVL(short pcfVL) {
		this.pcfVL = pcfVL;
	}

	public short getDefaultPlanNo() {
		return defaultPlanNo;
	}

	public void setDefaultPlanNo(short defaultPlanNo) {
		this.defaultPlanNo = defaultPlanNo;
	}

	public short getPlanNum() {
		return planNum;
	}

	public void setPlanNum(short planNum) {
		this.planNum = planNum;
	}

	public short getPlanNo() {
		return planNo;
	}

	public void setPlanNo(short planNo) {
		this.planNo = planNo;
	}

	public int getPortsStatus() {
		return portsStatus;
	}

	public void setPortsStatus(int portsStatus) {
		this.portsStatus = portsStatus;
	}

	public List<SwitchVL> getVls() {
		if(vls==null){
			vls=new ArrayList<>();
		}
		return vls;
	}

	public void setVls(List<SwitchVL> vls) {
		this.vls = vls;
	}

	public NodeDevice getNmu() {
		return nmu;
	}

	public void setNmu(NodeDevice nmu) {
		this.nmu = nmu;
	}

	public short getVlFwdConfigNum() {
		return vlFwdConfigNum;
	}

	public void setVlFwdConfigNum(short vlFwdConfigNum) {
		this.vlFwdConfigNum = vlFwdConfigNum;
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

}
