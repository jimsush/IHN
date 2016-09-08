package dima.config.common.models;

public class LinkPort {
	
	private int portNo;
	private String dstSwitchName;
	private int dstPortNo;
	
	public LinkPort(){}
	
	public LinkPort(int portNo, String dstSwitchName, int dstPortNo){
		this.portNo=portNo;
		this.dstSwitchName=dstSwitchName;
		this.setDstPortNo(dstPortNo);
	}
	
	public String getDstSwitchName() {
		return dstSwitchName;
	}
	public void setDstSwitchName(String dstSwitchName) {
		this.dstSwitchName = dstSwitchName;
	}
	public int getPortNo() {
		return portNo;
	}
	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	/**
	 * @return the dstPortNo
	 */
	public int getDstPortNo() {
		return dstPortNo;
	}

	/**
	 * @param dstPortNo the dstPortNo to set
	 */
	public void setDstPortNo(int dstPortNo) {
		this.dstPortNo = dstPortNo;
	}
	

}
