package dima.config.view.netconfig;

import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;

public interface UpdateCallback {
	
	public NodeDevice addNode(NodeDevice nodeDev, String oldId);
	
	public SwitchDevice addSwitch(SwitchDevice switchDev, String oldId);
	
	public void deleteDevice(String twaverId, int type);
	
}
