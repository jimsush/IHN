package dima.config.view.netconfig.topo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.view.netconfig.TopoView;
import twaver.TWaverUtil;

/**
 * set network redundancy
 *
 */
public class SetRedundancyDialog extends JDialog{

	private static final long serialVersionUID = 1568720669350414672L;

	private JTextField redundancyField;
	private TopoView topoView;
	
	public SetRedundancyDialog(Window parent, String title,TopoView topoView){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		setSize(300, 100);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        setAlwaysOnTop(true);
		
        this.topoView=topoView;
        
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
	    
		JLabel label1=new JLabel("网络冗余份数(1~2)");
		pane.add(label1,"1,1,f,0");
		
		redundancyField=ConfigUtils.getNumberTextField(1);
		pane.add(redundancyField,"3,1,f,0");
		
		redundancyField.setText(ConfigContext.REDUNDANCY+"");
		
		
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
                	if(redundancyField.getText().trim().length()<=0){
                		JOptionPane.showMessageDialog(SetRedundancyDialog.this, "冗余数不能为空");
                		return;
                	}
                	
                	int redundancy=Integer.valueOf(redundancyField.getText().trim());
                	if(redundancy<=0 || redundancy>2){
                		JOptionPane.showMessageDialog(SetRedundancyDialog.this, "冗余数:1~2");
                		return;
                	}

                	try{
                		BufferedWriter bufWriter = new BufferedWriter(new FileWriter(ConfigUtils.SYS_PROP_FILE));
                		bufWriter.write(ConfigUtils.PROP_KEY_REDUNDANCY+"="+redundancy);
                		bufWriter.newLine();
                		bufWriter.close();
                		
                		if(ConfigContext.REDUNDANCY!=redundancy){
                			// update topology view
                			topoView.updateAttachment(redundancy);
                			
                			ConfigContext.REDUNDANCY=redundancy;
                		}
                	}catch(Exception ex){
        				String msg=ex.getMessage();
        				JOptionPane.showMessageDialog(SetRedundancyDialog.this, "错误:"+msg);
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
