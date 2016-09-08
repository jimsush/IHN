package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

import twaver.Node;

public class SwitchMonitor extends Node{

	private static final long serialVersionUID = -6656499993827603035L;

	private String switchName;
	
	private int version=1;
	private int date;
	private int fileNo=1;
	private int numberOfConfigTable=1;
	private int locationId;
	
	private int[] configTableOffset;
	
	private List<SwitchMonitorCfg> monitorCfgs;
	
	
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
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(int date) {
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

	/**
	 * @return the numberOfConfigTable
	 */
	public int getNumberOfConfigTable() {
		return numberOfConfigTable;
	}

	/**
	 * @param numberOfConfigTable the numberOfConfigTable to set
	 */
	public void setNumberOfConfigTable(int numberOfConfigTable) {
		this.numberOfConfigTable = numberOfConfigTable;
	}

	/**
	 * @return the locationId
	 */
	public int getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int[] getConfigTableOffset() {
		return configTableOffset;
	}

	public void setConfigTableOffset(int[] configTableOffset) {
		this.configTableOffset = configTableOffset;
	}

	public List<SwitchMonitorCfg> getMonitorCfgs() {
		if(monitorCfgs==null){
			monitorCfgs=new ArrayList<SwitchMonitorCfg>();
		}
		return monitorCfgs;
	}

	public void setMonitorCfgs(List<SwitchMonitorCfg> monitorCfgs) {
		this.monitorCfgs = monitorCfgs;
	}

	@Override
	public String toString(){
		return switchName;
	}
	
	
}
