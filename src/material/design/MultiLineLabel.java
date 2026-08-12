/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package material.design;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

/**
 *
 * @author farid
 */
//import javax.swing;
//import java.awt.*;

public class MultiLineLabel extends JLabel {

    public MultiLineLabel(String text) {
        super(text);
        setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        String[] lines = getText().split("\n");
        FontMetrics metrics = g2d.getFontMetrics();
        int y = 0;
        for (String line : lines) {
            g2d.drawString(line, getInsets().left, y + metrics.getAscent());
            y += metrics.getHeight();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        FontMetrics metrics = getFontMetrics(getFont());
        size.height = metrics.getHeight() * getLineCount();
        return size;
    }

    private int getLineCount() {
        String[] lines = getText().split("\n");
        return lines.length;
    }
}
