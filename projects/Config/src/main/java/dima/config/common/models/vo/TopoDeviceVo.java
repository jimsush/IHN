package dima.config.common.models.vo;

import java.util.ArrayList;
import java.util.List;

public class TopoDeviceVo {
	public static final String T_SW="Switch";
	public static final String T_NODE="EndSystem";
	
	private String name;
	private String type;
	private int domainId;
	private int rtcSynEn;
	
	/** port no, device name */
	private List<Object[]> ports=new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDomainId() {
		return domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	public int getRtcSynEn() {
		return rtcSynEn;
	}

	public void setRtcSynEn(int rtcSynEn) {
		this.rtcSynEn = rtcSynEn;
	}

	/** port no, device name */
	public List<Object[]> getPorts() {
		return ports;
	}

	/** port no, device name */
	public void setPorts(List<Object[]> ports) {
		this.ports = ports;
	}
	
	@Override
	public String toString(){
		return name+" id:"+domainId;
	}
	
}
