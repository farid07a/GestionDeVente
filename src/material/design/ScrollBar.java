package material.design;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBar extends JScrollBar {

    public ScrollBar() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(10, 5));
        setBackground(new Color(242, 242, 242));
        setUnitIncrement(20);
        //setCursor(new Cursor(Cursor.HAND_CURSOR));
        
    }
  
    
}
