/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.card;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.text.*;

public class TextFieldPrice extends JTextField {

    private int radius = 14;

    private final Color borderColor = new Color(220, 224, 230);
    private final Color focusColor = new Color(37, 99, 235);
    private final Color backgroundColor = Color.WHITE;

    private final DecimalFormat formatter
            = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));

    public TextFieldPrice() {

        setOpaque(false);
        setBackground(backgroundColor);
        setForeground(new Color(52, 58, 64));
        setCaretColor(new Color(52, 58, 64));
        setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        ((AbstractDocument) getDocument()).setDocumentFilter(new NumberFilter());

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

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatText();
            }
        });

        addActionListener(e -> {
            formatText();
            transferFocus();
        });
    }

    private void formatText() {

        String text = getText().replace(",", "").trim();

        if (text.isEmpty()) {
            return;
        }

        try {
            double value = Double.parseDouble(text);
            setText(formatter.format(value));
        } catch (NumberFormatException ex) {
        }
    }

    public double getDoubleValue() {

        try {

            String text = getText().replace(",", "").trim();

            if (text.isEmpty()) {
                return 0;
            }

            return Double.parseDouble(text);

        } catch (Exception e) {
            return 0;
        }

    }

    private class NumberFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset,
                String string, AttributeSet attr)
                throws BadLocationException {

            replace(fb, offset, 0, string, attr);

        }

        @Override
        public void replace(FilterBypass fb,
                int offset,
                int length,
                String text,
                AttributeSet attrs)
                throws BadLocationException {

            if (text == null) {
                text = "";
            }

            Document doc = fb.getDocument();

            String oldText = doc.getText(0, doc.getLength());

            String newText = oldText.substring(0, offset)
                    + text
                    + oldText.substring(offset + length);

            newText = newText.replace(",", "");

            if (newText.isEmpty()) {

                fb.replace(offset, length, text, attrs);
                return;

            }

            if (!newText.matches("\\d*(\\.\\d*)?")) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            fb.replace(offset, length, text, attrs);

        }

        @Override
        public void remove(FilterBypass fb,
                int offset,
                int length)
                throws BadLocationException {

            fb.remove(offset, length);

        }

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0,
                getWidth() - 1,
                getHeight() - 1,
                radius,
                radius);

        super.paintComponent(g);

        g2.setColor(isFocusOwner() || !getText().trim().isEmpty()
                ? focusColor
                : borderColor);

        g2.setStroke(new BasicStroke(1.5f));

        g2.drawRoundRect(1,
                1,
                getWidth() - 3,
                getHeight() - 3,
                radius,
                radius);

        g2.dispose();

    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

}
