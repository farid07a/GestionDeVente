package material.design;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JCheckBox;

public class JCheckBoxCustom extends JCheckBox {

    private final int border = 4;

    public JCheckBoxCustom() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setBackground(new Color(69, 124, 235));
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int ly = (getHeight() - 16) / 2;
        if (isSelected()) {
            if (isEnabled()) {
                g2.setColor(getBackground());
            } else {
                g2.setColor(Color.black);
            }
            g2.fillRoundRect(getWidth()-16, ly, 16, 16, border, border);
            //  Draw Check icon
            int px[] = {4+getWidth()-18, 8+getWidth()-18, 14+getWidth()-18, 12+getWidth()-18, 8+getWidth()-18, 6+getWidth()-18};
            int py[] = {ly + 8, ly + 14, ly + 5, ly + 3, ly + 10, ly + 6};
            g2.setColor(Color.WHITE);
            g2.fillPolygon(px, py, px.length);
        } else {
            g2.setColor(Color.black);
            g2.fillRoundRect(getWidth()-16, ly, 16, 16, border, border);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(getWidth()-16, ly + 1, 14, 14, border, border);
        }
        g2.dispose();
    }
}
