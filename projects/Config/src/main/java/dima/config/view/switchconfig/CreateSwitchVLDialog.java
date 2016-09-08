package dima.config.view.switchconfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchVL;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.TWaverUtil;

public class CreateSwitchVLDialog extends JDialog{

	private static final long serialVersionUID = 4478166130836141013L;
	
	private String switchName;
	private int currentCfgTableId;
	private int currentPlanId;
	
	private ConfigDAO dao;
	private SwitchVL oldData;
	
	private JComboBox<String> switchBox;
	private JTextField vlIDTxtField;
	
	private JComboBox<String> typeBox;
	
	private JComboBox<String> beBox;
	private JLabel beLabel;
	
	private JPanel rcPane;
	private JLabel bagLabel;
	private JTextField bagTxtField;
	private JLabel jitterLabel;
	private JTextField jitterTxtField;
	
	private JPanel ttPane;
	private JLabel intervalLabel;
	private JTextField intervalTxtField;
	private JLabel windowLabel;
	private JTextField windowTxtField;
	
	private int PORT_NUM=16;
	private JButton[] inputButtons=null;
	private JButton[] outputButtons=null;
	
	private VLConfigPane tablePanel;
	
	public CreateSwitchVLDialog(Window parent, String title, String switchName, 
			int currentCfgTableId, int currentPlanId,
			SwitchVL data, VLConfigPane tablePanel){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.switchName=switchName;
		this.currentCfgTableId=currentCfgTableId;
		this.currentPlanId=currentPlanId;
		this.oldData=data;
		this.tablePanel=tablePanel;
		
		initService();
		initView();
	}
	
	private void initService(){
		dao=ServiceFactory.getService(ConfigDAO.class);
		
		SwitchDevice swDev = dao.readSwitchDevice(this.switchName, true);
		PORT_NUM=swDev.getPortNumber();
		
		inputButtons=new JButton[PORT_NUM+1];
		outputButtons=new JButton[PORT_NUM+1];
	}
	
	private void initView(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		int height=getDialogHeight();
        setSize(500, height);
        
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
		LayoutManager borderLayout = new BorderLayout(10, 10);
        JPanel contentPane = new JPanel(borderLayout);
        
        JPanel inputPane = createTopPanel();
        contentPane.add(inputPane, BorderLayout.NORTH);
        
        JPanel portPane = createPortNoButtonPanel();
        contentPane.add(portPane, BorderLayout.CENTER);
        
        JPanel btnPanel = createButtonPanel();
        contentPane.add(btnPanel, BorderLayout.SOUTH);
        
        getContentPane().add(contentPane);
	}
	
	private int getDialogHeight(){
		int btnH=getButtonAreaHeight();
		int height=btnH+105;
		System.out.println("dialog height:"+height);
		return height;
	}
	
	private int getButtonAreaHeight(){
		int height=44+(((PORT_NUM-11)/4+1)+3)*50;
		System.out.println("button area height:"+height);
		return height;
	}
	
