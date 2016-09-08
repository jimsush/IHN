package dima.config.view;

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
 * create/update configuration table
 *
 */
public class CreateConfigTableDialog extends JDialog{

	private static final long serialVersionUID = 1568720669350414672L;

	private boolean isAdd=true;
	
	private int oldCfgTableId=0;
	//private int oldPortNum=0;
	
	private JTextField cfgTableIdfield;
	//private JTextField portNumfield;
	
	private ConfigTableCallback dataChangeCallback;
	
	public CreateConfigTableDialog(Window parent, String title, boolean isAdd, int oldCfgTableId, ConfigTableCallback dataChangeCallback){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		setSize(300, 100);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
		setAlwaysOnTop(true);
		
		this.dataChangeCallback=dataChangeCallback;
		this.isAdd=isAdd;
		this.oldCfgTableId=oldCfgTableId;

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
	        ConfigUtils.getTableLayoutRowParam(1, 4) });
		JPanel pane=new JPanel(layout);
	    
		JLabel label1=new JLabel("配置表编号(1~4)");
		pane.add(label1,"1,1,f,0");
		
		cfgTableIdfield=ConfigUtils.getNumberTextField(1);
		pane.add(cfgTableIdfield,"3,1,f,0");
		
		if(!isAdd){
			// for update
			cfgTableIdfield.setText(oldCfgTableId+"");
			cfgTableIdfield.setEnabled(false);
		}else{
			cfgTableIdfield.setText("1");
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
                	if(cfgTableIdfield.getText().trim().length()<=0){
                		JOptionPane.showMessageDialog(CreateConfigTableDialog.this, "配置表编号不能为空");
                		return;
                	}
                	
                	int cfgTableId=Integer.valueOf(cfgTableIdfield.getText().trim());
                	if(cfgTableId<=0 || cfgTableId>4){
                		JOptionPane.showMessageDialog(CreateConfigTableDialog.this, "配置表编号:1~4");
                		return;
                	}

                	try{
                		dataChangeCallback.addOrUpdateConfigTable(isAdd, cfgTableId);
                	}catch(Exception ex){
        				String msg=ex.getMessage();
        				if(msg!=null && msg.contains("already exist")){
        					JOptionPane.showMessageDialog(CreateConfigTableDialog.this, "配置表编号已经存在");
        					return;
        				}else{
        					JOptionPane.showMessageDialog(CreateConfigTableDialog.this, "错误:"+msg);
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
