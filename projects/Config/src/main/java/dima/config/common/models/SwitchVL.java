package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

import twaver.Node;

public class SwitchVL extends Node{
	

	private static final long serialVersionUID = 9109774742013527877L;
	
	private String switchName;
	
	private int VLID;
	private short type;  //1-BE¡¢2-RC¡¢3-TT
	
	private short inputPortNo;
	
	private List<Integer> outputPortNos;
	
	private int bag;
	private int jitter;
	
	private short priority;
	
	/** tt start interval */
	private int ttInterval=0;
	
	private int ttSentInterval=0;
	
	private int ttWindowOffset=0;
	private int ttWindowStart=0;
	private int ttWindowEnd=0;
	
	public SwitchVL(String switchName, int VLID){
		super(switchName+"_"+VLID);
		
		this.switchName=switchName;
		this.VLID=VLID;
	}
	
	public SwitchVL(String switchName, int VLID, 
			short type, int bag, int jitter, short inputPortNo, int[] outputPortNoList){
		this(switchName, VLID);
		
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
	public int getTtWindowOffset() {
		return ttWindowOffset;
	}
	public void setTtWindowOffset(int ttWindowOffset) {
		this.ttWindowOffset = ttWindowOffset;
	}
	public short getInputPortNo() {
		return inputPortNo;
	}
	public void setInputPortNo(short inputPortNo) {
		this.inputPortNo = inputPortNo;
	}
	public List<Integer> getOutputPortNos() {
		return outputPortNos;
	}
	public void setOutputPortNos(List<Integer> outputPortNos) {
		this.outputPortNos = outputPortNos;
	}
	
	@Override
	public String toString(){
		return switchName+" VLID:"+VLID;
	}

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public int getTtSentInterval() {
		return ttSentInterval;
	}

	public void setTtSentInterval(int ttSentInterval) {
		this.ttSentInterval = ttSentInterval;
	}

	public int getTtWindowStart() {
		return ttWindowStart;
	}

	public void setTtWindowStart(int ttWindowStart) {
		this.ttWindowStart = ttWindowStart;
	}

	public int getTtWindowEnd() {
		return ttWindowEnd;
	}

	public void setTtWindowEnd(int ttWindowEnd) {
		this.ttWindowEnd = ttWindowEnd;
	}

}
