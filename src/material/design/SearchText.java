package material.design;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SearchText extends JTextField {
 
    
    public SearchText() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setSelectionColor(new Color(220, 204, 182));
       // setHorizontalAlignment( CENTER);
    }
    private final String hint = "بـحـث . . .";

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (getText().length() == 0) {
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
            g.setColor(new Color(c2, true));
            g.drawString(hint, getWidth()/2, h / 2 + fm.getAscent() / 2 - 2);
        }
        createLineStyle(g2);
        g2.dispose();
    }
    
      public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
      public Color getLineColor() {
        return lineColor;
    }
     private float location;
 
    private boolean show;

    private Color lineColor = new Color(3, 155, 216);
    private void createLineStyle(Graphics2D g2) {
        if (isFocusOwner() ) {
            double width = getWidth() - 4;
            int height = getHeight();
            g2.setColor(lineColor);
            double size;
            if (show) {
                size = width * (1 - location);
                
            } else {
                size = width * location;
                
            }
            double x = (width - size) / 2;
            g2.fillRect((int) (x + 2), height - 2, (int) size, 2);
        }
    }
    
}
