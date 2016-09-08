package dima.config.view.nodeconfig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.EnumTableCellRenderer;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.NodeDeviceCfg;
import dima.config.common.models.NodeMessage;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import dima.config.view.ConfigTableCallback;
import dima.config.view.CreateConfigTableDialog;
import twaver.Element;
import twaver.ElementAttribute;
import twaver.Node;
import twaver.TDataBox;
import twaver.table.TElementTable;
import twaver.table.TTableModel;
import twaver.tree.ElementNode;
import twaver.tree.TTree;

public class MessageConfigPanel extends JPanel implements ConfigTableCallback{

	private static final long serialVersionUID = 6387827226195414930L;
	
	private JPanel topPanel, centerPanel, bottomPanel;
	private TDataBox sendBox, receiveBox; 
	private JComboBox<String> terminalBox, sortingBox; 
	private TElementTable sendTable, recieveTable; 
	
	private ConfigDAO dao;

	private TDataBox treebox;
	private TTree tree;
	private Node treeRoot;
	
	private String currentNodeName;
	private String rootIconURL;
	private String cfgIconURL;
	
	private NodeMessage copiedRxMsg;
	private NodeMessage copiedTxMsg;
	
	public MessageConfigPanel() {
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());
		
		initService();
		
		initTopPanel();
		
		JScrollPane treePane = initCfgTablePanel();
		JPanel rightPane=new JPanel(new BorderLayout());
		
		initCenterPanel();
		rightPane.add(centerPanel, BorderLayout.CENTER);
		
		initBottomPanel();
		rightPane.add(bottomPanel, BorderLayout.SOUTH);
	
