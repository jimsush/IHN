package dima.config.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.JAccordion;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import dima.config.dao.ConfigDAOImpl;
import dima.config.view.importfile.ImportFileActions;
import dima.config.view.netconfig.NodeListPanel;
import dima.config.view.netconfig.SwitchListPanel;
import dima.config.view.netconfig.TopoView;
import dima.config.view.nodeconfig.MessageConfigPanel;
import dima.config.view.nodeconfig.NodeVLConfigPanel;
import dima.config.view.switchconfig.MonitorConfigPane;
import dima.config.view.switchconfig.VLConfigPane;
import dima.config.view.versionconfig.MergeBinFilesPane;
import dima.config.view.versionconfig.VersionPane;
import twaver.TWaverUtil;

public class ConfigMain {

	public static JPanel getDummyPanel(String iconName, String name) {
		  JPanel panel = new JPanel(new BorderLayout());
		  
		  URL iconPath = ConfigUtils.getImageURL(iconName);

		  ImageIcon icon=new ImageIcon(iconPath, name);
		  
		  JButton btn = new JButton(icon); 
		  btn.setVerticalTextPosition(JButton.BOTTOM);
		  btn.setHorizontalTextPosition(JButton.CENTER);
		  btn.setText(name);
		  
		  panel.add(btn);
		  
		  return panel;
	}
	
	public static JPanel getDummyPanel1(String[] iconName, String[] name, ActionListener[] listeners) {
		  JPanel panel = new JPanel();
		  
		  panel.setLayout(new GridLayout(iconName.length, 1));
		  for(int i=0; i<iconName.length; i++){
			  URL iconPath = ConfigUtils.getImageURL(iconName[i]);
			  ImageIcon icon=new ImageIcon(iconPath, name[i]);
			  JButton btn = new JButton(icon); 
			  
			  btn.setVerticalTextPosition(JButton.BOTTOM);
			  btn.setHorizontalTextPosition(JButton.CENTER);
			  btn.setText(name[i]);
			  
			  if(listeners!=null && listeners.length>=(i+1)){
				  btn.addActionListener(listeners[i]);
			  }
			  
			  panel.add(btn);
		  }
		  
		  return panel;
	}
	
	public static void main(String[] args){
		ConfigDAOImpl daoImpl=new ConfigDAOImpl();
		daoImpl.init();

		ServiceFactory.registerService(ConfigDAO.class, daoImpl);
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame("�������ù���");
		BufferedImage bi=null;
		try{
			URL url = ConfigUtils.getImageURL("logo.png");
			InputStream is = url.openStream();
			bi = ImageIO.read(is);
			is.close();
		}catch(Exception ex){
			System.out.println("warning: Can't find logo.png");
		}
		if(bi!=null){
			frame.setIconImage(bi); 
		}
		ConfigContext.mainFrame=frame;
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);     

		JPanel mainPanel=new JPanel(new BorderLayout());
		
		ActionListener topoListener = getPanelActionListener(TopoView.class);
		ActionListener switchListListener=getPanelActionListener(SwitchListPanel.class);
		ActionListener nodeListListener=getPanelActionListener(NodeListPanel.class);
		
		ActionListener switchVLListener = getPanelActionListener(VLConfigPane.class);
		ActionListener monitorListener=getPanelActionListener(MonitorConfigPane.class);
		ActionListener nodeVLListener=getPanelActionListener(NodeVLConfigPanel.class);
		ActionListener messageListener=getPanelActionListener(MessageConfigPanel.class);
		
		ActionListener versionListener=getPanelActionListener(VersionPane.class);
		ActionListener mergeListener=getPanelActionListener(MergeBinFilesPane.class);
		
		JAccordion outlookBar = new JAccordion();
		  outlookBar.addBar("��������",   getDummyPanel1(new String[]{"network32.png","switch-icon32.png","node32.png"}
		  , new String[]{"��������ͼ","����������", "�ڵ�����"} 
		  , new ActionListener[]{topoListener, switchListListener, nodeListListener}));
		  
		outlookBar.addBar("����������",   getDummyPanel1(new String[]{"vl32.png","monitor32.png"}
		  , new String[]{"VL����", "�������"} 
		  , new ActionListener[]{switchVLListener, monitorListener}));
		  
		outlookBar.addBar("�ڵ�����", getDummyPanel1(new String[]{"vl32.png","message32.png"}
		  , new String[]{"VL����", "��Ϣ����"} 
		  , new ActionListener[]{nodeVLListener, messageListener}));
		  
		outlookBar.addBar("ϵͳ����",  getDummyPanel1(new String[]{"message32.png","message32.png"},
				new String[]{"�汾��Ϣ", "�ϲ�Bin�ļ�"},
				new ActionListener[]{versionListener, mergeListener}));
		
		outlookBar.setVisibleBar(0);
		outlookBar.setPreferredSize(new Dimension(200, 600));
		frame.getContentPane().setLayout(new BorderLayout());
		  
		frame.getContentPane().add(outlookBar, BorderLayout.WEST);
		  
		ConfigContext.rightPanel=mainPanel;
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(d.width / 2 - 400, d.height / 2 - 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = createMainMenu();
		frame.setJMenuBar(menuBar);

		frame.pack();
				
		TWaverUtil.centerWindow(frame);
		
		frame.setVisible(true);
	}

	private static JMenuBar createMainMenu() {
		JMenuBar menuBar=new JMenuBar();
		JMenu menu=new JMenu("�ļ�����");
		menuBar.add(menu);

		JMenuItem itemTopo=new JMenuItem("����topo_ctrl");
		itemTopo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
				dao.clearAll();
				
				ImportFileActions.openImportTopoCtrlAction();
			}
		});
		menu.add(itemTopo);
		
		JMenuItem itemConfig=new JMenuItem("����config_ctrl");
		itemConfig.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ImportFileActions.openImportConfigCtrlAction();
			}
		});
		menu.add(itemConfig);
		
		menu.addSeparator();
		
		JMenuItem itemAdc=new JMenuItem("����ICD");
		itemAdc.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
				//dao.clearAll();
				
				ImportFileActions.openImportADCAction();
			}
		});
		menu.add(itemAdc);
		
		/*
		menu.addSeparator();
		
		JMenuItem itemReset=new JMenuItem("��������豸");
		itemReset.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
					dao.clearAll();
				}
	      });
		menu.add(itemReset);
		*/
		
		return menuBar;
	}
	
	private static ActionListener getPanelActionListener(Class<?> panelClass){
		ActionListener listener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigContext.topoView=null;
				
				Object obj = null;
				try{
					obj=panelClass.newInstance();
				}catch(Exception ex){
					ex.printStackTrace();
					JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "����: "+ex.getMessage());
					return;
				}
				
				ConfigContext.rightPanel.removeAll();
				ConfigContext.rightPanel.add((JPanel)obj);
				
				ConfigContext.rightPanel.validate();
			}
		 };
		 return listener;
	}

	
	
}
