package dima.config.view.switchconfig;

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
import javax.swing.JPanel;

import dima.config.common.controls.TableLayout;
import dima.config.common.models.SwitchVLPlan;
import twaver.TWaverUtil;

public class ChoosePlanDialog extends JDialog{

	private static final long serialVersionUID = -31949476570247773L;
	
	private List<SwitchVLPlan> plans;
	private JComboBox<String> planBox;
	private CreateMonitorDialog parentDlg;
	
	public ChoosePlanDialog(Window parent, String title, List<SwitchVLPlan> plans, CreateMonitorDialog parentDlg){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);

		this.plans=plans;
		this.parentDlg=parentDlg;
		
		initView();
	}
	
	private void initView(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(300, 100);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
        LayoutManager borderLayout = new BorderLayout(10,10);
        JPanel contentPane = new JPanel(borderLayout);
        
        JPanel inputPane = createTopPanel();
        contentPane.add(inputPane, BorderLayout.NORTH);
        
        JPanel btnPanel = createButtonPanel();
        contentPane.add(btnPanel, BorderLayout.SOUTH);
        
        getContentPane().add(contentPane);
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
                	String planDisplayName = planBox.getSelectedItem().toString();
                	int planId=getPlanId(planDisplayName);
                	
                	parentDlg.setSelectedPlanId(planId);
                	
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

	private JPanel createTopPanel() {
		LayoutManager layout =  new TableLayout(new double[][] {
            { 20,  90, 10, TableLayout.FILL, 20 }, 
            { 4, 22, 4} });
	    JPanel inputPane = new JPanel(layout);
	    
	    JLabel label1=new JLabel("配置方案:");
	    inputPane.add(label1, "1,1,f,0");
	    
	    planBox=new JComboBox<>();
	    for(SwitchVLPlan plan :  plans){
	    	planBox.addItem(getPlanString(plan));
	    }
	    if(planBox.getItemCount()>0){
	    	planBox.setSelectedIndex(0);
	    }
	    inputPane.add(planBox, "3,1,f,0");

	    return inputPane;
	}

	private String getPlanString(SwitchVLPlan plan){
		return "配置方案"+plan.getPlanId()+"("+plan.getVls().size()+"个VL)";
	}
	private int getPlanId(String displayName){
		int pos=displayName.indexOf("(");
		String planIdStr = displayName.substring(4, pos);
		return Integer.valueOf(planIdStr);
	}
	


}
