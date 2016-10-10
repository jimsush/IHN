package dima.config.view.nodeconfig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.swing.UIManager;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.EnumTableCellRenderer;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.NodeDeviceCfg;
import dima.config.common.models.NodeVL;
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

public class NodeVLConfigPanel extends JPanel implements ConfigTableCallback{

	private static final long serialVersionUID = -4144527399920723319L;

	private JPanel topPanel;
	private JComboBox<String> nodeBox;
	
	private TDataBox txBox;
	private TElementTable txTable;
	
	private TDataBox rxBox;
	private TElementTable rxTable;
	
	private ConfigDAO dao;
	
	private TDataBox treebox;
	private TTree tree;
	private Node treeRoot;
	
	private String currentNodeName;
	private String rootIconURL;
	private String cfgIconURL;
	
	private NodeVL copiedRxVL;
	private NodeVL copiedTxVL;
	
	public NodeVLConfigPanel(){
		initUI();
	}
	
	private void initUI(){
		this.setLayout(new BorderLayout());
		
		initService();
		
		initTopPanel();
		
		//JScrollPane treePane = initCfgTablePanel();
		
		JPanel rightPane=new JPanel(new BorderLayout());
		JPanel txPane=initTxVLPanel();
		rightPane.add(txPane, BorderLayout.CENTER);
		JPanel rxPane=initRxVLPanel();
		rightPane.add(rxPane, BorderLayout.SOUTH);
		
		//JSplitPane bottomPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, rightPane);
		//bottomPane.setDividerLocation(140);
		//this.add(bottomPane, BorderLayout.CENTER);
		this.add(rightPane, BorderLayout.CENTER);
		
		if(nodeBox.getSelectedItem()!=null){
			String nodeName=nodeBox.getSelectedItem().toString();
			this.currentNodeName=nodeName;
			
			//fillTree(nodeName);
			
			// select the first cfg table
			//List<?> children = treeRoot.getChildren();
			//if(children!=null && children.size()>0){
			//	Object child=children.get(0);
			//	Element cfgTableElement=(Element)child;
				
			//	cfgTableElement.setSelected(true);
				fillTxTable(nodeName);
				fillRxTable(nodeName);
			//}
		}
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	private JScrollPane initCfgTablePanel() {
		this.treebox=new TDataBox();
		this.tree=new TTree(this.treebox, new Comparator<Element>(){
			@Override
			public int compare(Element o1, Element o2) {
				if(o2==null)
					return 1;
				if(o1==null)
					return -1;
				return o1.getID().toString().compareTo(o2.getID().toString());
		}});
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
				
				//fillTxTable(currentNodeName, cfgTableElement);
				//fillRxTable(currentNodeName, cfgTableElement);
		}});
		
		JScrollPane treePane=new JScrollPane(this.tree);
		return treePane;
	}

	private void initService(){
		dao=ServiceFactory.getService(ConfigDAO.class);
		rootIconURL=ConfigUtils.getImageURLString("root.png");
		cfgIconURL=ConfigUtils.getImageURLString("config.png");
	}
	
	private void initTopPanel(){
		topPanel=new JPanel(new BorderLayout());
		
		JPanel leftPanel=new JPanel(new FlowLayout());
		leftPanel.setBorder(BorderFactory.createTitledBorder("��ʾ��Χ")); 
		
		JLabel label1=new JLabel("ѡ���ն�:");
		leftPanel.add(label1);
		
		nodeBox=new JComboBox<>();
		
		List<String> allNodes = getAllNodeNames();
		for(String nodeName : allNodes){
			nodeBox.addItem(nodeName);
		}
		
		nodeBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj=nodeBox.getSelectedItem();
				if(obj==null){
					return;
				}

				String nodeName=obj.toString();
				currentNodeName=nodeName;
				
				//fillTree(nodeName);
				
				//Element ele = treebox.getLastSelectedElement();
				
				//fillTxTable(nodeName, ele);
				//fillRxTable(nodeName, ele);
				fillTxTable(nodeName);
				fillRxTable(nodeName);
			}
		});
		
		leftPanel.add(nodeBox);
		
		JLabel label3=new JLabel("����ʽ:");
		leftPanel.add(label3);
		
		JComboBox<String> sortingBox=new JComboBox<>();
		sortingBox.addItem("������·��");
		leftPanel.add(sortingBox);
		
		JButton btnAddCfgTable=new JButton("�������ñ�");
		//leftPanel.add(btnAddCfgTable);
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
						"�������ñ�", true, 0, NodeVLConfigPanel.this);
				dialog.setVisible(true);
			}
		});
		
		JButton btnDeleteCfgTable=new JButton("ɾ�����ñ�");
		//leftPanel.add(btnDeleteCfgTable);
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
				
				txBox.clear();
				rxBox.clear();
			}
		});
		
		JLabel label4=new JLabel("      ");
		leftPanel.add(label4);
		
		topPanel.add(leftPanel, BorderLayout.WEST);

		this.add(topPanel, BorderLayout.NORTH);
	}
	
	private JPanel initTxVLPanel(){
		txBox=new TDataBox();
		txTable=tableInit(txBox, true);

		JPanel mainPanel=new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createTitledBorder("����VL:")); 
		
		JPanel buttonPanel=new JPanel(new FlowLayout());
		
		JButton btn1=new JButton("���Ӽ�¼");
		btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object obj=nodeBox.getSelectedItem();
				if(obj==null){
					return;
				}
				//Element treeElement=treebox.getLastSelectedElement();
				//int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				//if(cfgTableId<=0){
				//	return;
				//}
				
				CreateNodeVLDialog dlg=new CreateNodeVLDialog(ConfigContext.mainFrame, 
						"���ӷ���VL", obj.toString(), null, NodeVLConfigPanel.this, true);
				dlg.setVisible(true);
			}
		});
		buttonPanel.add(btn1);
		
		JButton btn2=new JButton("�޸ļ�¼");
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = txBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "��ѡ��һ����¼");
					return;
				}

				NodeVL oldVL=(NodeVL)element;
				CreateNodeVLDialog dlg=new CreateNodeVLDialog(ConfigContext.mainFrame, 
						"�޸ķ���VL", oldVL.getNodeName(), oldVL, NodeVLConfigPanel.this, true);
				dlg.setVisible(true);
				
			}
		});
		buttonPanel.add(btn2);
		
		JButton btn3=new JButton("ɾ����¼");
		btn3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = txBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "û��ѡ���κμ�¼");
					return;
				}
				
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "ȷ��Ҫɾ����¼��?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.OK_OPTION){
					deleteVL((NodeVL)element, true);
				}
			}
		});
		buttonPanel.add(btn3);

		JButton copyBtn=new JButton("���Ƽ�¼");
		buttonPanel.add(copyBtn);
		copyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = txBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "û��ѡ���κμ�¼");
					return;
				}
				NodeVL currentVL=(NodeVL)element;
				copiedTxVL=cloneNodeVL(currentVL, currentVL.getVLID());
			}
		});

		JButton pasteBtn=new JButton("ճ����¼");
		buttonPanel.add(pasteBtn);
		pasteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(copiedTxVL==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "���ȸ���һ������VL��¼");
					return;
				}
				
				//Element treeElement=treebox.getLastSelectedElement();
				//int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				//if(cfgTableId>0){
					int nextVLID=getNextVLID(txBox);
					NodeVL newVL=cloneNodeVL(copiedTxVL, nextVLID);
					
					addOrUpdateTxNodeVL(newVL, null);
				//}
			}
		});
		
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		
		JScrollPane tablePanel=new JScrollPane(txTable);

		mainPanel.add(tablePanel, BorderLayout.CENTER);
		
		return mainPanel;
	}

	private NodeVL cloneNodeVL(NodeVL vl, int vlid) {
		NodeVL newVL=new NodeVL(vl.getNodeName(), vlid);
		newVL.setType(vl.getType());
		newVL.setBag(vl.getBag());
		newVL.setJitter(vl.getJitter());
		
		newVL.setTtInterval(vl.getTtInterval());
		newVL.setTtSentInterval(vl.getTtSentInterval());
		newVL.setTtWindowStart(vl.getTtWindowStart());
		newVL.setTtWindowEnd(vl.getTtWindowEnd());
		newVL.setTtWindowOffset(vl.getTtWindowOffset());
		
		newVL.setNetworkType(vl.getNetworkType());
		newVL.setCompleteCheck(vl.getCompleteCheck());
		newVL.setRedudanceType(vl.getRedudanceType());
		newVL.setUseOfLink(vl.getUseOfLink());
		newVL.setRtcInterval(vl.getRtcInterval());
		return newVL;
	}
	
	private int getNextVLID(TDataBox tableBox) {
		List<?> elements = tableBox.getAllElements();
		if(elements==null || elements.size()==0){
			return 0;
		}
		int maxId=0;
		for(Object obj : elements){
			NodeVL vl=(NodeVL)obj;
			if(vl.getVLID()>=maxId){
				maxId=vl.getVLID();
			}
		}
		return maxId+1;
	}

	private JPanel initRxVLPanel(){
		rxBox=new TDataBox();
		rxTable=tableInit(rxBox, false);

		JPanel mainPanel=new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createTitledBorder("����VL:")); 
		
		JPanel buttonPanel=new JPanel(new FlowLayout());
		
		JButton btn1=new JButton("���Ӽ�¼");
		btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object obj=nodeBox.getSelectedItem();
				if(obj==null){
					return;
				}
				//Element treeElement=treebox.getLastSelectedElement();
				//int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				//if(cfgTableId<=0){
				//	return;
				//}
				
				CreateNodeVLDialog dlg=new CreateNodeVLDialog(ConfigContext.mainFrame, 
						"���ӽ���VL", obj.toString(), null, NodeVLConfigPanel.this, false);
				dlg.setVisible(true);
			}
		});
		buttonPanel.add(btn1);
		
		JButton btn2=new JButton("�޸ļ�¼");
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = rxBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "��ѡ��һ����¼");
					return;
				}
				NodeVL oldVL=(NodeVL)element;
				CreateNodeVLDialog dlg=new CreateNodeVLDialog(ConfigContext.mainFrame, 
						"�޸Ľ���VL", oldVL.getNodeName(), oldVL, NodeVLConfigPanel.this, false);
				dlg.setVisible(true);
			}
		});
		buttonPanel.add(btn2);
		
		JButton btn3=new JButton("ɾ����¼");
		btn3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = rxBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "û��ѡ���κμ�¼");
					return;
				}
				
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "ȷ��Ҫɾ����¼��?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.OK_OPTION){
					deleteVL((NodeVL)element, false);
				}
			}
		});
		buttonPanel.add(btn3);

		JButton copyBtn=new JButton("���Ƽ�¼");
		buttonPanel.add(copyBtn);
		copyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = rxBox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "û��ѡ���κμ�¼");
					return;
				}
				NodeVL currentVL=(NodeVL)element;
				copiedRxVL=cloneNodeVL(currentVL, currentVL.getVLID());
			}
		});

		JButton pasteBtn=new JButton("ճ����¼");
		buttonPanel.add(pasteBtn);
		pasteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(copiedRxVL==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "���ȸ���һ������VL��¼");
					return;
				}
				
				//Element treeElement=treebox.getLastSelectedElement();
				//int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				//if(cfgTableId>0){
					int nextVLID=getNextVLID(rxBox);
					NodeVL newVL=cloneNodeVL(copiedRxVL, nextVLID);
					
					addOrUpdateRxNodeVL(newVL, null);
				//}
			}
		});
		
		mainPanel.add(buttonPanel, BorderLayout.NORTH);

		JScrollPane tablePanel=new JScrollPane(rxTable);
		
		mainPanel.setPreferredSize(new Dimension(500, 360));
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		
		return mainPanel;
	}
	
	private TElementTable tableInit(TDataBox box, boolean isTx){
		TElementTable table = new TElementTable(box);
		
		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);
		
		table.setElementClass(NodeVL.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("nodeName");
		attribute.setDisplayName("�豸����");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("VLID");
		attribute.setDisplayName("VL_ID");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("type");
		attribute.setDisplayName("����");
		attributes.add(attribute);
		
		if(isTx){
			attribute = new ElementAttribute();
			attribute.setName("bag");
			attribute.setDisplayName("BAG");
			attributes.add(attribute);
			attribute = new ElementAttribute();
			attribute.setName("jitter");
			attribute.setDisplayName("Jitter");
			attributes.add(attribute);
			
			attribute = new ElementAttribute();
			attribute.setName("ttInterval");
			attribute.setDisplayName("TT��ʼ����");
			attributes.add(attribute);
			attribute = new ElementAttribute();
			attribute.setName("ttSentInterval");
			attribute.setDisplayName("TT��������");
			attributes.add(attribute);
			
			attribute = new ElementAttribute();
			attribute.setName("ttWindowOffset");
			attribute.setDisplayName("TT����ƫ��");
			attributes.add(attribute);
			attribute = new ElementAttribute();
			attribute.setName("ttWindowStart");
			attribute.setDisplayName("TT������ʼʱ��");
			attributes.add(attribute);
			attribute = new ElementAttribute();
			attribute.setName("ttWindowEnd");
			attribute.setDisplayName("TT���ڽ���ʱ��");
			attributes.add(attribute);
			
			
			attribute = new ElementAttribute();
			attribute.setName("networkType");
			attribute.setDisplayName("����ѡ��");
			attributes.add(attribute);
		}
		
		attribute = new ElementAttribute();
		attribute.setName("redudanceType");
		attribute.setDisplayName("�������");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("useOfLink");
		attribute.setDisplayName("��·��;");
		attributes.add(attribute);
		
		if(isTx){
			attribute = new ElementAttribute();
			attribute.setName("rtcInterval");
			attribute.setDisplayName("RTC��������");
			attributes.add(attribute);
		}else{
			attribute = new ElementAttribute();
			attribute.setName("completeCheck");
			attribute.setDisplayName("�����Լ��");
			attributes.add(attribute);
		}
		
		table.registerElementClassAttributes(NodeVL.class, attributes);
		
		table.getTableModel().sortColumn("VLID", TTableModel.SORT_ASCENDING);
		
		Map<Object, String> m1=new HashMap<Object, String>();
		m1.put((short)1, "BE");
		m1.put((short)2, "RC");
		m1.put((short)3, "TT");
		EnumTableCellRenderer r1=new EnumTableCellRenderer(m1);
		table.getColumnByName("type").setCellRenderer(r1);
		
		Map<Object, String> enableMap=new HashMap<Object, String>();
		enableMap.put((short)0, "NO");
		enableMap.put((short)1, "YES");
		
		if(isTx){
			Map<Object, String> m2=new HashMap<Object, String>();
			m2.put((short)1, "����A");
			m2.put((short)2, "����B");
			m2.put((short)3, "BOTH");
			EnumTableCellRenderer r2=new EnumTableCellRenderer(m2);
			table.getColumnByName("networkType").setCellRenderer(r2);
		}else{
			EnumTableCellRenderer r2=new EnumTableCellRenderer(enableMap);
			table.getColumnByName("completeCheck").setCellRenderer(r2);
		}
		
		EnumTableCellRenderer r3=new EnumTableCellRenderer(enableMap);
		table.getColumnByName("redudanceType").setCellRenderer(r3);
		
		Map<Object, String> m4=new HashMap<Object, String>();
		m4.put((short)0, "����ͨ��");
		m4.put((short)1, "RTC");
		m4.put((short)2, "PCF");
		EnumTableCellRenderer r4=new EnumTableCellRenderer(m4);
		table.getColumnByName("useOfLink").setCellRenderer(r4);
		
		table.addElementDoubleClickedActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				NodeVL oldVL=((NodeVL)e.getSource());
				
				String title=(isTx ? "�޸ķ���VL" : "�޸Ľ���VL");
				CreateNodeVLDialog dlg=new CreateNodeVLDialog(ConfigContext.mainFrame, 
						title, oldVL.getNodeName(), oldVL, NodeVLConfigPanel.this, isTx);
				dlg.setVisible(true);
			}
		});
		
		return table;
	}
	
	private List<String> getAllNodeNames(){
		List<String> list=new ArrayList<String>();
		List<NodeDevice> nodes = dao.readAllNodeDevices(true);
		for(NodeDevice node : nodes){
			list.add(node.getNodeName());
		}
		return list;
	}
	
	private void fillTxTable(String nodeName){//, Element cfgTableElement){
		txBox.clear();
		
		//int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		//if(currentCfgTableId<=0){
		//	return;
		//}
		
		NodeDevice nodeDevice = dao.readNodeDeviceFromCache(nodeName);
		if(nodeDevice!=null){
			//List<NodeDeviceCfg> cfgs = nodeDevice.getCfgs();
			//for(NodeDeviceCfg cfg : cfgs){
			//	if(cfg.getCfgTableId()==currentCfgTableId){
			//		List<NodeVL> vls = cfg.getTxVLs();
					List<NodeVL> vls = nodeDevice.getTxVls();
					if(vls!=null){
						for(NodeVL vl : vls){
							txBox.addElement(vl);
						}
					}
			//		break;
			//	}
			//}
		}
	}
	
	private void fillRxTable(String nodeName){//, Element cfgTableElement){
		rxBox.clear();
		
		//int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		//if(currentCfgTableId<=0){
		//	return;
		//}
		
		NodeDevice nodeDevice = dao.readNodeDeviceFromCache(nodeName);
		if(nodeDevice!=null){
			//List<NodeDeviceCfg> cfgs = nodeDevice.getCfgs();
			//for(NodeDeviceCfg cfg : cfgs){
			//	if(cfg.getCfgTableId()==currentCfgTableId){
			//		List<NodeVL> vls = cfg.getRxVLs();
					List<NodeVL> vls = nodeDevice.getRxVls();
					if(vls!=null){
						for(NodeVL vl : vls){
							rxBox.addElement(vl);
						}
					}
			//		break;
			//	}
			//}
		}
	}

	public void addOrUpdateTxNodeVL(NodeVL newNodeVL, NodeVL oldVL){
		//Element cfgTableElement = treebox.getLastSelectedElement();
		//int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		//if(currentCfgTableId<=0){
		//	return;
		//}
		
		NodeDevice node = dao.readNodeDeviceFromCache(currentNodeName);
		//List<NodeDeviceCfg> cfgs = node.getCfgs();
		if(oldVL==null){
			txBox.addElement(newNodeVL);

			//for(NodeDeviceCfg cfg : cfgs){
			//	if(cfg.getCfgTableId()==currentCfgTableId){
			//		List<NodeVL> vls = cfg.getTxVLs();
			//		if(vls==null){
			//			vls =new ArrayList<NodeVL>();
			//			cfg.setTxVLs(vls);
			//		}
					List<NodeVL> vls = node.getTxVls();
					vls.add(newNodeVL);
			//		break;
			//	}
			//}
			dao.saveNodeDevice(node, null);
		}else{
			txBox.removeElement(oldVL);

			//for(NodeDeviceCfg cfg : cfgs){
			//	if(cfg.getCfgTableId()==currentCfgTableId){
			//		List<NodeVL> vls = cfg.getTxVLs();
			//		if(vls==null){
			//			vls =new ArrayList<NodeVL>();
			//			cfg.setTxVLs(vls);
			//			vls.add(newNodeVL);
			//		}else{
						List<NodeVL> vls = node.getTxVls();
						for(NodeVL vl : vls){
							if(oldVL.getVLID()==vl.getVLID()){ //matched than update
								vl.setBag(newNodeVL.getBag());
								vl.setJitter(newNodeVL.getJitter());
								vl.setType(newNodeVL.getType());  //1-BE��2-RC��3-TT
								
								vl.setTtInterval(newNodeVL.getTtInterval());
								vl.setTtSentInterval(newNodeVL.getTtSentInterval());
								
								vl.setTtWindowStart(newNodeVL.getTtWindowStart());
								vl.setTtWindowOffset(newNodeVL.getTtWindowOffset());
								vl.setTtWindowEnd(newNodeVL.getTtWindowEnd());
								
								vl.setNetworkType(newNodeVL.getNetworkType());
								vl.setCompleteCheck(newNodeVL.getCompleteCheck());
								vl.setRedudanceType(newNodeVL.getRedudanceType());
								vl.setUseOfLink(newNodeVL.getUseOfLink());
								vl.setRtcInterval(newNodeVL.getRtcInterval());
								vl.setSwitchPortNo(newNodeVL.getSwitchPortNo());
								break;
							}
						}
					//}
			//		break;
			//	}
			//}
			
			dao.saveNodeDevice(node, null);
			txBox.addElement(newNodeVL);
		}
	}
	
	public void addOrUpdateRxNodeVL(NodeVL newNodeVL, NodeVL oldVL){
		//Element cfgTableElement = treebox.getLastSelectedElement();
		//int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		//if(currentCfgTableId<=0){
		//	return;
		//}
		
		NodeDevice node = dao.readNodeDeviceFromCache(currentNodeName);
		List<NodeVL> vls = node.getRxVls();
		//List<NodeDeviceCfg> cfgs = node.getCfgs();
		if(oldVL==null){
			rxBox.addElement(newNodeVL);
	
			//for(NodeDeviceCfg cfg : cfgs){
			//	if(cfg.getCfgTableId()==currentCfgTableId){
			//		List<NodeVL> vls = cfg.getRxVLs();
			//		if(vls==null){
			//			vls =new ArrayList<NodeVL>();
			//			cfg.setRxVLs(vls);
			//		}
					vls.add(newNodeVL);
					//this.sortConfigTable(vls);
			//		break;
			//	}
			//}
			
			dao.saveNodeDevice(node, null);
		}else{
			rxBox.removeElement(oldVL);
			
			//for(NodeDeviceCfg cfg : cfgs){
			//	if(cfg.getCfgTableId()==currentCfgTableId){
			//		List<NodeVL> vls = cfg.getRxVLs();
			//		if(vls==null){
			//			vls =new ArrayList<NodeVL>();
			//			cfg.setRxVLs(vls);
			//			vls.add(newNodeVL);
			//		}else{
						for(NodeVL vl : vls){
							if(oldVL.getVLID()==vl.getVLID()){
								vl.setType(newNodeVL.getType());  //1-BE��2-RC��3-TT
								vl.setCompleteCheck(newNodeVL.getCompleteCheck());
								vl.setRedudanceType(newNodeVL.getRedudanceType());
								vl.setUseOfLink(newNodeVL.getUseOfLink());
								vl.setSwitchPortNo(newNodeVL.getSwitchPortNo());
								break;
							}
						}
					//}
					//break;
				//}
			//}
			
			dao.saveNodeDevice(node, null);
			
			rxBox.addElement(newNodeVL);
		}
	}
	
	public void deleteVL(NodeVL currentVL, boolean isTx){
		//Element cfgTableElement = treebox.getLastSelectedElement();
		//int currentCfgTableId = ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		//if(currentCfgTableId<=0){
		//	return;
		//}
		
		NodeDevice node = dao.readNodeDeviceFromCache(currentNodeName);
		if(node!=null){
			//List<NodeDeviceCfg> cfgs = node.getCfgs();
			List<NodeVL> vls = isTx ? node.getTxVls() : node.getRxVls();
			//for(NodeDeviceCfg cfg : cfgs){
			//	if(cfg.getCfgTableId()==currentCfgTableId){
			//		List<NodeVL> vls = isTx ? cfg.getTxVLs() : cfg.getRxVLs();
					if(vls!=null){
						Iterator<NodeVL> it = vls.iterator();
						for( ; it.hasNext() ; ){
							NodeVL vl = it.next();
							if(vl.getVLID()==currentVL.getVLID()){
								it.remove();
								break;
							}
						}
					}
				//	break;
				//}
			//}
			dao.saveNodeDevice(node, null);
		}
		
		if(isTx){
			txBox.removeElement(currentVL);
		}else{
			rxBox.removeElement(currentVL);
		}
	}

	/**
	 * @deprecated
	 * @param nodeName
	 */
	private void fillTree(String nodeName){
		/*
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
		*/
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public void addOrUpdateConfigTable(boolean isAdd, int cfgTableId) {
		/*
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
			
			this.rxBox.clear();
			this.txBox.clear();
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
		*/
	}

	/**
	 * @deprecated
	 */
	@Override
	public void deleteConfigTable(int cfgTableId) {
		/*
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
		*/
	}
	
	private void sortConfigTable(List<NodeDeviceCfg> cfgs) {
		Collections.sort(cfgs);
	}

}
