package dima.config.view.netconfig;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;

public class NewSwitchPanel extends JPanel{

	private static final long serialVersionUID = 6619029545758176422L;

	private SwitchDevice oldData;
	
	private JTextField switchNameField;
	private JTextField locationField;
	
	private JTextField switchLocalIDField;
	
	private JTextField switchPortNumberField;
	
	private JTextField eportNumField;
	private JTextField eportListField;
	private JComboBox<String> timeSyncEnableBox; //0-��ʹ�ܣ�1-ʹ�� 
	
	private JLabel timeSyncVL1Label;
	private JTextField timeSyncVL1Field; 
	
	private JLabel timeSyncVL2Label;
	private JTextField timeSyncVL2Field; 
	
	private JLabel pcfVLLabel;
	private JTextField pcfVLField;
	
	private JComboBox<String> timeSyncComboBox; // 0 SM 1 RC
	
	private JTextField overallIntervalField;
	private JTextField clusterIntervalField;

	private ConfigDAO dao;
	
	public NewSwitchPanel(SwitchDevice switchDevice){
		this.oldData=switchDevice;
		initService();
		initUI();
	}
	
	private void initService() {
		dao=ServiceFactory.getService(ConfigDAO.class);
	}

	private void initUI(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 10,  180, TableLayout.FILL, 120, 10 }, 
            ConfigUtils.getTableLayoutRowParam(13, 4) });  
		JPanel mainPane=new JPanel(layout);
		mainPane.setBorder(BorderFactory.createTitledBorder("����������")); 

		JLabel label1=new JLabel("����������");
		mainPane.add(label1,"1,1,f,0");
		switchNameField=new JTextField();
		switchNameField.setToolTipText("16�����ڵ�Ӣ���ַ�,����SW-02");
		mainPane.add(switchNameField,"3,1,f,0");
	    
		JLabel label11=new JLabel("������λ��ʶ���");
		mainPane.add(label11,"1,3,f,0");
		locationField=ConfigUtils.getNumberTextField();
		mainPane.add(locationField,"3,3,f,0");
		
		JLabel label3=new JLabel("������������ID");
		mainPane.add(label3,"1,5,f,0");
		switchLocalIDField=ConfigUtils.getNumberTextField();
		switchLocalIDField.setToolTipText("1~239(0xEF)");
		mainPane.add(switchLocalIDField,"3,5,f,0");
		
		JLabel label2=new JLabel("�������˿�����");
		mainPane.add(label2,"1,7,f,0");
		switchPortNumberField=ConfigUtils.getNumberTextField(2);
		mainPane.add(switchPortNumberField,"3,7,f,0");

		JLabel label4=new JLabel("������E_Port��");
		mainPane.add(label4,"1,9,f,0");
		eportNumField=ConfigUtils.getNumberTextField(1);
		eportNumField.setToolTipText("0~6");
		mainPane.add(eportNumField,"3,9,f,0");
	    
		JLabel label5=new JLabel("������E_Port����б�");
		label5.setToolTipText("16���Ʊ�ʾ, ��2�ֽ�Ϊ������ID����2�ֽ�Ϊ�˿ںţ����ŷָ�����˿�");
		mainPane.add(label5,"1,11,f,0");
		eportListField=new JTextField();
		eportListField.setToolTipText("16���Ʊ�ʾ, ��2�ֽ�Ϊ������ID����2�ֽ�Ϊ�˿ںţ�����:20002,20002,20005");
		mainPane.add(eportListField,"3,11,f,0");
		
		JLabel label6=new JLabel("ʱ��ͬ��VL�Ƿ�ʹ��");
		mainPane.add(label6,"1,13,f,0");
		timeSyncEnableBox=new JComboBox<>();
		timeSyncEnableBox.addItem("��ʹ��"); //0
		timeSyncEnableBox.addItem("ʹ��");   //1
		timeSyncEnableBox.setSelectedIndex(1);
		mainPane.add(timeSyncEnableBox,"3,13,f,0");
		timeSyncEnableBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				switchTimeSyncSelection();
			}
		});
		
		timeSyncVL1Label=new JLabel("ʱ��ͬ��VL1");
		mainPane.add(timeSyncVL1Label,"1,15,f,0");
		timeSyncVL1Field=ConfigUtils.getNumberTextField();
		mainPane.add(timeSyncVL1Field,"3,15,f,0");
		
		timeSyncVL2Label=new JLabel("ʱ��ͬ��VL2");
		mainPane.add(timeSyncVL2Label,"1,17,f,0");
		timeSyncVL2Field=ConfigUtils.getNumberTextField();
		mainPane.add(timeSyncVL2Field,"3,17,f,0");
		
		pcfVLLabel=new JLabel("PCF VL");
		mainPane.add(pcfVLLabel,"1,19,f,0");
		pcfVLField=ConfigUtils.getNumberTextField();
		mainPane.add(pcfVLField,"3,19,f,0");
		
		JLabel label8=new JLabel("ʱ��ͬ����ɫ");
		mainPane.add(label8,"1,21,f,0");
		timeSyncComboBox=new JComboBox<>();
		timeSyncComboBox.addItem("SM");
		timeSyncComboBox.addItem("SC");
		mainPane.add(timeSyncComboBox,"3,21,f,0");
		
		JLabel label9=new JLabel("��������");
		mainPane.add(label9,"1,23,f,0");
		overallIntervalField=ConfigUtils.getNumberTextField();
		mainPane.add(overallIntervalField,"3,23,f,0");
		
		JLabel label10=new JLabel("��Ⱥ����");
		mainPane.add(label10,"1,25,f,0");
		clusterIntervalField=ConfigUtils.getNumberTextField();
		mainPane.add(clusterIntervalField,"3,25,f,0");

		this.add(mainPane, BorderLayout.CENTER);
		
		//set values
		if(oldData!=null){
			switchNameField.setText(oldData.getSwitchName());
			
			switchPortNumberField.setText(oldData.getPortNumber()+"");
			
			switchLocalIDField.setText(oldData.getLocalDomainID()+"");
			
			eportNumField.setText(oldData.getEportNumber()+"");
			
			// TODO
			eportListField.setText(ConfigUtils.intListToString(oldData.getEportNos(), true));
			
			if(oldData.isEnableTimeSyncVL())
				timeSyncEnableBox.setSelectedIndex(0);
			else
				timeSyncEnableBox.setSelectedIndex(1);
			
			timeSyncVL1Field.setText(oldData.getTimeSyncVL1()+"");
			timeSyncVL2Field.setText(oldData.getTimeSyncVL2()+"");
			pcfVLField.setText(oldData.getPcfVL()+"");
			
			timeSyncComboBox.setSelectedIndex(oldData.getTimeSyncRole());
			
			overallIntervalField.setText(oldData.getOverallInterval()+"");
			clusterIntervalField.setText(oldData.getClusterInterval()+"");
			
			locationField.setText(oldData.getLocationId()+"");
			
		}else{
			switchPortNumberField.setText("32");
			
			int nextId=1;
			List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
			if(switches!=null && switches.size()>0){
				for(SwitchDevice sw : switches){
					if(sw.getLocalDomainID()>nextId){
						nextId=sw.getLocalDomainID();
					}
				}
				nextId++;
			}
			if(nextId<10)
				switchNameField.setText("net1_sw0"+nextId);
			else
				switchNameField.setText("net1_sw"+nextId);
			switchLocalIDField.setText(nextId+"");
			
			eportNumField.setText("1");
			
			eportListField.setText("1");
					
			timeSyncVL1Field.setText("0");
			timeSyncVL2Field.setText("0");
			pcfVLField.setText("0");
			
			overallIntervalField.setText("0");
			clusterIntervalField.setText("0");
			locationField.setText("0");
		}
		
		switchTimeSyncSelection();
	}
	
	private void switchTimeSyncSelection(){
		switch(timeSyncEnableBox.getSelectedIndex()){
		case 1: // enable
			timeSyncVL1Label.setEnabled(true);
			timeSyncVL1Field.setEnabled(true);
			timeSyncVL2Label.setEnabled(true);
			timeSyncVL2Field.setEnabled(true);
			break;
		case 0:
			timeSyncVL1Label.setEnabled(false);
			timeSyncVL1Field.setEnabled(false);
			timeSyncVL2Label.setEnabled(false);
			timeSyncVL2Field.setEnabled(false);
			break;
		}
	}
	
	public SwitchDevice getData(){
		if(switchNameField.getText().length()>16){
			JOptionPane.showMessageDialog(this, "���������Ʋ��ܳ���16Ӣ����ĸ!");
			return null;
		}
		
		SwitchDevice data=new SwitchDevice(switchNameField.getText());
		if(oldData!=null){
			ConfigUtils.copyHideFieldsForSwitch(oldData, data);
		}
		
		try{
			data.setPortNumber(Integer.valueOf(switchPortNumberField.getText()));
			
			data.setLocalDomainID(Integer.valueOf(switchLocalIDField.getText()));
			if(data.getLocalDomainID()<=0 || data.getLocalDomainID()>0xEF){
				JOptionPane.showMessageDialog(this, "������ID������0x1~0xEF(239)��!");
				return null;
			}
			
			data.setEportNumber(Integer.valueOf(eportNumField.getText()));
			if(data.getEportNumber()<0 || data.getEportNumber()>6){
				JOptionPane.showMessageDialog(this, "EPort����������0~6��!");
				return null;
			}
			
			List<Integer> eportList = ConfigUtils.stringToIntList(eportListField.getText(), true);
			data.setEportNos(eportList);
			if(eportList.size()!=data.getEportNumber()){
				JOptionPane.showMessageDialog(this, "EPort����"+data.getEportNumber()+"��FE�б�"+eportList.size()+"��һ��!");
				return null;
			}

			data.setEnableTimeSyncVL(timeSyncEnableBox.getSelectedIndex()==0);
			data.setTimeSyncVL1(Short.valueOf(timeSyncVL1Field.getText()));
			data.setTimeSyncVL2(Short.valueOf(timeSyncVL2Field.getText()));
			data.setPcfVL(Short.valueOf(pcfVLField.getText()));
			
			data.setTimeSyncRole(timeSyncComboBox.getSelectedIndex());
			data.setOverallInterval(Integer.valueOf(overallIntervalField.getText()));
			data.setClusterInterval(Integer.valueOf(clusterIntervalField.getText()));
			data.setLocationId(Integer.valueOf(locationField.getText().trim()));

		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "�������ݸ�ʽ����ȷ:"+ex.getMessage());
			return null;
		}
		return data;
	}

}
