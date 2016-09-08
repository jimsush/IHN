package dima.config.view.netconfig;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.DevicePortTableCellRenderer;
import dima.config.common.controls.EnumTableCellRenderer;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.Element;
import twaver.ElementAttribute;
import twaver.TDataBox;
import twaver.table.TElementTable;
import twaver.table.TTableModel;

public class NodeListPanel extends JPanel implements UpdateCallback{

	private static final long serialVersionUID = -3407645417475850254L;

	private TDataBox box;
	private TElementTable table;
	
	public NodeListPanel(){
		initUI();
	}
	
	private void initUI(){
		setLayout(new BorderLayout());
		
		JPanel topPane=new JPanel(new FlowLayout());
		
		JButton modifyBtn=new JButton("修改记录");
		topPane.add(modifyBtn);
		modifyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = box.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请选中一条记录");
					return;
				}
				
				openModifyNodeDialog((NodeDevice)element);
			}
		});
		
		JButton deleteBtn=new JButton("删除记录");
		topPane.add(deleteBtn);
		deleteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = box.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "没有选中任何记录");
					return;
				}
				
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除记录吗?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.OK_OPTION){
					NodeDevice nodeDevice=(NodeDevice)element;
					deleteDevice(element.getID().toString(), nodeDevice.getType());
				}
			}
		});
		
		add(topPane, BorderLayout.NORTH);
		
		initTable();
		JScrollPane tablePanel=new JScrollPane(this.table);
		add(tablePanel, BorderLayout.CENTER);
	}
	
	private void initTable(){
		box=new TDataBox();
		this.table=tableInit(box);
		
		ConfigDAO dao = ServiceFactory.getService(ConfigDAO.class);
		List<NodeDevice> nodes = dao.readAllNodeDevices(true);
		for(NodeDevice node : nodes){
			box.addElement(node);
		}
	}
	
	private TElementTable tableInit(TDataBox box){
		TElementTable table = new TElementTable(box);
		
		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);
		
		table.setElementClass(NodeDevice.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("nodeName");
		attribute.setDisplayName("节点名称");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("type");
		attribute.setDisplayName("节点类型");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("portNoToA");
		attribute.setDisplayName("A网交换机端口");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("portNoToB");
		attribute.setDisplayName("B网交换机端口");
		attributes.add(attribute);

		table.registerElementClassAttributes(NodeDevice.class, attributes);
		
		table.getColumnByName("portNoToA").setCellRenderer(new DevicePortTableCellRenderer());
		table.getColumnByName("portNoToB").setCellRenderer(new DevicePortTableCellRenderer());

		Map<Object, String> mapping=new HashMap<>();
		mapping.put(0, "普通终端");
		mapping.put(1, "交换机网络管理单元");
		EnumTableCellRenderer renderer=new EnumTableCellRenderer(mapping);
		table.getColumnByName("type").setCellRenderer(renderer);
		
		table.getTableModel().sortColumn("nodeName", TTableModel.SORT_ASCENDING);
		
		table.addElementDoubleClickedActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openModifyNodeDialog((NodeDevice)e.getSource());
			}
		});
		return table;
	}

	private void openModifyNodeDialog(NodeDevice node) {
		NewNodeDialog dialog=new NewNodeDialog(ConfigContext.mainFrame,
				"修改节点", node,  NodeListPanel.this,
				node.getType()==ConfigUtils.TYPE_NODE, null);
		dialog.setVisible(true);
	}
	
	@Override
	public NodeDevice addNode(NodeDevice newNodeDev, String oldID) {
		if(oldID==null){
			box.addElement(newNodeDev);
			return newNodeDev;
		}else{
			String oldTwaverID=ConfigUtils.makeNodeTwaverID(oldID, newNodeDev.getType());
			NodeDevice oldNodeDevice = (NodeDevice) box.getElementByID(oldTwaverID);
			NodeDevice nodeDevice=null;
			if(!newNodeDev.getNodeName().equals(oldID)
					|| newNodeDev.getPortNo()!=oldNodeDevice.getPortNo()){
				nodeDevice=new NodeDevice(newNodeDev.getNodeName(), newNodeDev.getType());
				nodeDevice.setCfgs(oldNodeDevice.getCfgs());
				nodeDevice.setVersion(oldNodeDevice.getVersion());
				nodeDevice.setDate(oldNodeDevice.getDate());
				nodeDevice.setFileNo(oldNodeDevice.getFileNo());
			}else{
				nodeDevice=oldNodeDevice;
			}
			// copy updated properties
			nodeDevice.setNodeName(newNodeDev.getNodeName());
			nodeDevice.setRoleOfNM(newNodeDev.getRoleOfNM());
			nodeDevice.setRoleOfNetworkLoad(newNodeDev.getRoleOfNetworkLoad());
			nodeDevice.setRoleOfTimeSync(newNodeDev.getRoleOfTimeSync());
			nodeDevice.setRtcSendInterval(newNodeDev.getRtcSendInterval());
			nodeDevice.setPortNoToA(newNodeDev.getPortNoToA());
			nodeDevice.setPortNoToB(newNodeDev.getPortNoToB());
			nodeDevice.setLocationId(newNodeDev.getLocationId());
			
			box.removeElementByID(oldTwaverID);
			box.addElement(nodeDevice);
			return nodeDevice;
		}
	}

	@Override
	public SwitchDevice addSwitch(SwitchDevice switchDev, String oldID) {
		return switchDev;
	}

	@Override
	public void deleteDevice(String twaverId, int type) {
		box.removeElementByID(twaverId);
		
		// delete from files
		ConfigDAO dao = ServiceFactory.getService(ConfigDAO.class);
		String nodeName = ConfigUtils.getNodeNameFromTwaverID(twaverId);
		NodeDevice nodeDevice = dao.readNodeDeviceFromCache(nodeName);
		dao.deleteNodeDevice(nodeDevice);
	}
	
}
