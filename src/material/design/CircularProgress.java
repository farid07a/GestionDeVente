/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package material.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class CircularProgress extends JPanel {

    private int progress = 72;

    public void setProgress(int progress) {
        this.progress = progress;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 20;

        // الخلفية
        g2.setStroke(new BasicStroke(10));
        g2.setColor(new Color(230,230,230));
        g2.drawArc(10,10,size,size,0,360);

        // التقدم
        g2.setColor(new Color(52,152,219));
        g2.drawArc(10,10,size,size,90,-(int)(360*progress/100.0));

        // النص
        String text = progress + "%";
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth()-fm.stringWidth(text))/2;
        int y = (getHeight()+fm.getAscent())/2-5;

        g2.drawString(text, x, y);
    }
}
