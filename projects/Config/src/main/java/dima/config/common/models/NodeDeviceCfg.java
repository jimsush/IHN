package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

public class NodeDeviceCfg implements Comparable<NodeDeviceCfg>{

	private int cfgTableId=1;
	
	private List<NodeVL> rxVLs=new ArrayList<>();
	private List<NodeVL> txVLs=new ArrayList<>();
	private List<NodeMessage> rxMessages=new ArrayList<>();
	private List<NodeMessage> txMessages=new ArrayList<>();
	
	public NodeDeviceCfg(){}
	public NodeDeviceCfg(int cfgTableId){
		this.cfgTableId=cfgTableId;
	}
	
	public List<NodeVL> getRxVLs() {
		return rxVLs;
	}
	public void setRxVLs(List<NodeVL> vls) {
		this.rxVLs = vls;
	}
	public List<NodeMessage> getRxMessages() {
		return rxMessages;
	}
	public void setRxMessages(List<NodeMessage> messages) {
		this.rxMessages = messages;
	}
	
	public List<NodeVL> getTxVLs() {
		return txVLs;
	}

	public void setTxVLs(List<NodeVL> txVLs) {
		this.txVLs = txVLs;
	}

	public List<NodeMessage> getTxMessages() {
		return txMessages;
	}

	public void setTxMessages(List<NodeMessage> txMessages) {
		this.txMessages = txMessages;
	}
	
	@Override
	public String toString(){
		return "txMsgs:"+this.txMessages.size()
		+" rxMsgs:"+this.rxMessages.size()
		+" txVLs:"+this.txVLs.size()
		+" rxVLs:"+this.rxVLs.size();
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
	
	@Override
	public int compareTo(NodeDeviceCfg o) {
		if(o==null){
			return 1;
		}
		return cfgTableId-o.cfgTableId;
	}
	
}
