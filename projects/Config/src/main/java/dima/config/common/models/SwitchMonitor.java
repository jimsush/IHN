package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

import twaver.Node;

public class SwitchMonitor extends Node{

	private static final long serialVersionUID = -6656499993827603035L;

	private String switchName;
	
	private String version;
	private String date;
	private int fileNo=1;
	
	private List<SwitchMonitorPort> monitorPorts;
	
	public SwitchMonitor(){
	}
	
	public SwitchMonitor(String switchName){
		super(switchName);
		this.switchName=switchName;
	}
	
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the fileNo
	 */
	public int getFileNo() {
		return fileNo;
	}

	/**
	 * @param fileNo the fileNo to set
	 */
	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}

	@Override
	public String toString(){
		return switchName;
	}

	public List<SwitchMonitorPort> getMonitorPorts() {
		if(monitorPorts==null){
			monitorPorts=new ArrayList<>();
		}
		return monitorPorts;
	}

	public void addMonitorPort(SwitchMonitorPort monPort){
		List<SwitchMonitorPort> monitorPorts2 = this.getMonitorPorts();
		monitorPorts2.add(monPort);
	}
	
	public void setMonitorPorts(List<SwitchMonitorPort> monitorPorts) {
		this.monitorPorts = monitorPorts;
	}
	
	
}
