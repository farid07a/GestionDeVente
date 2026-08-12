/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package material.design;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JRadioButton;

public class RadioButtonCustomAR extends JRadioButton {

    public RadioButtonCustomAR() {
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(new Color(102,204,0));// 69, 124, 235)); [102,204,0]
       setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        setFont(new Font(getFont().getName(), Font.BOLD, 20));
        //setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

   
    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Adjust the vertical position for centering the radio button
        int ly = (getHeight() - 16) / 2;

        // Draw the radio button for selected state
        if (isSelected()) {
            if (isEnabled()) {
                g2.setColor(getBackground());
            } else {
                g2.setColor(Color.GRAY);
            }

            // Draw outer circle (radio button circle)
            g2.fillOval(getWidth() - 17, ly, 16, 16); // Move to right side

            // Draw inner white circle
            g2.setColor(Color.WHITE);
            g2.fillOval(getWidth() - 16, ly + 1, 14, 14);

            // Draw the inner selected dot
            if (isEnabled()) {
                g2.setColor(getBackground());
            } else {
                g2.setColor(Color.GRAY);
            }
            g2.fillOval(getWidth() - 13, ly + 4, 8, 8); // Adjust the position of the inner dot
        } else {
            // Draw the unselected radio button
            if (isFocusOwner()) {
                g2.setColor(getBackground());
            } else {
                g2.setColor(Color.GRAY);
            }

            // Draw outer circle (radio button circle)
            g2.fillOval(getWidth() - 17, ly, 16, 16); // Move to right side

            // Draw inner white circle
            g2.setColor(Color.WHITE);
            g2.fillOval(getWidth() - 16, ly + 1, 14, 14);
        }

        g2.dispose();
    }
}
