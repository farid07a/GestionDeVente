package ui.card;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextFieldRound extends JTextField {

    // Radius
    private int radius = 18;

    // Colors
    private final Color borderColor = new Color(220, 224, 230);   // #DCE0E6
    private final Color focusColor = new Color(37, 99, 235);       // #2563EB
    private final Color backgroundColor = Color.WHITE;

    public TextFieldRound() {

        setOpaque(false);
        setBackground(backgroundColor);
        setForeground(new Color(52, 58, 64));
        setCaretColor(new Color(52, 58, 64));

        // Padding
//setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 12));
        // تحديث اللون مباشرة عند الكتابة أو الحذف
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                radius,
                radius);

        super.paintComponent(g);

        // Border Color
        if (isFocusOwner() || !getText().trim().isEmpty()) {
            g2.setColor(focusColor);
        } else {
            g2.setColor(borderColor);
        }

        // Border Width
        g2.setStroke(new BasicStroke(1.2f));

        g2.drawRoundRect(
                1,
                1,
                getWidth() - 3,
                getHeight() - 3,
                radius,
                radius);

        g2.dispose();
    }

    //==========================
    // Getter & Setter
    //==========================

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
}