package dima.config.view.switchconfig;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import twaver.TWaverUtil;

/**
 * create/update VL configuration table
 *
 */
public class CreateVLCfgTableDialog extends JDialog{

	private static final long serialVersionUID = 1568720669350414672L;

	private boolean isAdd=true;
	
	private int oldCfgTableId=0;
	private int oldPlanNum=0;
	private int oldDefaultPlanId=0;
	
	private JTextField cfgTableIdField;
	private JTextField planNumField;
	private JTextField defaultPlanField;
	
	private VLConfigPane tablePane;
	
	public CreateVLCfgTableDialog(Window parent, String title, boolean isAdd, int oldCfgTableId
			, int oldPlanNum, int oldDefaultPlanId, VLConfigPane parentPane){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		setSize(300, 146);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
		setAlwaysOnTop(true);
		
		this.tablePane=parentPane;
		this.isAdd=isAdd;
		
		this.oldCfgTableId=oldCfgTableId;
		this.oldPlanNum=oldPlanNum;
		this.oldDefaultPlanId=oldDefaultPlanId;

		initView();
	}
	
	private void initView(){
		Container mainPane = getContentPane();
		JPanel formPane=createFormPanel();
		mainPane.add(formPane, BorderLayout.CENTER);
	
		JPanel buttonPane=createButtonPanel();
		mainPane.add(buttonPane, BorderLayout.SOUTH);
	}

	private JPanel createFormPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
	        { 10,  120, TableLayout.FILL, 120, 10 }, 
	        ConfigUtils.getTableLayoutRowParam(3, 4) });
		JPanel pane=new JPanel(layout);
	    
		JLabel label1=new JLabel("配置表编号(1~4)");
		pane.add(label1,"1,1,f,0");
		
		cfgTableIdField=ConfigUtils.getNumberTextField(1);
		cfgTableIdField.setToolTipText("1~4");
		pane.add(cfgTableIdField,"3,1,f,0");
		
		JLabel label2=new JLabel("方案个数");
		pane.add(label2,"1,3,f,0");
		
		planNumField=ConfigUtils.getNumberTextField(2);
		pane.add(planNumField,"3,3,f,0");
		
		JLabel label3=new JLabel("缺省方案编号");
		pane.add(label3,"1,5,f,0");
		
		defaultPlanField=ConfigUtils.getNumberTextField(2);
		pane.add(defaultPlanField,"3,5,f,0");
		
		if(!isAdd){
			// for update
			cfgTableIdField.setText(oldCfgTableId+"");
			cfgTableIdField.setEnabled(false);
			
			planNumField.setText(oldPlanNum+"");
			defaultPlanField.setText(oldDefaultPlanId+"");
		}else{
			cfgTableIdField.setText("1");
			planNumField.setText("1");
			defaultPlanField.setText("1");
		}
		
		return pane;
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
                	if(cfgTableIdField.getText().trim().length()<=0){
                		JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "配置表编号不能为空");
                		return;
                	}
                	if(planNumField.getText().trim().length()<=0){
                		JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "配置方案个数不能为空");
                		return;
                	}
                	if(defaultPlanField.getText().trim().length()<=0){
                		JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "缺省配置方案编号不能为空");
                		return;
                	}
                	
                	int cfgTableId=Integer.valueOf(cfgTableIdField.getText().trim());
                	if(cfgTableId<=0 || cfgTableId>4){
                		JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "配置表编号:1~4");
                		return;
                	}
                	
                	int planNum=Integer.valueOf(planNumField.getText().trim());
                	if(planNum<=0){
                		JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "配置方案个数范围不正确");
                		return;
                	}
                	
                	int defaultPlanId=Integer.valueOf(defaultPlanField.getText().trim());
                	if(defaultPlanId<=0){
                		JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "缺省配置方案不正确");
                		return;
                	}
                	
                	try{
                		tablePane.addOrUpdateConfigTable(isAdd, cfgTableId, planNum, defaultPlanId);
                	}catch(Exception ex){
        				String msg=ex.getMessage();
        				if(msg!=null && msg.contains("already exist")){
        					JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "配置表编号已经存在");
        					return;
        				}else{
        					JOptionPane.showMessageDialog(CreateVLCfgTableDialog.this, "错误:"+msg);
        					return;
        				}
        			}
                	
                	dispose();
                }else if(source==cancelBtn){
                	dispose();
                }
            }
	    };
	    
	    okBtn.addActionListener(l);
        cancelBtn.addActionListener(l);
        
		return buttonPane;
	}
	
}
