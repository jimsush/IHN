package dima.config.view;

import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;

public class TitlePane extends JPanel {

	private static final long serialVersionUID = 2651842266885860545L;
	
	private String title;
	private String tips;
	
	private JLabel titleLabel;
	private JLabel tipsLabel;
	
	public TitlePane(String title){
		this.title=title;
		
		LayoutManager layout =  new TableLayout(new double[][] {
	        { 10,  TableLayout.FILL, 10 }, 
	        ConfigUtils.getTableLayoutRowParam(2, 4) });
		this.setLayout(layout);
		
		titleLabel=new JLabel("<html><b>"+this.title+"</b></html>");
		add(titleLabel, "1,1,f,0");
		
		tipsLabel=new JLabel(title);
		add(tipsLabel, "1,3,f,0");
	}
	
	public void setTips(String tips){
		this.tips=tips;
		tipsLabel.setText(this.tips);
	}
	
	
}
