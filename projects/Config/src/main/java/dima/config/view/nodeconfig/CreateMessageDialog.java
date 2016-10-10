package dima.config.view.nodeconfig;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.NodeMessage;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.TWaverUtil;

public class CreateMessageDialog extends JDialog {

	private static final long serialVersionUID = -6162007779673465651L;
	
	private NodeMessage oldData;
	
	private JComboBox<String> nodeBoxNode;
	private JTextField msgIdTxtField;
	private JTextField msgNameTxtField;
	private JTextField maxLengthTxtField;
	
	private JTextField vlIdTxtField;
	private JComboBox<String> snmpIDBox;
	private JComboBox<String> loadIDBox;
	
	private JComboBox<String> useOfMessageBox;
	private JComboBox<String> typeBox;
	private JTextField sidTxtField;
	private JTextField didTxtField;
	
	/** 0 tx-send, 1 rx */
	private int sendRecieve = -1; 
	
	private MessageConfigPanel tablePanel;
	private String nodeName;

	public CreateMessageDialog(Window parent, String title, NodeMessage data,
			MessageConfigPanel tablePanel, int sendrecieve, String nodeName) {
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.oldData = data;
		this.tablePanel = tablePanel;
		this.sendRecieve = sendrecieve;
		this.nodeName=nodeName;
		
		initView();
	}

	private void initView() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(420, 268);
		setResizable(false);
		TWaverUtil.centerWindow(this);

		LayoutManager borderLayout = new BorderLayout(10, 10);
		JPanel contentPane = new JPanel(borderLayout);

		JPanel inputPane = createTopPanel();
		contentPane.add(inputPane, BorderLayout.CENTER);

		JPanel btnPanel = createButtonPanel();
		contentPane.add(btnPanel, BorderLayout.SOUTH);

