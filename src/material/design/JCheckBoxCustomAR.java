package material.design;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JCheckBox;

public class JCheckBoxCustomAR extends JCheckBox {

    private final int border = 4;

    public JCheckBoxCustomAR() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setBackground(new Color(69, 124, 235));
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Adjust the vertical position for centering the checkbox
        int ly = (getHeight() - 16) / 2;

        // When the checkbox is selected (checked)
        if (isSelected()) {
            if (isEnabled()) {
                g2.setColor(getBackground());
            } else {
                g2.setColor(Color.GRAY);
            }
            // Draw the checkbox background with rounded corners (selected state)
            g2.fillRoundRect(getWidth() - 17, ly, 16, 16, border, border);  // Move to the right side

            // Draw the white checkmark icon
            int px[] = {getWidth() - 13, getWidth() - 9, getWidth() - 3, getWidth() - 5, getWidth() - 9, getWidth() - 11};
            int py[] = {ly + 8, ly + 14, ly + 5, ly + 3, ly + 10, ly + 6};
            g2.setColor(Color.WHITE);
            g2.fillPolygon(px, py, px.length);
        } else {
            // Draw the checkbox background with rounded corners (unselected state)
            g2.setColor(Color.GRAY);
            g2.fillRoundRect(getWidth() - 17, ly, 16, 16, border, border); // Move to the right side

            // Draw the inner white square (unselected state)
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(getWidth() - 16, ly + 1, 14, 14, border, border);
        }

        g2.dispose();
    }
}
