package dima.config.common.models.vo;

import java.util.ArrayList;
import java.util.List;

public class ADCItem {
	
	private String appName;
	private List<SendInfo> sendTable=new ArrayList<>();
	private List<ReceiveInfo> receiveTable=new ArrayList<>();
	
	public ADCItem(){}
	
	public ADCItem(String appName){
		setAppName(appName);
	}
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public List<SendInfo> getSendTable() {
		return sendTable;
	}
	
	public void addSendInfo(SendInfo sendInfo){
		if(sendTable==null){
			sendTable=new ArrayList<>();
		}
		sendTable.add(sendInfo);
	}
	
	public void setSendTable(List<SendInfo> sendTable) {
		this.sendTable = sendTable;
	}
	public List<ReceiveInfo> getReceiveTable() {
		return receiveTable;
	}
	
	public void addReceiveInfo(ReceiveInfo receiveInfo){
		if(receiveTable==null){
			receiveTable=new ArrayList<>();
		}
		receiveTable.add(receiveInfo);
	}
	
	public void setReceiveTable(List<ReceiveInfo> receiveTable) {
		this.receiveTable = receiveTable;
	}

	@Override
	public String toString(){
		return "appName:"+appName+" receiveTable:"+receiveTable.size()+" sendTable:"+sendTable.size();
	}
	
}