		getContentPane().add(contentPane);
	}

	private JPanel createTopPanel() {
		LayoutManager layout = new TableLayout(new double[][] {
				{ 10, 80, 4, 100, TableLayout.FILL, 80, 4, 100, 10 },
				{ 10, 22, 10, 22, 10, 22, 10, 22,10, 22,10, 22 } });
		JPanel inputPane = new JPanel(layout);

		JLabel label1 = new JLabel("设备名称:");
		inputPane.add(label1, "1,1,f,0");

		nodeBoxNode = new JComboBox<>();
		//ConfigDAO dao = ServiceFactory.getService(ConfigDAO.class);
		//List<NodeDevice> nodes = dao.readAllNodeDevices(true);
		//for (NodeDevice node : nodes) {
		//	nodeBoxNode.addItem(node.getNodeName());
		//}
		nodeBoxNode.addItem(nodeName);
		if (oldData != null) {
			nodeBoxNode.setSelectedItem(oldData.getNodeName());
		}
		inputPane.add(nodeBoxNode, "3,1,f,0");
		nodeBoxNode.setEditable(false);
		
		JLabel label2 = new JLabel("消息ID:");
		inputPane.add(label2, "5,1,f,0");

		msgIdTxtField = ConfigUtils.getNumberTextField();
		msgIdTxtField.setText("1");
		if (oldData != null) {
			msgIdTxtField.setText(oldData.getMessageID() + "");
			msgIdTxtField.setEnabled(false);
		}
		inputPane.add(msgIdTxtField, "7,1,f,0");

		JLabel label3 = new JLabel("消息名称:");
		inputPane.add(label3, "1,3,f,0");

		msgNameTxtField = new JTextField();
		msgNameTxtField.setText("msg_001");
		if (oldData != null) {
			msgNameTxtField.setText(oldData.getMessageName());
		}
		inputPane.add(msgNameTxtField, "3,3,f,0");

		JLabel label4 = new JLabel("发送最大长度:");
		inputPane.add(label4, "5,3,f,0");

		maxLengthTxtField = ConfigUtils.getNumberTextField();
		maxLengthTxtField.setText("2048");
		if (oldData != null) {
			maxLengthTxtField.setText(oldData.getMaxOfLen() + "");
		}
		inputPane.add(maxLengthTxtField, "7,3,f,0");

		JLabel label5 = new JLabel("消息用途:");
		inputPane.add(label5, "1,5,f,0");

		useOfMessageBox = new JComboBox<>();
		useOfMessageBox.addItem("normal");
		useOfMessageBox.addItem("LOAD_File");
		useOfMessageBox.addItem("LOAD_File_ACK");
		useOfMessageBox.addItem("LOAD_Status");
		useOfMessageBox.addItem("LOAD_Status_ACK");
		useOfMessageBox.addItem("SNMP");
		useOfMessageBox.addItem("SNMP_TRAP");
		useOfMessageBox.addItem("SNMP_Inform");
		useOfMessageBox.addItem("SNMP_InfromACK");
		useOfMessageBox.addItem("RTC");
		if (oldData != null) {
			useOfMessageBox.setSelectedIndex(oldData.getUseOfMessage());
		}else{
			useOfMessageBox.setSelectedIndex(0);
		}
		inputPane.add(useOfMessageBox, "3,5,f,0");
		
		JLabel label51 = new JLabel("虚拟链路号:");
		inputPane.add(label51, "5,5,f,0");

		vlIdTxtField = ConfigUtils.getNumberTextField();
		vlIdTxtField.setText("0");
		if (oldData != null) {
			vlIdTxtField.setText(oldData.getVl()+"");
		}
		inputPane.add(vlIdTxtField, "7,5,f,0");

		JLabel label6 = new JLabel("SnmpID:");
		inputPane.add(label6, "1,7,f,0");

		snmpIDBox = new JComboBox<>();
		snmpIDBox.addItem("管理端");
		snmpIDBox.addItem("备份管理端");
		if (oldData != null) {
			snmpIDBox.setSelectedIndex(oldData.getSnmpID()-1);
		}else{
			snmpIDBox.setSelectedIndex(0);
		}
		inputPane.add(snmpIDBox, "3,7,f,0");

		JLabel label7 = new JLabel("LoadID:");
		inputPane.add(label7, "5,7,f,0");

		loadIDBox = new JComboBox<>();
		loadIDBox.addItem("加载器1");
		loadIDBox.addItem("加载器2");
		if (oldData != null) {
			loadIDBox.setSelectedIndex(oldData.getLoadID());
		}else{
			loadIDBox.setSelectedIndex(0);
		}
		inputPane.add(loadIDBox, "7,7,f,0");
		
		JLabel label8 = new JLabel("消息类型:");
		inputPane.add(label8, "1,9,f,0");

		this.typeBox = new JComboBox<>();
		typeBox.addItem("BE");
		typeBox.addItem("RC");
		typeBox.addItem("TT");
		if (oldData != null) {
			typeBox.setSelectedIndex(oldData.getType()-1);
		}else{
			typeBox.setSelectedIndex(0);
		}
		inputPane.add(typeBox, "3,9,f,0");

		JLabel label9 = new JLabel("S_ID:");
		inputPane.add(label9, "5,9,f,0");
		this.sidTxtField=ConfigUtils.getNumberTextField();
		inputPane.add(sidTxtField, "7,9,f,0");
		if (oldData != null) {
			sidTxtField.setText(oldData.getSID()+"");
		}else{
			sidTxtField.setText("0");
		}
		
		JLabel label10 = new JLabel("D_ID:");
		inputPane.add(label10, "1,11,f,0");
		this.didTxtField=ConfigUtils.getNumberTextField();
		inputPane.add(didTxtField, "3,11,f,0");
		if (oldData != null) {
			didTxtField.setText(oldData.getDID()+"");
		}else{
			didTxtField.setText("0");
		}
		
		return inputPane;
	}

	private JPanel createButtonPanel() {
		LayoutManager layout = new TableLayout(new double[][] {
				{ 80, 80, TableLayout.FILL, 80, 80 },
				{ 6, 22, TableLayout.FILL, 6 } });
		JPanel buttonPane = new JPanel(layout);
		final JButton okBtn = new JButton("确定");
		final JButton cancelBtn = new JButton("取消");

		buttonPane.add(okBtn, "1,1,f,0");
		buttonPane.add(cancelBtn, "3,1,f,0");

		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == okBtn) {
					NodeMessage newMessage = getData();
					if (newMessage == null) {
						return;
					}
					
					try{
						if (sendRecieve == 0) {
							tablePanel.addSendMessage(CreateMessageDialog.this, newMessage, oldData);
						} else {
							tablePanel.addRecieveMessage(CreateMessageDialog.this, newMessage, oldData);
						}
					}catch(Exception ex){
						JOptionPane.showMessageDialog(CreateMessageDialog.this, "添加消息错误:" + ex.getMessage());
						return ;
					}

					dispose();

				} else if (source == cancelBtn) {
					dispose();
				}
			}
		};

		okBtn.addActionListener(l);
		cancelBtn.addActionListener(l);

		return buttonPane;
	}

	public NodeMessage getData() {
		String nodeName = nodeBoxNode.getSelectedItem().toString();
		NodeMessage message = null;
		try {
			int msgId=Integer.valueOf(msgIdTxtField.getText().trim());
			message = new NodeMessage(nodeName, msgId);
			message.setVl(Integer.valueOf(vlIdTxtField.getText().trim()));
			
			if(message.getVl()<0 || message.getVl()>65535){
				JOptionPane.showMessageDialog(this, "VL必须在0~65535间!");
				return null;
			}
			
			message.setMessageName(msgNameTxtField.getText().trim());
			message.setMaxOfLen(Integer.valueOf(maxLengthTxtField.getText().trim()));
			message.setUseOfMessage((short)(useOfMessageBox.getSelectedIndex()+1));
			message.setSnmpID((short)(snmpIDBox.getSelectedIndex()+1));
			message.setLoadID((short)(loadIDBox.getSelectedIndex()));
			message.setType((short)(typeBox.getSelectedIndex()+1));
			message.setSID(Integer.valueOf(sidTxtField.getText().trim()));
			message.setDID(Integer.valueOf(didTxtField.getText().trim()));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "输入格式有误:" + ex.getMessage());
			return null;
		}
		return message;
	}
}
