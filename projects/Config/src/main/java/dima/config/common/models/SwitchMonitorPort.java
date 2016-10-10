package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

import twaver.Node;

public class SwitchMonitorPort extends Node{

	private static final long serialVersionUID = -6656499993827603035L;

	private String switchName;
	
	private int configTableId=1;
	
	private int locationId;
	
	private int portId;
	
	private int portEnableMonitor;
	
	private int portMonitorMode; // port or VL
	
	private List<Integer> portInputPortList=new ArrayList<>();  //0-16
	
	private List<Integer> portOutputPortList=new ArrayList<>(); 
	
	private List<Integer> portVLList=new ArrayList<>();
	
	public SwitchMonitorPort(){
	}
	
	public SwitchMonitorPort(String switchName, int configTableId, int portId){
		super(switchName+"-"+configTableId+"-"+portId);
		
		this.switchName=switchName;
		this.configTableId=configTableId;
		this.portId=portId;
	}
	
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}

	public int getConfigTableId() {
		return configTableId;
	}

	public void setConfigTableId(int configTableId) {
		this.configTableId = configTableId;
	}

	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	public int getPortEnableMonitor() {
		return portEnableMonitor;
	}

	public void setPortEnableMonitor(int portEnableMonitor) {
		this.portEnableMonitor = portEnableMonitor;
	}

	public int getPortMonitorMode() {
		return portMonitorMode;
	}

	public void setPortMonitorMode(int portMonitorMode) {
		this.portMonitorMode = portMonitorMode;
	}

	public List<Integer> getPortInputPortList() {
		if(portInputPortList==null){
			portInputPortList=new ArrayList<>();
		}
		return portInputPortList;
	}

	public void setPortInputPortList(List<Integer> portInputPortList) {
		this.portInputPortList = portInputPortList;
	}

	public List<Integer> getPortOutputPortList() {
		if(portOutputPortList==null){
			portOutputPortList=new ArrayList<>();
		}
		return portOutputPortList;
	}

	public void setPortOutputPortList(List<Integer> portOutputPortList) {
		this.portOutputPortList = portOutputPortList;
	}

	public List<Integer> getPortVLList() {
		if(portVLList==null){
			portVLList=new ArrayList<>();
		}
		return portVLList;
	}

	public void setPortVLList(List<Integer> portVLList) {
		this.portVLList = portVLList;
	}
	
	@Override
	public String toString(){
		return switchName+" "+configTableId+" "+portId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
}
