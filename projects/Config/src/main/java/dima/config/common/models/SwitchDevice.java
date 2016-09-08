package dima.config.common.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twaver.Node;

public class SwitchDevice extends Node{
	
	private static final long serialVersionUID = -2155055500631955470L;
	
	private String switchName;
	
	private int version=1;
	private int date;
	private int fileNo=1;
	private int locationId;
	
	private int portNumber;
	private int localDomainID;
	private int eportNumber;
	private String eportFEPortNos;
	private boolean enableTimeSyncVL;
	private int timeSyncVL;
	private int timeSyncRole; //0-SM 1-SC
	private int overallInterval;
	private int clusterInterval;
	
	private List<SwitchVLCfg> vlCfgs=new ArrayList<>();
	
	private List<LinkPort> linkedPorts=new ArrayList<LinkPort>();

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
	
	public String getEportFEPortNos() {
		return eportFEPortNos;
	}
	
	public void setEportFEPortNos(String eportFEPortNos) {
		this.eportFEPortNos = eportFEPortNos;
	}
	
	public List<Integer> getEportFEs(){
		List<Integer> ports=new ArrayList<Integer>();
		if(eportFEPortNos!=null){
			String[] fields = eportFEPortNos.split(",");
			for(String field : fields){
				String portNoHexStr=field.trim();
				if(portNoHexStr.length()>0){
					ports.add(Integer.valueOf(portNoHexStr, 16));
				}
			}
		}
		Collections.sort(ports);
		return ports;
	}
	
	public Set<Integer> getEportFESet(){
		Set<Integer> ports=new HashSet<Integer>();
		if(eportFEPortNos!=null){
			String[] fields = eportFEPortNos.split(",");
			for(String field : fields){
				String portNoHexStr=field.trim(); //10004
				if(portNoHexStr.length()>0){
					ports.add(Integer.valueOf(portNoHexStr, 16));
				}
			}
		}
		return ports;
	}
	
	public boolean isEnableTimeSyncVL() {
		return enableTimeSyncVL;
	}
	public void setEnableTimeSyncVL(boolean enableTimeSyncVL) {
		this.enableTimeSyncVL = enableTimeSyncVL;
	}
	public int getTimeSyncVL() {
		return timeSyncVL;
	}
	public void setTimeSyncVL(int syncTimeVL) {
		this.timeSyncVL = syncTimeVL;
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
	
	public int getNumberOfConfigTable() {
		if(vlCfgs==null){
			return 0;
		}
		return vlCfgs.size();
	}
	
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	/**
	 * @return the linkedPorts
	 */
	public List<LinkPort> getLinkedPorts() {
		return linkedPorts;
	}
	/**
	 * @param linkedPorts the linkedPorts to set
	 */
	public void setLinkedPorts(List<LinkPort> linkedPorts) {
		this.linkedPorts = linkedPorts;
	}

	/**
	 * @return the vlCfgs
	 */
	public List<SwitchVLCfg> getVlCfgs() {
		if(vlCfgs==null){
			vlCfgs=new ArrayList<>();
		}
		return vlCfgs;
	}
	
	public void addVLCfg(SwitchVLCfg vlCfg){
		List<SwitchVLCfg> vlCfgs2 = this.getVlCfgs();
		vlCfgs2.add(vlCfg);
	}

	/**
	 * @param vlCfgs the vlCfgs to set
	 */
	public void setVlCfgs(List<SwitchVLCfg> vlCfgs) {
		this.vlCfgs = vlCfgs;
	}
	
	@Override
	public String toString(){
		return switchName+" portNum:"+portNumber;
	}
	

}