		JSplitPane bottomPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, rightPane);
		bottomPane.setDividerLocation(140);
		this.add(bottomPane, BorderLayout.CENTER);
		
		if(terminalBox.getSelectedItem()!=null){
			String nodeName=terminalBox.getSelectedItem().toString();
			
			fillTree(nodeName);
			
			// select the first cfg table
			List<?> children = treeRoot.getChildren();
			if(children!=null && children.size()>0){
				Object child=children.get(0);
				Element cfgTableElement=(Element)child;
							
				cfgTableElement.setSelected(true);
				fillSendTable(nodeName, cfgTableElement);
				fillReceiveTable(nodeName, cfgTableElement);
			}
		}
	}

	private void initService() {
		dao = ServiceFactory.getService(ConfigDAO.class);
		rootIconURL=ConfigUtils.getImageURLString("root.png");
		cfgIconURL=ConfigUtils.getImageURLString("config.png");
	}

	private void initTopPanel() {
		topPanel = new JPanel(new BorderLayout());

		JPanel leftPanel = new JPanel(new FlowLayout());
		leftPanel.setBorder(BorderFactory.createTitledBorder("��ʾ��Χ"));

		JLabel label1 = new JLabel("ѡ���ն�:");
		leftPanel.add(label1);

		terminalBox = new JComboBox<>();

		List<String> allTerminal = getAllTerminalNames();
		for (String switchName : allTerminal) {
			terminalBox.addItem(switchName);
		}
		
		terminalBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj=terminalBox.getSelectedItem();
				if(obj==null){
					return;
				}

				String nodeName=obj.toString();
				currentNodeName=nodeName;
				
				fillTree(nodeName);
				
				Element ele = treebox.getLastSelectedElement();

				fillSendTable(nodeName, ele);
				fillReceiveTable(nodeName, ele);
			}
		});
		leftPanel.add(terminalBox);

		JLabel label2 = new JLabel("����ʽ:");
		leftPanel.add(label2);
		sortingBox = new JComboBox<>();

		List<String> allSorting = getAllSortingNames();
		for (String switchName : allSorting) {
			sortingBox.addItem(switchName);
		}
		leftPanel.add(sortingBox);

		JButton btnAddCfgTable=new JButton("�������ñ�");
		leftPanel.add(btnAddCfgTable);
		btnAddCfgTable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currentNodeName==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "����ѡ�нڵ�");
					return;
				}
				if(treeRoot!=null){
					List<?> children = treeRoot.getChildren();
					if(children!=null && children.size()>=ConfigContext.MAX_NUM_CFGTABLE){
						JOptionPane.showMessageDialog(ConfigContext.mainFrame, "Ŀǰ����4�����ñ����ܼ����������ñ�");
						return;
					}
				}
				
				CreateConfigTableDialog dialog=new CreateConfigTableDialog(ConfigContext.mainFrame,
						"�������ñ�", true, 0, MessageConfigPanel.this);
				dialog.setVisible(true);
			}
		});
		
		JButton btnDeleteCfgTable=new JButton("ɾ�����ñ�");
		leftPanel.add(btnDeleteCfgTable);
		btnDeleteCfgTable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element ele = treebox.getLastSelectedElement();
				if(ele==null){
					return;
				}
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "ȷ��Ҫɾ�����ñ���? �⽫ɾ�������ñ��µ�����VL�ͼ�����ã�", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if(confirm!=JOptionPane.OK_OPTION){
					return;
				}
				
				int cfgTableId=ConfigUtils.getCurrentCfgTableId(ele);
				//if(cfgTableId<=0){
				//	return;
				//}

				deleteConfigTable(cfgTableId);
				
				sendBox.clear();
				receiveBox.clear();
			}
		});
		
		JLabel label4=new JLabel("      ");
		leftPanel.add(label4);
		
		topPanel.add(leftPanel, BorderLayout.WEST);
		
		this.add(topPanel, BorderLayout.NORTH);
	}

	private void initCenterPanel() {
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("������Ϣ"));

		JPanel buttonPanel = new JPanel(new FlowLayout());

		JButton btn1 = new JButton("���Ӽ�¼");
		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currentNodeName==null){
					return;
				}
				Element treeElement=treebox.getLastSelectedElement();
				int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				if(cfgTableId<=0){
					return;
				}
				
				CreateMessageDialog dialog = new CreateMessageDialog(
						ConfigContext.mainFrame, "������Ϣ", null,
						MessageConfigPanel.this, 0);
				dialog.setVisible(true);
			}
		});
		buttonPanel.add(btn1);

		JButton btn2 = new JButton("�޸ļ�¼");
		btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = sendBox.getLastSelectedElement();
				if (element == null) {
					return;
				}
				
				NodeMessage data = (NodeMessage) element;
				CreateMessageDialog dialog = new CreateMessageDialog(
						ConfigContext.mainFrame, "�޸���Ϣ", data,
						MessageConfigPanel.this, 0);
				dialog.setVisible(true);
			}
		});
		buttonPanel.add(btn2);

		JButton btn3 = new JButton("ɾ����¼");
		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = sendBox.getLastSelectedElement();
				if (element == null) {
					return;
				}

				int confirm = JOptionPane.showConfirmDialog(
						ConfigContext.mainFrame, "ȷ��Ҫɾ����¼��?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.OK_OPTION) {
					deleteSendMessage((NodeMessage) element);
				}
			}
		});
		buttonPanel.add(btn3);

		JButton copyBtn=new JButton("���Ƽ�¼");
		buttonPanel.add(copyBtn);
		copyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = sendBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "û��ѡ���κμ�¼");
					return;
				}
				NodeMessage currentMsg=(NodeMessage)element;
				copiedTxMsg=cloneMessage(currentMsg, currentMsg.getMessageID());
			}
		});

		JButton pasteBtn=new JButton("ճ����¼");
		buttonPanel.add(pasteBtn);
		pasteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(copiedTxMsg==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "���ȸ���һ��������Ϣ��¼");
					return;
				}
				
				Element treeElement=treebox.getLastSelectedElement();
				int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				if(cfgTableId>0){
					int nextMsgId=getNextMessageId(sendBox);
					NodeMessage newMsg=cloneMessage(copiedTxMsg, nextMsgId);
					
					addSendMessage(ConfigContext.mainFrame, newMsg, null);
				}
			}
		});
		
		centerPanel.add(buttonPanel, BorderLayout.NORTH);

		initSendTable();
		JScrollPane tablePanel = new JScrollPane(this.sendTable);

		centerPanel.add(tablePanel, BorderLayout.CENTER);
	}

	private int getNextMessageId(TDataBox tableBox) {
		List<?> elements = tableBox.getAllElements();
		if(elements==null || elements.size()==0){
			return 1;
		}
		int maxId=1;
		for(Object obj : elements){
			NodeMessage msg=(NodeMessage)obj;
			if(msg.getMessageID()>=maxId){
				maxId=msg.getMessageID();
			}
		}
		return maxId+1;
	}
	
	private NodeMessage cloneMessage(NodeMessage msg, int nextMsgId) {
		NodeMessage newMsg=new NodeMessage(msg.getNodeName(), nextMsgId);
		newMsg.setMessageName(msg.getMessageName());
		newMsg.setVl(msg.getVl());
		newMsg.setMaxOfLen(msg.getMaxOfLen());
		newMsg.setUseOfMessage(msg.getUseOfMessage());
		newMsg.setSnmpID(msg.getSnmpID());
		newMsg.setLoadID(msg.getLoadID());
		return newMsg;
	}

	private void initBottomPanel() {
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createTitledBorder("������Ϣ"));

		JPanel buttonPanel = new JPanel(new FlowLayout());

		JButton btn1 = new JButton("���Ӽ�¼");
		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element treeElement=treebox.getLastSelectedElement();
				int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				if(cfgTableId<=0){
					return;
				}
				
				CreateMessageDialog dialog = new CreateMessageDialog(
						ConfigContext.mainFrame, "������Ϣ", null,
						MessageConfigPanel.this, 1);
				dialog.setVisible(true);
			}
		});
		buttonPanel.add(btn1);

		JButton btn2 = new JButton("�޸ļ�¼");
		btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = receiveBox.getLastSelectedElement();
				if (element == null) {
					return;
				}
				NodeMessage data = (NodeMessage) element;
				CreateMessageDialog dialog = new CreateMessageDialog(
						ConfigContext.mainFrame, "�޸���Ϣ", data,
						MessageConfigPanel.this, 1);
				dialog.setVisible(true);
			}
		});
		buttonPanel.add(btn2);

		JButton btn3 = new JButton("ɾ����¼");
		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = receiveBox.getLastSelectedElement();
				if (element == null) {
					JOptionPane.showMessageDialog(ConfigContext.mainFrame,
							"û��ѡ���κμ�¼");
					return;
				}

				int confirm = JOptionPane.showConfirmDialog(
						ConfigContext.mainFrame, "ȷ��Ҫɾ����¼��?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.OK_OPTION) {
					deleteRecieveMessage((NodeMessage) element);
				}
			}
		});
		buttonPanel.add(btn3);

		JButton copyBtn=new JButton("���Ƽ�¼");
		buttonPanel.add(copyBtn);
		copyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = receiveBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "û��ѡ���κμ�¼");
					return;
				}
				NodeMessage currentMsg=(NodeMessage)element;
				copiedRxMsg=cloneMessage(currentMsg, currentMsg.getMessageID());
			}
		});

		JButton pasteBtn=new JButton("ճ����¼");
		buttonPanel.add(pasteBtn);
		pasteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(copiedRxMsg==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "���ȸ���һ��������Ϣ��¼");
					return;
				}
				
				Element treeElement=treebox.getLastSelectedElement();
				int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				if(cfgTableId>0){
					int nextMsgId=getNextMessageId(receiveBox);
					NodeMessage newMsg=cloneMessage(copiedRxMsg, nextMsgId);
					
					addRecieveMessage(ConfigContext.mainFrame, newMsg, null);
				}
			}
		});

		bottomPanel.add(buttonPanel, BorderLayout.NORTH);

		initRecieveTable();
		JScrollPane tablePanel = new JScrollPane(this.recieveTable);
		
		bottomPanel.setPreferredSize(new Dimension(500, 360));
		bottomPanel.add(tablePanel, BorderLayout.CENTER);
	}

	private List<String> getAllTerminalNames() {

		List<String> list = new ArrayList<String>();
		List<NodeDevice> switches = dao.readAllNodeDevices(true);
		for (NodeDevice sw : switches) {
			list.add(sw.getNodeName());
		}
		return list;
	}

	private List<String> getAllSortingNames() {
		List<String> list = new ArrayList<String>();
		list.add("������·��");
		return list;
	}

	private void fillSendTable(String nodeName, Element cfgTableElement) {
		sendBox.clear();

		int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		if(currentCfgTableId<=0){
			return;
		}
		
		NodeDevice nodeDevice = dao.readNodeDeviceFromCache(nodeName);
		if (nodeDevice != null) {
			List<NodeDeviceCfg> cfgs = nodeDevice.getCfgs();
			for(NodeDeviceCfg cfg : cfgs){
				if(cfg.getCfgTableId()==currentCfgTableId){
					List<NodeMessage> msgs = cfg.getTxMessages();
					if (msgs != null) {
						for (NodeMessage msg : msgs) {
							sendBox.addElement(msg);
						}
					}
					break;
				}
			}
		}
	}

	private void fillReceiveTable(String nodeName, Element cfgTableElement) {
		receiveBox.clear();

		int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		if(currentCfgTableId<=0){
			return;
		}
		
		NodeDevice nodeDevice = dao.readNodeDeviceFromCache(nodeName);
		if (nodeDevice != null) {
			List<NodeDeviceCfg> cfgs = nodeDevice.getCfgs();
			for(NodeDeviceCfg cfg : cfgs){
				if(cfg.getCfgTableId()==currentCfgTableId){
					List<NodeMessage> msgs = cfg.getRxMessages();
					if (msgs != null) {
						for (NodeMessage msg : msgs) {
							receiveBox.addElement(msg);
						}
					}
					break;
				}
			}
		}
	}

	private void initSendTable() {
		sendBox = new TDataBox();
		this.sendTable = tableInit(sendBox, 0);
	}

	private void initRecieveTable() {
		receiveBox = new TDataBox();
		this.recieveTable = tableInit(receiveBox, 1);
	}

	private TElementTable tableInit(TDataBox box, int txOrRx) {
		TElementTable table = new TElementTable(box);

		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);

		table.setElementClass(NodeMessage.class);

		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("nodeName");
		attribute.setDisplayName("�豸����");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("vl");
		attribute.setDisplayName("VL_ID");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("messageID");
		attribute.setDisplayName("��ϢID");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("messageName");
		attribute.setDisplayName("��Ϣ����");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("maxOfLen");
		attribute.setDisplayName("������󳤶�");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("useOfMessage");
		attribute.setDisplayName("��Ϣ��;");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("snmpID");
		attribute.setDisplayName("SnmpID");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("loadID");
		attribute.setDisplayName("LoadID");
		attributes.add(attribute);

		table.registerElementClassAttributes(NodeMessage.class, attributes);

		Map<Object, String> mapping=new HashMap<Object, String>();
		mapping.put(1, "�����");
		mapping.put(2, "���ݹ����");
		EnumTableCellRenderer renderer1=new EnumTableCellRenderer(mapping);
		table.getColumnByName("snmpID").setCellRenderer(renderer1);
		
		Map<Object, String> mapping2=new HashMap<Object, String>();
		mapping2.put(0, "������1");
		mapping2.put(1, "������2");
		EnumTableCellRenderer renderer2=new EnumTableCellRenderer(mapping2);
		table.getColumnByName("loadID").setCellRenderer(renderer2);
		
		table.getTableModel().sortColumn("vl", TTableModel.SORT_ASCENDING);
		
		table.addElementDoubleClickedActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				NodeMessage oldMessage=((NodeMessage)e.getSource());
				CreateMessageDialog dialog = new CreateMessageDialog(
						ConfigContext.mainFrame, "�޸���Ϣ", oldMessage,
						MessageConfigPanel.this, txOrRx);
				dialog.setVisible(true);
			}
		});
		
		return table;
	}

	public void addRecieveMessage(Window dlg, NodeMessage newMessage, NodeMessage oldMessage) {
		Element cfgTableElement = treebox.getLastSelectedElement();
		int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		if(currentCfgTableId<=0){
			JOptionPane.showConfirmDialog(dlg, "û��ѡ����Ч�����ñ�");
			return;
		}
		
		NodeDevice node = dao.readNodeDeviceFromCache(currentNodeName);
		List<NodeDeviceCfg> cfgs = node.getCfgs();
		if(oldMessage==null){
			receiveBox.addElement(newMessage);

			for(NodeDeviceCfg cfg : cfgs){
				if(cfg.getCfgTableId()==currentCfgTableId){
					List<NodeMessage> msgs = cfg.getRxMessages();
					if(msgs==null){
						msgs =new ArrayList<NodeMessage>();
						cfg.setRxMessages(msgs);
					}
					msgs.add(newMessage);
					break;
				}
			}
			
			dao.saveNodeDevice(node, null);
		}else{
			receiveBox.removeElement(oldMessage);
	
			for(NodeDeviceCfg cfg : cfgs){
				if(cfg.getCfgTableId()==currentCfgTableId){
					List<NodeMessage> msgs = cfg.getRxMessages();
					if(msgs==null || msgs.size()==0){
						msgs =new ArrayList<NodeMessage>();
						cfg.setRxMessages(msgs);
						msgs.add(newMessage);
					}else{
						for(NodeMessage msg : msgs){
							if(msg.getMessageID()==newMessage.getMessageID()){ //matched than update
								msg.setLoadID(newMessage.getLoadID());
								msg.setMaxOfLen(newMessage.getMaxOfLen());
								msg.setMessageName(newMessage.getMessageName());
								msg.setSnmpID(newMessage.getSnmpID());
								msg.setUseOfMessage(newMessage.getUseOfMessage());
								msg.setVl(newMessage.getVl());
								break;
							}
						}
					}
					break;
				}
			}
			
			dao.saveNodeDevice(node, null);
			
			receiveBox.addElement(newMessage);
		}
	}

	public void addSendMessage(Window dlg, NodeMessage newMessage, NodeMessage oldMessage) {
		Element cfgTableElement = treebox.getLastSelectedElement();
		int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		if(currentCfgTableId<=0){
			JOptionPane.showConfirmDialog(dlg, "û��ѡ����Ч�����ñ�");
			return;
		}
		
		NodeDevice node = dao.readNodeDeviceFromCache(currentNodeName);
		List<NodeDeviceCfg> cfgs = node.getCfgs();
		if(oldMessage==null){
			sendBox.addElement(newMessage);

			for(NodeDeviceCfg cfg : cfgs){
				if(cfg.getCfgTableId()==currentCfgTableId){
					List<NodeMessage> msgs = cfg.getTxMessages();
					if(msgs==null){
						msgs =new ArrayList<NodeMessage>();
						cfg.setTxMessages(msgs);
					}
					msgs.add(newMessage);
					break;
				}
			}
			
			dao.saveNodeDevice(node, null);
		}else{
			sendBox.removeElement(oldMessage);

			for(NodeDeviceCfg cfg : cfgs){
				if(cfg.getCfgTableId()==currentCfgTableId){
					List<NodeMessage> msgs = cfg.getTxMessages();
					if(msgs==null || msgs.size()==0){
						msgs = new ArrayList<NodeMessage>();
						cfg.setTxMessages(msgs);
						msgs.add(newMessage);
					}else{
						for(NodeMessage msg : msgs){
							if(msg.getMessageID()==newMessage.getMessageID()){ 
								msg.setLoadID(newMessage.getLoadID());
								msg.setMaxOfLen(newMessage.getMaxOfLen());
								msg.setMessageName(newMessage.getMessageName());
								msg.setSnmpID(newMessage.getSnmpID());
								msg.setUseOfMessage(newMessage.getUseOfMessage());
								msg.setVl(newMessage.getVl());
								break;
							}
						}
					}
					break;
				}
			}
			
			dao.saveNodeDevice(node, null);
			
			sendBox.addElement(newMessage);
		}
	}

	public void deleteSendMessage(NodeMessage currentMessage) {
		deleteMessage(currentMessage, true);
	}

	public void deleteRecieveMessage(NodeMessage currentMessage) {
		deleteMessage(currentMessage, false);
	}
	
	public void deleteMessage(NodeMessage currentMessage, boolean isTx){
		Element cfgTableElement = treebox.getLastSelectedElement();
		int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		if(currentCfgTableId<=0){
			return;
		}
		
		NodeDevice node = dao.readNodeDeviceFromCache(currentNodeName);
		if(node!=null){
			List<NodeDeviceCfg> cfgs = node.getCfgs();
			for(NodeDeviceCfg cfg : cfgs){
				if(cfg.getCfgTableId()==currentCfgTableId){
					List<NodeMessage> msgs = isTx ? cfg.getTxMessages() : cfg.getRxMessages();
					if(msgs!=null){
						Iterator<NodeMessage> it = msgs.iterator();
						for( ; it.hasNext() ; ){
							NodeMessage msg = it.next();
							if(msg.getMessageID()==currentMessage.getMessageID()){
								it.remove();
							}
						}
					}
					break;
				}
			}
			dao.saveNodeDevice(node, null);
		}
		
		if(isTx){
			sendBox.removeElement(currentMessage);
		}else{
			receiveBox.removeElement(currentMessage);
		}
	}

	private JScrollPane initCfgTablePanel() {
		this.treebox=new TDataBox();
		this.tree=new TTree(this.treebox);
		this.tree.setRootVisible(false);
		this.tree.addTreeNodeClickedActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				ElementNode node=(ElementNode)obj;
				Element cfgTableElement=null;
				if(node!=null){
					cfgTableElement=node.getElement();
				}
				
				fillSendTable(currentNodeName, cfgTableElement);
				fillReceiveTable(currentNodeName, cfgTableElement);
		}});
		
		JScrollPane treePane=new JScrollPane(this.tree);
		return treePane;
	}
	
	@Override
	public void addOrUpdateConfigTable(boolean isAdd, int cfgTableId) {
		Node cfgNode=null;
		String twaverId = ConfigUtils.getCfgTableTwaverId(cfgTableId);
		if(isAdd){
			cfgNode=new Node(twaverId);
			if(treeRoot!=null){
				cfgNode.setParent(treeRoot);
			}
			cfgNode.setIcon(this.cfgIconURL);
		}else{
			cfgNode=(Node) treebox.getElementByID(twaverId);
		}
		
		cfgNode.setName("���ñ�"+cfgTableId);
		if(isAdd){
			treebox.addElement(cfgNode);
			tree.clearSelection();
			cfgNode.setSelected(true);
			tree.expandAll();
			
			this.receiveBox.clear();
			this.sendBox.clear();
		}
		
		NodeDevice nodeDev = dao.readNodeDeviceFromCache(currentNodeName);
		if(nodeDev!=null){
			List<NodeDeviceCfg> cfgs= nodeDev.getCfgs();
			if(isAdd){
				NodeDeviceCfg cfg=new NodeDeviceCfg(cfgTableId);
				cfgs.add(cfg);
				sortConfigTable(cfgs);
			}
			dao.saveNodeDevice(nodeDev, null);
		}
	}

	@Override
	public void deleteConfigTable(int cfgTableId) {
		treebox.removeElementByID(ConfigUtils.getCfgTableTwaverId(cfgTableId));
		
		NodeDevice nodeDev = dao.readNodeDeviceFromCache(currentNodeName);
		if(nodeDev!=null){
			List<NodeDeviceCfg> cfgs= nodeDev.getCfgs();
			Iterator<NodeDeviceCfg> it = cfgs.iterator();
			for(; it.hasNext(); ){
				NodeDeviceCfg cfg = it.next();
				if(cfgTableId==cfg.getCfgTableId()){
					it.remove();
					break;
				}
			}
			sortConfigTable(cfgs);
			dao.saveNodeDevice(nodeDev, null);
		}
	}
	
	private void fillTree(String nodeName){
		treebox.clear();
		
		if(nodeName==null){
			return;
		}
		
		treeRoot=new Node(nodeName);
		treeRoot.setName(nodeName);
		treeRoot.setIcon(this.rootIconURL);
		treebox.addElement(treeRoot);
		currentNodeName=nodeName;
		
		NodeDevice nodeDev = dao.readNodeDeviceFromCache(nodeName);
		if(nodeDev!=null){
			List<NodeDeviceCfg> cfgs = nodeDev.getCfgs();
			for(NodeDeviceCfg cfg : cfgs){
				String cfgTwaverId = ConfigUtils.getCfgTableTwaverId(cfg.getCfgTableId());
				Node cfgNode=new Node(cfgTwaverId);
				cfgNode.setName("���ñ�"+cfg.getCfgTableId());
				cfgNode.setIcon(this.cfgIconURL);
				cfgNode.setParent(treeRoot);
				treebox.addElement(cfgNode);
			}
		}

		tree.expandAll();
	}
	
	private void sortConfigTable(List<NodeDeviceCfg> cfgs) {
		Collections.sort(cfgs);
	}
	
}
