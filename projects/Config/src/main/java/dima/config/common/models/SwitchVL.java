package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

import twaver.Node;

public class SwitchVL extends Node{
	

	private static final long serialVersionUID = 9109774742013527877L;
	
	private String switchName;
	private int cfgTableId;
	private int planId;
	
	private int VLID;
	private int type;  //1-BE¡¢2-RC¡¢3-TT
	private int be;
	private int bag;
	private int jitter;
	private int ttInterval=0;
	private int ttWindow=0;
	private int inputPortNo;
	private List<Integer> outputPortNos;
	
	public SwitchVL(String switchName, int cfgTableId,int planId, int VLID){
		super(switchName+"_"+cfgTableId+"_"+planId+"_"+VLID);
		
		this.switchName=switchName;
		this.cfgTableId=cfgTableId;
		this.planId=planId;
		this.VLID=VLID;
	}
	
	public SwitchVL(String switchName, int cfgTableId,int planId,int VLID, int type, int bag, int jitter, int inputPortNo, int[] outputPortNoList){
		this(switchName, cfgTableId, planId, VLID);
		
		this.type=type;
		this.bag=bag;
		this.jitter=jitter;
		this.inputPortNo=inputPortNo;
		this.outputPortNos=new ArrayList<>();
		
		if(outputPortNoList!=null && outputPortNoList.length>0){
			for(int outputPort : outputPortNoList){
				outputPortNos.add(outputPort);
			}
		}
	}
	
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
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
	public int getInputPortNo() {
		return inputPortNo;
	}
	public void setInputPortNo(int inputPortNo) {
		this.inputPortNo = inputPortNo;
	}
	public List<Integer> getOutputPortNos() {
		return outputPortNos;
	}
	public void setOutputPortNos(List<Integer> outputPortNos) {
		this.outputPortNos = outputPortNos;
	}
	public int getBe() {
		return be;
	}
	public void setBe(int be) {
		this.be = be;
	}
	/**
	 * @return the cfgTableId
	 */
	public int getCfgTableId() {
		return cfgTableId;
	}
	/**
	 * @param cfgTableId the cfgTableId to set
	 */
	public void setCfgTableId(int cfgTableId) {
		this.cfgTableId = cfgTableId;
	}
	/**
	 * @return the planId
	 */
	public int getPlanId() {
		return planId;
	}
	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	
	@Override
	public String toString(){
		return switchName+" cfgTab:"+cfgTableId+" plan:"+planId+" VLID:"+VLID;
	}

}
