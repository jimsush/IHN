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
 * @deprecated
 * update VL's Plan information: plan ID, vlN
 *
 */
public class CreateVLPlanDialog extends JDialog{

	private static final long serialVersionUID = 1568720669350414672L;

	private boolean isAdd=true;
	
	private int oldPlanId=0;
	private int oldVLNum=0;
	
	private JTextField planIdField;
	private JTextField vlNumField;
	
	private VLConfigPane tablePane;
	
	public CreateVLPlanDialog(Window parent, String title, boolean isAdd, int oldPlanId
			, int oldVLNum, VLConfigPane parentPane){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		setSize(300, 120);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
		setAlwaysOnTop(true);
		
		this.tablePane=parentPane;
		this.isAdd=isAdd;
		
		this.oldPlanId=oldPlanId;
		this.oldVLNum=oldVLNum;

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
	        ConfigUtils.getTableLayoutRowParam(2, 4) });
		JPanel pane=new JPanel(layout);
	    
		JLabel label1=new JLabel("配置方案编号");
		pane.add(label1,"1,1,f,0");
		
		planIdField=ConfigUtils.getNumberTextField(3);
		planIdField.setEditable(false);
		pane.add(planIdField,"3,1,f,0");
		
		JLabel label2=new JLabel("VL个数");
		pane.add(label2,"1,3,f,0");
		
		vlNumField=ConfigUtils.getNumberTextField(4);
		vlNumField.setToolTipText("0~4096");
		pane.add(vlNumField,"3,3,f,0");

		if(!isAdd){
			// for update
			planIdField.setText(oldPlanId+"");
			planIdField.setEnabled(false);
			
			vlNumField.setText(oldVLNum+"");
		}else{
			planIdField.setText("0");
			vlNumField.setText("1");
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
                	if(planIdField.getText().trim().length()<=0){
                		JOptionPane.showMessageDialog(CreateVLPlanDialog.this, "配置方案编号不能为空");
                		return;
                	}
                	if(vlNumField.getText().trim().length()<=0){
                		JOptionPane.showMessageDialog(CreateVLPlanDialog.this, "VL个数不能为空");
                		return;
                	}
                	
                	int curPlanId=Integer.valueOf(planIdField.getText().trim());
                	if(curPlanId<=0){
                		JOptionPane.showMessageDialog(CreateVLPlanDialog.this, "配置方案编号不正确");
                		return;
                	}
                	
                	int curVLNum=Integer.valueOf(vlNumField.getText().trim());
                	if(curVLNum<=0 || curVLNum>4096){
                		JOptionPane.showMessageDialog(CreateVLPlanDialog.this, "VL个数范围不正确");
                		return;
                	}
                	
                	try{
                		//tablePane.addOrUpdatePlan(isAdd, curPlanId, curVLNum);
                	}catch(Exception ex){
        				String msg=ex.getMessage();
        				if(msg!=null && msg.contains("already exist")){
        					JOptionPane.showMessageDialog(CreateVLPlanDialog.this, "配置表编号已经存在");
        					return;
        				}else{
        					JOptionPane.showMessageDialog(CreateVLPlanDialog.this, "错误:"+msg);
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
