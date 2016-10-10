package dima.config.common.models.vo;

import java.util.ArrayList;
import java.util.List;

public class ConfigMessageItem {
	
	private String messageName;
	private String type;
	private int msgId;
	private String peroid; //10000ns
	private int maxLength;
	private int msgUse;
	
	private String refSender; //name='ES_MMP_01
	private String refReceiver; //ES_IDMP
	
	public String getMessageName() {
		return messageName;
	}
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	public String getType() {
		return type;
	}
	public short getTypeID() {
		if("BEMsg".equals(type)){
			return 1;
		}else if("RCMsg".equals(type)){
			return 2;
		}else if("TTMsg".equals(type)){
			return 3;
		}
		return 1;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public String getPeroid() {
		return peroid;
	}
	
	public long getPeroidLong() {
		return parsePeriod(peroid);
	}
	
	public void setPeroid(String peroid) {
		this.peroid = peroid;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public int getMsgUse() {
		return msgUse;
	}
	public void setMsgUse(int msgUse) {
		this.msgUse = msgUse;
	}
	public String getRefSender() {
		return refSender;
	}
	
	public List<String> getRefSenderDevices() {
		return parseRefDevices(this.refSender);
	}
	
	public void setRefSender(String refSender) {
		this.refSender = refSender;
	}
	
	public String getRefReceiver() {
		return refReceiver;
	}
	
	public List<String> getRefReceiverDevices() {
		return parseRefDevices(this.refReceiver);
	}
	
	public void setRefReceiver(String refReceiver) {
		this.refReceiver = refReceiver;
	}
	
	public static List<String> parseRefDevices(String ref){
		List<String> refDevs=new ArrayList<>();
		if(ref==null){
			return refDevs;
		}
		
		String[] fields = ref.split(" ");
		for(String f : fields){
			int pos=f.indexOf("name='");
			String refDev=f.substring(pos+6, f.length()-2);
			refDevs.add(refDev);
		}
		return refDevs;
	}
	
	public static long parsePeriod(String peroid){
		if(peroid==null){
			return 0L;
		}
		char u=peroid.charAt(peroid.length()-2);
		if(u>='0' && u<='9'){
			return Long.valueOf(peroid);
		}else{
			String n=peroid.substring(0, peroid.length()-2);
			return Long.valueOf(n);
		}
	}
	
	public static void main(String[] args){
		List<String> devs=parseRefDevices("topo_ctrl.xml#//@devices/@device[name='ES_IDMP'] topo_ctrl.xml#//@devices/@device[name='ES_DCMP'] topo_ctrl.xml#//@devices/@device[name='ES_MMP_02']");
		System.out.println(devs);
		
		long per = parsePeriod("12345ns");
		System.out.println(per);
		
		per = parsePeriod("123456");
		System.out.println(per);
	}
	
}
