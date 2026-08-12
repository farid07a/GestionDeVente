/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.card;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import javax.swing.JTextField;

public class TextFieldPriceV2 extends JTextField {

    private int radius = 14;

    private final Color borderColor = new Color(220, 224, 230);
    private final Color focusColor = new Color(37, 99, 235);
    private final Color backgroundColor = Color.WHITE;

    private final DecimalFormat formatter =
            new DecimalFormat("#,##0.00",
                    new DecimalFormatSymbols(Locale.US));

    private boolean formatting = false;

    public TextFieldPriceV2() {

        setOpaque(false);
        setBackground(backgroundColor);
        setForeground(new Color(52, 58, 64));
        setCaretColor(new Color(52, 58, 64));
        setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        ((AbstractDocument) getDocument())
                .setDocumentFilter(new NumberFilter());

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
            public void focusGained(FocusEvent e) {
                formatNow();
            }

            @Override
            public void focusLost(FocusEvent e) {
                formatNow();
            }

        });

    }

    public double getDoubleValue() {

        try {

            String value = getText().replace(",", "");

            if (value.isEmpty()) {
                return 0;
            }

            return Double.parseDouble(value);

        } catch (Exception ex) {
            return 0;
        }

    }

    private void formatNow() {

        if (formatting) {
            return;
        }

        formatting = true;

        try {

            String text = getText().replace(",", "");

            if (text.isEmpty()) {
                formatting = false;
                return;
            }

            if (text.equals(".")) {
                formatting = false;
                return;
            }

            double value = Double.parseDouble(text);

            int caret = getCaretPosition();

            String formatted = formatter.format(value);

            setText(formatted);

            if (caret > formatted.length()) {
                caret = formatted.length();
            }

            final int pos = caret;

            SwingUtilities.invokeLater(() -> {

                try {
                    setCaretPosition(pos);
                } catch (Exception ex) {
                }

            });

        } catch (Exception ex) {

        }

        formatting = false;

    }

    private class NumberFilter extends DocumentFilter {
        
            @Override
        public void insertString(FilterBypass fb,
                int offset,
                String string,
                AttributeSet attr)
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

            if (formatting) {
                fb.replace(offset, length, text, attrs);
                return;
            }

            if (text == null) {
                text = "";
            }

            String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());

            String newText = oldText.substring(0, offset)
                    + text
                    + oldText.substring(offset + length);

            newText = newText.replace(",", "");

            if (newText.isEmpty()) {
                fb.replace(0, fb.getDocument().getLength(), "", attrs);
                return;
            }

            if (!newText.matches("\\d*(\\.\\d{0,2})?")) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            if (newText.equals(".")) {
                fb.replace(0, fb.getDocument().getLength(), ".", attrs);
                return;
            }

            try {

                int digitsAfterCaret = oldText.length() - offset;

                double value = Double.parseDouble(newText);

                String formatted = formatter.format(value);

                formatting = true;

                fb.replace(0,
                        fb.getDocument().getLength(),
                        formatted,
                        attrs);

                formatting = false;

                int caret = formatted.length() - digitsAfterCaret;

                if (caret < 0) {
                    caret = 0;
                }

                if (caret > formatted.length()) {
                    caret = formatted.length();
                }

                final int pos = caret;

                SwingUtilities.invokeLater(() -> {

                    try {
                        setCaretPosition(pos);
                    } catch (Exception ex) {
                    }

                });

            } catch (NumberFormatException ex) {

                formatting = false;

            }

        }

        @Override
        public void remove(FilterBypass fb,
                int offset,
                int length)
                throws BadLocationException {

            replace(fb, offset, length, "", null);

        }

    }
}