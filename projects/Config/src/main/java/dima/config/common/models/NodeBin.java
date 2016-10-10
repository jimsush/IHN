package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

public class NodeBin {

	private String version="";
	private String date="";
	private short fileNo=1;
	private List<NodeDevice> nodeCfgs;
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
	
	public List<NodeDevice> getNodeCfgs() {
		if(nodeCfgs==null){
			nodeCfgs=new ArrayList<>();
		}
		return nodeCfgs;
	}
	
	public void addNode(NodeDevice node){
		List<NodeDevice> nodeCfgs2 = this.getNodeCfgs();
		nodeCfgs2.add(node);
	}
	
	public void setNodeCfgs(List<NodeDevice> nodeCfgs) {
		this.nodeCfgs = nodeCfgs;
	}
	
}
