package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

public class SwitchBin {

	private String version="";
	private String date="";
	private short fileNo=1;
	
	private List<SwitchDevice> swCfgs;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public short getFileNo() {
		return fileNo;
	}

	public void setFileNo(short fileNo) {
		this.fileNo = fileNo;
	}

	public List<SwitchDevice> getSwCfgs() {
		if(swCfgs==null){
			swCfgs=new ArrayList<>();
		}
		return swCfgs;
	}
	
	public void addSwitchConfig(SwitchDevice config){
		List<SwitchDevice> swCfgs2 = this.getSwCfgs();
		swCfgs2.add(config);
	}

	public void setSwCfgs(List<SwitchDevice> swCfgs) {
		this.swCfgs = swCfgs;
	}
	
	
}
