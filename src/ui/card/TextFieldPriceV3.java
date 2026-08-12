/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.card;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class TextFieldPriceV3 extends JTextField {

    private int radius = 14;

    private final Color borderColor = new Color(220, 224, 230);
    private final Color focusColor = new Color(37, 99, 235);
    private final Color backgroundColor = Color.WHITE;

    // الفلتر النهائي عند خروج التركيز (يضيف الفاصلة والكسر العشري .00 دائماً)
    private final DecimalFormat finalFormatter =
            new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));

    // فلتر الأرقام الصحيحة أثناء الكتابة
    private final DecimalFormat liveFormatter = 
            new DecimalFormat("#,##0", new DecimalFormatSymbols(Locale.US));

    private boolean isFormatting = false; // حماية لمنع الحلقة اللانهائية أثناء التحديث المباشر

    public TextFieldPriceV3() {
        setOpaque(false);
        setBackground(backgroundColor);
        setForeground(new Color(52, 58, 64));
        setCaretColor(new Color(52, 58, 64));
        setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        ((AbstractDocument) getDocument()).setDocumentFilter(new NumberFilter());

        // التنسيق المباشر أثناء الكتابة
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyLiveFormatting();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyLiveFormatting();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyLiveFormatting();
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatTextOnFocusLost();
            }
        });
    }

    // دالة التنسيق اللحظي أثناء الضغط على الأرقام
    private void applyLiveFormatting() {
        SwingUtilities.invokeLater(() -> {
            if (isFormatting) return;

            isFormatting = true;
            try {
                String originalText = getText();
                String cleanText = originalText.replace(",", "").trim();

                if (!cleanText.isEmpty()) {
                    int caretPos = getCaretPosition();
                    
                    // حساب عدد الأرقام الحقيقية (بدون الفواصل) قبل مكان المؤشر الحالي
                    int digitsBeforeCaret = 0;
                    for (int i = 0; i < caretPos && i < originalText.length(); i++) {
                        if (originalText.charAt(i) != ',') {
                            digitsBeforeCaret++;
                        }
                    }

                    // فصل الجزء الصحيح عن الجزء العشري حتى لا يتم إجبار المستخدم على الأصفار أثناء الكتابة
                    String intPart = cleanText;
                    String decPart = "";
                    if (cleanText.contains(".")) {
                        int dotIndex = cleanText.indexOf(".");
                        intPart = cleanText.substring(0, dotIndex);
                        decPart = cleanText.substring(dotIndex); // يشمل النقطة وما بعدها
                    }

                    String formattedInt = "";
                    if (!intPart.isEmpty()) {
                        double val = Double.parseDouble(intPart);
                        formattedInt = liveFormatter.format(val);
                    } else if (cleanText.startsWith(".")) {
                        formattedInt = "0"; // لو بدأ بنقطة يكتب تلقائياً 0.
                    }

                    String newText = formattedInt + decPart;
                    setText(newText);

                    // إعادة وضع مؤشر الكتابة (Caret) في مكانه الصحيح بدقة بعد إضافة الفواصل الجديدة
                    int newCaretPos = 0;
                    int digitsCount = 0;
                    while (newCaretPos < newText.length() && digitsCount < digitsBeforeCaret) {
                        if (newText.charAt(newCaretPos) != ',') {
                            digitsCount++;
                        }
                        newCaretPos++;
                    }
                    setCaretPosition(Math.min(newCaretPos, newText.length()));
                }
            } catch (Exception ex) {
                // تجنب الأخطاء في حالة الإدخالات غير المتوقعة
            } finally {
                isFormatting = false;
                repaint();
            }
        });
    }

    // التنسيق النهائي بإضافة خانتين عشريتين عند الخروج من الحقل
    private void formatTextOnFocusLost() {
        String text = getText().replace(",", "").trim();
        if (text.isEmpty()) return;

        try {
            double value = Double.parseDouble(text);
            isFormatting = true; // منع الـ DocumentListener من التداخل
            setText(finalFormatter.format(value));
        } catch (NumberFormatException ex) {
            // كتم الخطأ
        } finally {
            isFormatting = false;
        }
    }

    public double getDoubleValue() {
        try {
            String text = getText().replace(",", "").trim();
            if (text.isEmpty()) return 0;
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    private class NumberFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) text = "";

            Document doc = fb.getDocument();
            String oldText = doc.getText(0, doc.getLength());
            String newText = oldText.substring(0, offset) + text + oldText.substring(offset + length);
            
            newText = newText.replace(",", "");

            if (newText.isEmpty()) {
                fb.replace(offset, length, text, attrs);
                return;
            }

            // تحديث التعبير النمطي ليقبل رقمين عشريين كحد أقصى أثناء الكتابة (أفضل لتنسيق العملات)
            if (!newText.matches("\\d*(\\.\\d{0,2})?")) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            fb.replace(offset, length, text, attrs);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            fb.remove(offset, length);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        super.paintComponent(g);

        g2.setColor(isFocusOwner() || !getText().trim().isEmpty() ? focusColor : borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
        g2.dispose();
    }

    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; repaint(); }
}