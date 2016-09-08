package dima.config.view.netconfig;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;

public class NewNodeOrNMUPanel extends JPanel{

	private static final long serialVersionUID = 6979245345620069547L;

	private NewNodeDialog dlg;
	
	private boolean isForNode=false;
	private NodeDevice oldData;
	private String switchName;
	
	private JTextField nodeNameField;
	
	private String netSwitchName;
	private int netSwitchId;
	private int netPort;
	
	private JButton netPortBtn;
	
	private JComboBox<String> nmuRoleBox; 
	private JComboBox<String> networkLoadRoleBox;
	private JComboBox<String> timeSyncRoleBox;
	private JTextField rtcIntervalField;
	private JTextField locationField;
	
	private ConfigDAO dao;
	
	public NewNodeOrNMUPanel(NewNodeDialog dlg, boolean isForNode, NodeDevice nodeDevice, String switchName){
		this.dlg=dlg;
		this.isForNode=isForNode;
		this.oldData=nodeDevice;
		this.switchName=switchName;
		
		initUI();
	}
	
	private void initUI(){
		dao=ServiceFactory.getService(ConfigDAO.class);
		
		int rowNum= isForNode ? 7:5;
		String title=isForNode ? "�ڵ�����":"�������Ԫ����";
		String name=isForNode ? "�ڵ�����":"�������������Ԫ����";
		
		double[] hs = ConfigUtils.getTableLayoutRowParam(rowNum, 4);
		if(isForNode){
			hs[3]=28;
		}
		LayoutManager layout =  new TableLayout(new double[][] {
            { 10,  140, TableLayout.FILL, 240, 10 },  hs});
	    setLayout(layout);
	    
	    setBorder(BorderFactory.createTitledBorder(title)); 

	    JLabel label1=new JLabel(name);
		add(label1,"1,1,f,0");
		nodeNameField=new JTextField();
		add(nodeNameField,"3,1,f,0");
		if(!isForNode){
			nodeNameField.setToolTipText("����Ϊ�գ�16�����ڵ�Ӣ���ַ�������SW-21");
			nodeNameField.setText(oldData!=null ? oldData.getNodeName() : switchName);
			nodeNameField.setEnabled(false);
		}else{
			nodeNameField.setToolTipText("����Ϊ�գ�16�����ڵ�Ӣ���ַ�������NP-21");
		}
		
		int nextRow=3;
		if(isForNode){
			JLabel label11=new JLabel("���ӽ������˿�");
			add(label11,"1,3,f,0");
			
			JPanel panel=new JPanel(new FlowLayout());
			
			JLabel labelANet=new JLabel("��������˿ں�:");
			panel.add(labelANet);
			netPortBtn=new JButton();
			netPortBtn.setText("0x0");
			panel.add(netPortBtn);
			netPortBtn.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int portNo=0;
					if(netPortBtn.getText()!=null  && netPortBtn.getText().length()>2){
						portNo=Integer.valueOf(netPortBtn.getText().substring(2), 16);
					}
					ChooseSwitchPortDialog dialog=new ChooseSwitchPortDialog(dlg,
							"ѡ�����ӽ������˿�", true, portNo, NewNodeOrNMUPanel.this);
					dialog.setVisible(true);
				}
			});
			
			add(panel,"3,3,f,0");
			nextRow+=2;
			
			JLabel label6=new JLabel("�ڵ�λ��ID");
			add(label6,"1,"+nextRow+",f,0");
			this.locationField=ConfigUtils.getNumberTextField();
			add(locationField,"3,"+nextRow+",f,0");
			nextRow+=2;
		}
		
		JLabel label2=new JLabel("��������ɫ");
		add(label2,"1,"+nextRow+",f,0");
		this.nmuRoleBox=new JComboBox<>();
		nmuRoleBox.addItem("�����");
		nmuRoleBox.addItem("�����");
		nmuRoleBox.addItem("���ݹ����");
		add(nmuRoleBox,"3,"+nextRow+",f,0");
		nextRow+=2;
		
		JLabel label3=new JLabel("������ؽ�ɫ");
		add(label3,"1,"+nextRow+",f,0");
		this.networkLoadRoleBox=new JComboBox<>();
		networkLoadRoleBox.addItem("������");
		networkLoadRoleBox.addItem("Ŀ���");
		add(networkLoadRoleBox,"3,"+nextRow+",f,0");
		nextRow+=2;
		
		JLabel label4=new JLabel("ʱ��ͬ����ɫ");
		add(label4,"1,"+nextRow+",f,0");
		this.timeSyncRoleBox=new JComboBox<>();
		timeSyncRoleBox.addItem("�ͻ���");
		timeSyncRoleBox.addItem("������");
		timeSyncRoleBox.addItem("���ݷ�����");
		add(timeSyncRoleBox,"3,"+nextRow+",f,0");
		nextRow+=2;
		
		JLabel label5=new JLabel("RTC��������������");
		add(label5,"1,"+nextRow+",f,0");
		this.rtcIntervalField=ConfigUtils.getNumberTextField(5);
		add(rtcIntervalField,"3,"+nextRow+",f,0");
		nextRow+=2;
		
		if(oldData!=null){
			nodeNameField.setText(oldData.getNodeName());
			
			if(netPortBtn!=null){
				netPortBtn.setText("0x"+Integer.toHexString(oldData.getPortNo()));
				if(oldData.getPortNo()==0){
					netPortBtn.setToolTipText("δ���ý������˿�");
				}else{
					int p=oldData.getPortNo() & 0xffff;
					int swId=(oldData.getPortNo()>>16) & 0xffff;
					
					List<SwitchDevice> sws = dao.readAllSwitchDevices(true);
					for(SwitchDevice sw : sws){
						if(sw.getLocalDomainID()==swId){
							netPortBtn.setToolTipText(sw.getSwitchName()+"("+swId+"):"+p);
							break;
						}
					}
				}
			}

			
			nmuRoleBox.setSelectedIndex(oldData.getRoleOfNM());
			networkLoadRoleBox.setSelectedIndex(oldData.getRoleOfNetworkLoad());
			timeSyncRoleBox.setSelectedIndex(oldData.getRoleOfTimeSync());
			rtcIntervalField.setText(oldData.getRtcSendInterval()+"");
			if(locationField!=null){
				locationField.setText(oldData.getLocationId()+"");
			}
		}else{
			if(isForNode){
				nodeNameField.setText("NP-");
			}
			rtcIntervalField.setText("1000");
			if(netPortBtn!=null){
				netPortBtn.setText("0x0");
				netPortBtn.setToolTipText("δ���ý������˿�");
			}
			
			if(locationField!=null){
				locationField.setText("0");
			}
		}
	}
	
	public NodeDevice getData(){
		if(nodeNameField.getText().trim().length()==0){
			JOptionPane.showMessageDialog(this, "���Ʋ���Ϊ��!");
			return null;
		}
		int type=isForNode ? ConfigUtils.TYPE_NODE: ConfigUtils.TYPE_NMU;
		
		NodeDevice node=new NodeDevice(nodeNameField.getText(), type);
		if(oldData!=null){
			node.setCfgs(oldData.getCfgs());
		}
		
		try{
			if(isForNode){
				if(netPortBtn.getText()!=null && netPortBtn.getText().length()>2){
					int p=Integer.valueOf(netPortBtn.getText().substring(2), 16);
					node.setPortNo(p);
				}
				
				if(node.getPortNo()==0){
					JOptionPane.showMessageDialog(this, "���ӽ������˿ڲ���Ϊ��!");
					return null;
				}
			}
			node.setRoleOfNetworkLoad(networkLoadRoleBox.getSelectedIndex());
			node.setRoleOfNM(nmuRoleBox.getSelectedIndex());
			node.setRoleOfTimeSync(timeSyncRoleBox.getSelectedIndex());
			node.setRtcSendInterval(Integer.valueOf(rtcIntervalField.getText()));
			if(locationField!=null && locationField.getText().trim().length()>0){
				node.setLocationId(Integer.valueOf(locationField.getText().trim()));
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "�������ݸ�ʽ����ȷ:"+ex.getMessage());
			return null;
		}
		return node;
	}
	
	public void updateNetPortButton(boolean aNet, String switchName, int switchId,int portNo){
		if(aNet){
			this.netSwitchName=switchName;
			this.netSwitchId=switchId;
			this.netPort=portNo;
			if(this.netSwitchName==null){
				this.netPortBtn.setText("0x0");
				this.netPortBtn.setToolTipText("δ���ý������˿�");
			}else{
				this.netPortBtn.setText(ConfigUtils.getPortName(this.netSwitchId, this.netPort));
				this.netPortBtn.setToolTipText(this.netSwitchName+"("+this.netSwitchId+"):"+this.netPort);
			}
		}
	}
	
}
