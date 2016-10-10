package dima.config.view.versionconfig;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;

public class MergeBinFilesPane extends JPanel{

	private static final long serialVersionUID = -4950859739959600444L;

	private ConfigDAO dao;
	private JButton swBtn;
	private JButton nodeBtn;
	private JButton monitorBtn;
	
	public MergeBinFilesPane(){
		initService();
		initUI();
	}
	
	private void initUI(){
		this.setLayout(new BorderLayout());
		
		JPanel topPane = createInputPanel();
		this.add(topPane, BorderLayout.NORTH);
		
		setActionListeners();
	}
	
	private void initService(){
		dao=ServiceFactory.getService(ConfigDAO.class);
		System.out.println(dao.getClass().getSimpleName());
	}
	
	private JPanel createInputPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
	        { 10,  180, 20, 180, 10 }, 
	        ConfigUtils.getTableLayoutRowParam(3, 4) });
		JPanel pane=new JPanel(layout);
		
		JLabel label1=new JLabel("  合并交换机配置文件: ");
		pane.add(label1,"1,1,f,0");
		
		swBtn = new JButton("合并...");
		pane.add(swBtn,"3,1,f,0");
		
		JLabel label2=new JLabel("  合并节点配置文件: ");
		pane.add(label2,"1,3,f,0");
		
		nodeBtn = new JButton("合并...");
		pane.add(nodeBtn,"3,3,f,0");
		
		JLabel label3=new JLabel("  合并交换机监控配置文件: ");
		pane.add(label3,"1,5,f,0");
		
		monitorBtn = new JButton("合并...");
		pane.add(monitorBtn,"3,5,f,0");

		return pane;
	}
	
	private void setActionListeners(){
		ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object source = e.getSource();
                if (source == swBtn) {
                	try{
                		mergeBinFiles(0);
                	}catch(Exception ex){
                		JOptionPane.showMessageDialog(MergeBinFilesPane.this, "合并交换机配置文件出错: "+ex.getMessage());
                	}
                }else if (source == nodeBtn) {
                	try{
                		mergeBinFiles(1);
                	}catch(Exception ex){
                		JOptionPane.showMessageDialog(MergeBinFilesPane.this, "合并节点机配置文件出错: "+ex.getMessage());
                	}
                }else if (source == monitorBtn) {
                	try{
                		mergeBinFiles(2);
                	}catch(Exception ex){
                		JOptionPane.showMessageDialog(MergeBinFilesPane.this, "合并交换机监控配置文件出错: "+ex.getMessage());
                	}
                }
            }
	    };
	    
	    swBtn.addActionListener(l);
	    nodeBtn.addActionListener(l);
	    monitorBtn.addActionListener(l);
	}
	
	private void mergeBinFiles(int code){
		String[] objs=new String[]{"交换机", "节点机", "交换机监控"};
		MergeObjectsDialog dlg=new MergeObjectsDialog(ConfigContext.mainFrame, "合并"+objs[code]+"的Bin配置文件", code);
		dlg.setVisible(true);
	}
	
}
