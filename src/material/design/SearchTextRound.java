/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package material.design;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SearchTextRound extends JTextField {

    private int radius = 20; // درجة دائرية الحواف
    private final Color borderColor = new Color(220, 224, 230); // رمادي فاتح
    private final Color focusColor = new Color(37, 99, 235);   // أزرق عند التركيز
    private final Color backgroundColor = Color.WHITE;
    private final String hint = "بـحـث . . .";
    
    // يمكنك تمرير أيقونة مخصصة هنا، أو سيقوم الكود برسم أيقونة بحث افتراضية
    private Icon searchIcon; 

    public SearchTextRound() {
        setOpaque(false);
        setBackground(backgroundColor);
        setForeground(new Color(52, 58, 64));
        setCaretColor(new Color(52, 58, 64));
        setSelectionColor(new Color(220, 204, 182));

        // محاذاة النص لليمين بما أنه بحث باللغة العربية
        setHorizontalAlignment(JTextField.RIGHT);

        /*
         * إضافة هوامش داخلية (Padding):
         * أعلى: 6، يمين: 15، أسفل: 6
         * يسار: 35 (تركنا مساحة أكبر 35 بكسل في الجهة اليسرى لوضع أيقونة البحث هناك)
         */
        setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 35));

        // إعادة الرسم مباشرة عند الكتابة أو الحذف لضبط حالة الإطار والنص التلميحي
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { repaint(); }
            @Override
            public void removeUpdate(DocumentEvent e) { repaint(); }
            @Override
            public void changedUpdate(DocumentEvent e) { repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. رسم الخلفية الدائرية
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // السماح لـ Swing برسم النص الأساسي والمؤشر فوق الخلفية التي رسمناها
        super.paintComponent(g);

        // 2. رسم نص التلميح (Hint) إذا كان الحقل فارغاً
        if (getText().length() == 0) {
            FontMetrics fm = g2.getFontMetrics();
            int h = getHeight();
            
            // دمج لوني الخلفية والأمامية لإنتاج لون رمادي شفاف وناعم للنص التلميحي
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
            g2.setColor(new Color(c2, true));

            // رسم النص محاذياً لليمين مع مراعاة الهامش (15 بكسل)
            int textX = getWidth() - fm.stringWidth(hint) - 15;
            int textY = h / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(hint, textX, textY);
        }

        // 3. رسم أيقونة البحث في الجهة اليسرى
        int iconSize = 16;
        int iconX = 12; // البعد عن الحافة اليسرى
        int iconY = (getHeight() - iconSize) / 2; // لتوسيط الأيقونة عمودياً

        if (searchIcon != null) {
            // إذا قمت بتعيين أيقونة خاصة بك من الخارج
            searchIcon.paintIcon(this, g2, iconX, iconY);
        } else {
            // رسم أيقونة بحث (عدسة مكبرة) افتراضية جذابة عبر الـ Graphics
            g2.setColor(new Color(150, 150, 150));
            g2.setStroke(new BasicStroke(2.0f));
            // رسم دائرة العدسة
            g2.drawOval(iconX, iconY, 10, 10);
            // رسم عصا العدسة
            g2.drawLine(iconX + 8, iconY + 8, iconX + 14, iconY + 14);
        }

        // 4. رسم الإطار الخارجي الدائري (يتغير لونه عند التركيز أو الكتابة)
        if (isFocusOwner() || !getText().trim().isEmpty()) {
            g2.setColor(focusColor);
        } else {
            g2.setColor(borderColor);
        }

        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);

        g2.dispose();
    }

    //==========================
    // Getters & Setters
    //==========================

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public Icon getSearchIcon() {
        return searchIcon;
    }

    /**
     * يمكنك استخدام هذه الدالة لتغيير أيقونة البحث الافتراضية بأيقونة PNG أو SVG خاصة بمشروعك
     */
    public void setSearchIcon(Icon searchIcon) {
        this.searchIcon = searchIcon;
        repaint();
    }
}