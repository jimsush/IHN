package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

public class SwitchMonitorCfg implements Comparable<SwitchMonitorCfg>{
	
	private int cfgTableId;
	private int mirrorPortNum=2;
	
	private List<SwitchMonitorPort> ports=new ArrayList<>();
	
	public SwitchMonitorCfg(){}
	public SwitchMonitorCfg(int cfgTableId){
		this.cfgTableId=cfgTableId;
	}
	
	public int getCfgTableId() {
		return cfgTableId;
	}
	
	public void setCfgTableId(int cfgTableId) {
		this.cfgTableId = cfgTableId;
	}
	
	public int getMirrorPortNum() {
		return mirrorPortNum;
	}
	
	public void setMirrorPortNum(int mirrorPortNum) {
		this.mirrorPortNum = mirrorPortNum;
	}
	
	public List<SwitchMonitorPort> getMonitorPorts() {
		if(ports==null){
			return new ArrayList<SwitchMonitorPort>();
		}
		return ports;
	}
	
	public void setMonitorPorts(List<SwitchMonitorPort> ports) {
		this.ports = ports;
	}
	
	public void addMonitorPort(SwitchMonitorPort monitorPort){
		if(this.ports==null){
			this.ports=new ArrayList<>();
		}
		this.ports.add(monitorPort);
	}
	
	@Override
	public int compareTo(SwitchMonitorCfg o) {
		if(o==null){
			return 1;
		}else{
			return cfgTableId-o.cfgTableId;
		}
	}
	
}
