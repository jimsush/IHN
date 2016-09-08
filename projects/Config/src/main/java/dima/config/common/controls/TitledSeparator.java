package dima.config.common.controls;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;


/**
 * 
 * <p>
 * Title: TitledSeparator
 * </p>
 */
public class TitledSeparator extends JPanel {

    private static final long serialVersionUID = 2015477187754778821L;

    private JSeparator separator = new JSeparator();
    
    /**
     * Label
     */
    private JLabel titleLabel;

    public TitledSeparator(String title) {
        this(title, 0);
    }
    
    public TitledSeparator(String title,int topGap) {
        titleLabel = new JLabel(title);
        String titleStr = title;
        Font font = UIManager.getFont("Label.font");
        FontMetrics fontMetrics = titleLabel.getFontMetrics(font);
        int stringWidth = fontMetrics.stringWidth(titleStr);
        LayoutManager tableLayout = new TableLayout(new double[][] {
                { 8,stringWidth + 10, TableLayout.FILL }, { topGap,18 } });
        setLayout(tableLayout);
        add(titleLabel, "1,1,f,c");
        add(separator, "2,1,f,c");
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.titleLabel.setEnabled(enabled);
        this.separator.setEnabled(enabled);
    }
}