	private JPanel createTopPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 6,  100, 4, 100, TableLayout.FILL, 100, 4, 100, 6 }, 
            { 4, 22, 4, 22, 10, 50, TableLayout.FILL, 4} });
	    JPanel inputPane = new JPanel(layout);
	    
	    JLabel label1=new JLabel("交换机名称:");
	    inputPane.add(label1, "1,1,f,0");
	    
	    switchBox=new JComboBox<>();
	    switchBox.addItem(switchName);
	    switchBox.setEditable(false);
	    inputPane.add(switchBox, "3,1,f,0");
	    
	    JLabel label2=new JLabel("虚拟链路号:");
	    inputPane.add(label2, "5,1,f,0");
	    
	    vlIDTxtField=ConfigUtils.getNumberTextField();
	    vlIDTxtField.setText("0");
	    vlIDTxtField.setToolTipText("0~4095");
	    if(oldData!=null){
	    	vlIDTxtField.setText(oldData.getVLID()+"");
	    	vlIDTxtField.setEnabled(false);
	    }
	    inputPane.add(vlIDTxtField, "7,1,f,0");
	    
	    JLabel label3=new JLabel("类型:");
	    inputPane.add(label3, "1,3,f,0");
	    
	    typeBox=new JComboBox<>();
	    typeBox.addItem("BE");
	    typeBox.addItem("RC");
	    typeBox.addItem("TT");
	    if(oldData!=null){
	    	typeBox.setSelectedIndex(oldData.getType()-1);
	    }else{
	    	typeBox.setSelectedIndex(1);
	    }
	    inputPane.add(typeBox, "3,3,f,0");
	    typeBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				switchTypeSelection();
			}
		});

	    beLabel=new JLabel("BE优先级:");
	    inputPane.add(beLabel, "5,3,f,0");
	    
	    beBox=new JComboBox<>();
	    beBox.addItem("高");
	    beBox.addItem("低");
	    beBox.setSelectedIndex(0);
	    inputPane.add(beBox, "7,3,f,0");
	    
	    // RC
	    LayoutManager rclayout =  new TableLayout(new double[][] {
            { 6,  80, TableLayout.FILL, 80, 6 }, 
            { 4, 22, 4, 22, TableLayout.FILL, 4} });
	    rcPane = new JPanel(rclayout);
	    rcPane.setBorder(BorderFactory.createTitledBorder("RC属性设置")); 
	    
	    bagLabel=new JLabel("BAG:");
	    rcPane.add(bagLabel, "1,1,f,0");
	    
	    bagTxtField=ConfigUtils.getNumberTextField();
	    bagTxtField.setText("500");
	    if(oldData!=null){
	    	bagTxtField.setText(oldData.getBag()+"");
	    }
	    rcPane.add(bagTxtField, "3,1,f,0");
	    
	    jitterLabel=new JLabel("Jitter:");
	    rcPane.add(jitterLabel, "1,3,f,0");
	    
	    jitterTxtField=ConfigUtils.getNumberTextField();
	    jitterTxtField.setText("50");
	    if(oldData!=null){
	    	jitterTxtField.setText(oldData.getJitter()+"");
	    }
	    rcPane.add(jitterTxtField, "3,3,f,0");
	    
	    inputPane.add(rcPane, "1,5,3,7");

	    // TT
	    ttPane = new JPanel(rclayout);
	    ttPane.setBorder(BorderFactory.createTitledBorder("TT属性设置")); 
	    intervalLabel=new JLabel("周期:");
	    ttPane.add(intervalLabel, "1,1,f,0");
	    
	    intervalTxtField=ConfigUtils.getNumberTextField();
	    intervalTxtField.setText("0");
	    ttPane.add(intervalTxtField, "3,1,f,0");
	    
	    windowLabel=new JLabel("窗口:");
	    ttPane.add(windowLabel, "1,3,f,0");
	    
	    windowTxtField=ConfigUtils.getNumberTextField();
	    windowTxtField.setText("0");
	    ttPane.add(windowTxtField, "3,3,f,0");
	    
	    inputPane.add(ttPane, "5,5,7,7");
	    
	    // init selection
	    switchTypeSelection();
	    
	    return inputPane;
	}
	
	private JPanel createPortNoButtonPanel(){
		int height=this.getButtonAreaHeight();
		LayoutManager layout =  new TableLayout(new double[][] {
            { 4,  240, TableLayout.FILL, 240, 4 }, { 4, height, TableLayout.FILL, 4}});
	    JPanel portButtonPane = new JPanel(layout);
	    
	    JPanel leftPanel = new JPanel(new FlowLayout());
	    leftPanel.setBorder(BorderFactory.createTitledBorder("输入端口")); 
	    for(int i=0; i<PORT_NUM; i++){
	    	inputButtons[i]=new JButton(i+"");
	    	leftPanel.add(inputButtons[i]);
	    	
	    	if(oldData!=null){
	    		if(i==oldData.getInputPortNo()){
	    			inputButtons[i].setBackground(Color.RED);
	    			inputButtons[i].setForeground(Color.RED);
	    		}
	    	}
	    	
	    	setInputPortActions(inputButtons[i]);
	    }
	    
	    inputButtons[PORT_NUM]=new JButton(PORT_NUM+"(交换机网络管理单元)");
    	setInputPortActions(inputButtons[PORT_NUM]);
    	leftPanel.add(inputButtons[PORT_NUM]);

    	Set<Integer> outputPorts=new HashSet<>();
    	if(oldData!=null){
    		outputPorts.addAll(oldData.getOutputPortNos());
    	}
    	
	    JPanel rightPanel = new JPanel();
	    rightPanel.setBorder(BorderFactory.createTitledBorder("输出端口")); 
	    for(int i=0; i<PORT_NUM; i++){
	    	outputButtons[i]=new JButton(i+"");
	    	rightPanel.add(outputButtons[i]);
	    	if(outputPorts.contains(i)){
	    		outputButtons[i].setBackground(Color.RED);
	    		outputButtons[i].setForeground(Color.RED);
	    	}
	    	
	    	setOutPortActions(outputButtons[i]);
	    }
	    outputButtons[PORT_NUM]=new JButton(PORT_NUM+"(交换机网络管理单元)");
	    setOutPortActions(outputButtons[PORT_NUM]);
	    rightPanel.add(outputButtons[PORT_NUM]);

	    portButtonPane.add(leftPanel,"1,1,f,0");
	    portButtonPane.add(rightPanel,"3,1,f,0");
	    
	    return portButtonPane;
	}
	
	private JPanel createButtonPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 80,  80, TableLayout.FILL, 80, 80 }, { 4, 22, TableLayout.FILL, 4}});
	    JPanel buttonPane = new JPanel(layout);
	    final JButton okBtn = new JButton("确定");
	    final JButton cancelBtn = new JButton("取消");
	    
	    buttonPane.add(okBtn,"1,1,f,0");
	    buttonPane.add(cancelBtn,"3,1,f,0");
	    
	    ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object source = e.getSource();
                if (source == okBtn) {
                	SwitchVL newVL=getData();
                	if(newVL==null){
                		return;
                	}
                	
                	boolean result=tablePanel.addOrUpdateSwitchVL(newVL, oldData, CreateSwitchVLDialog.this);
                	if(result){
                		dispose();
                	}
                }else if(source==cancelBtn){
                	dispose();
                }
            }
	    };
	    
	    okBtn.addActionListener(l);
        cancelBtn.addActionListener(l);
        
		return buttonPane;
	}
	
	private void setInputPortActions(JButton btn){
		btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Color.RED.equals(btn.getBackground())){
					btn.setBackground(null);
					btn.setForeground(null);
				}else{
					for(int j=0; j<=PORT_NUM; j++){
						inputButtons[j].setBackground(null);
						inputButtons[j].setForeground(null);
					}
					btn.setBackground(Color.RED);
					btn.setForeground(Color.RED);
				}
			}
    	});
	}
	
	private void setOutPortActions(JButton btn){
		btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Color.RED.equals(btn.getBackground())){
					btn.setBackground(null);
					btn.setForeground(null);
				}else{
					btn.setBackground(Color.RED);
					btn.setForeground(Color.RED);
				}
			}
    	});
	}
	
	private void switchTypeSelection(){
		switch(typeBox.getSelectedIndex()){
		case 0: //be
			beBox.setEnabled(true);
			beLabel.setEnabled(true);
			rcPane.setEnabled(false);
			bagLabel.setEnabled(false);
			bagTxtField.setEnabled(false);
			jitterLabel.setEnabled(false);
			jitterTxtField.setEnabled(false);
			ttPane.setEnabled(false);
			windowTxtField.setEnabled(false);
			windowLabel.setEnabled(false);
			intervalTxtField.setEnabled(false);
			intervalLabel.setEnabled(false);
			break;
		case 1: //rc
			beBox.setEnabled(false);
			beLabel.setEnabled(false);
			rcPane.setEnabled(true);
			bagLabel.setEnabled(true);
			bagTxtField.setEnabled(true);
			jitterLabel.setEnabled(true);
			jitterTxtField.setEnabled(true);
			ttPane.setEnabled(false);
			windowTxtField.setEnabled(false);
			windowLabel.setEnabled(false);
			intervalTxtField.setEnabled(false);
			intervalLabel.setEnabled(false);
			break;
		case 2: //tt
			beBox.setEnabled(false);
			beLabel.setEnabled(false);
			rcPane.setEnabled(false);
			bagLabel.setEnabled(false);
			bagTxtField.setEnabled(false);
			jitterLabel.setEnabled(false);
			jitterTxtField.setEnabled(false);
			ttPane.setEnabled(true);
			windowTxtField.setEnabled(true);
			windowLabel.setEnabled(true);
			intervalTxtField.setEnabled(true);
			intervalLabel.setEnabled(true);
			break;
		}
	}
	
	public SwitchVL getData(){
		String swName=switchBox.getSelectedItem().toString();
		SwitchVL vl=null;
		try{
			String vlIDStr=vlIDTxtField.getText().trim();
			if(vlIDStr.length()==0){
				JOptionPane.showMessageDialog(this, "没有输入VL_ID");
				return null;
			}
			
			vl=new SwitchVL(swName, currentCfgTableId, currentPlanId, Integer.valueOf(vlIDStr));
			if(vl.getVLID()<0 || vl.getVLID()>4095){
				JOptionPane.showMessageDialog(this, "VL_ID必须在0~4095间");
				return null;
			}
			
			vl.setType(typeBox.getSelectedIndex()+1);
			vl.setBe(beBox.getSelectedIndex());
			vl.setBag(Integer.valueOf(bagTxtField.getText().trim()));
			vl.setJitter(Integer.valueOf(jitterTxtField.getText().trim()));
			vl.setTtInterval(Integer.valueOf(intervalTxtField.getText().trim()));
			vl.setTtWindow(Integer.valueOf(windowTxtField.getText().trim()));
			
			vl.setInputPortNo(-1);
			for(int i=0; i<=PORT_NUM; i++){
				if(Color.RED.equals(inputButtons[i].getBackground())){
					vl.setInputPortNo(i);
					break;
				}
			}
			if(vl.getInputPortNo()==-1){
				JOptionPane.showMessageDialog(this, "没有选择输入端口");
				return null;
			}
			
			List<Integer> outputPortNos=new ArrayList<>();
			for(int i=0; i<=PORT_NUM; i++){
				if(Color.RED.equals(outputButtons[i].getBackground())){
					outputPortNos.add(i);
				}
			}
			vl.setOutputPortNos(outputPortNos);
			if(vl.getOutputPortNos()==null || vl.getOutputPortNos().size()==0){
				JOptionPane.showMessageDialog(this, "没有选择输出端口");
				return null;
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "输入格式有误:"+ex.getMessage());
			return null;
		}
		return vl;
	}
	
}
