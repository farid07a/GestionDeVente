package ui.autosuggestextefield;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
// استيراد الكلاس المخصص للـ ScrollBar الخاص بـ Windows 11

public class ModernPopupMenu extends JPopupMenu {

    private final JPanel container;
    private final JScrollPane scrollPane;

    private final Color NORMAL = Color.WHITE;
    private final Color HOVER = new Color(235, 244, 255);
    private final Color PRESSED = new Color(215, 233, 255);

    public ModernPopupMenu() {

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        scrollPane = new JScrollPane(container);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        scrollPane.setPreferredSize(new Dimension(270, 300));

        // التعديل هنا: تطبيق تصميم Windows 11 الجديد للـ ScrollBar العمودي والأفقي
        scrollPane.getVerticalScrollBar().setUI(new ScrollBarWin11UI());
        scrollPane.getHorizontalScrollBar().setUI(new ScrollBarWin11UI());
        
        // قمنا بإلغاء السطر القديم: ScrollDesign.apply(scrollPane);

        add(scrollPane, BorderLayout.CENTER);
    }

    public JMenuItem addMenuItem(String text) {

    JMenuItem item = new JMenuItem(text);

    // 1. تعديل العرض (Width) والارتفاع هنا:
    // يمكنك زيادة الـ 300 إلى 350 مثلاً إذا أردت عرضاً أكبر يتناسق مع الخط الجديد
    int itemWidth = 320; 
    item.setPreferredSize(new Dimension(itemWidth, 48));
    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

    item.setOpaque(true);
    item.setFocusPainted(false);
    item.setBorderPainted(false);

    item.setBackground(NORMAL);
    item.setForeground(new Color(40, 40, 40));

    // 2. تعديل الخط ليصبح Cairo Bold وحجمه 16
    item.setFont(new Font("Cairo", Font.BOLD, 18));

    item.setHorizontalAlignment(SwingConstants.RIGHT);
    item.setHorizontalTextPosition(SwingConstants.RIGHT);
    item.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    item.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // تعديل الحواشي الداخلية لتتناسب مع الحجم الجديد
    item.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

    // 3. التحكم في لون خلفية العنصر المحدد (Hover / Pressed)
    // يمكنك تغيير الألوان هنا مباشرة (HOVER و PRESSED) حسب رغبتك
    item.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            item.setBackground(HOVER); // اللون عند مرور الماوس (Selection)
        }

        @Override
        public void mouseExited(MouseEvent e) {
            item.setBackground(NORMAL); // العودة للون الطبيعي عند خروج الماوس
        }

        @Override
        public void mousePressed(MouseEvent e) {
            item.setBackground(PRESSED); // اللون عند الضغط بالماوس
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            item.setBackground(HOVER);
        }
    });

    container.add(item);
    container.revalidate();
    container.repaint();

    return item;
}
    public void removeAllItems() {
        container.removeAll();
        container.revalidate();
        container.repaint();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JPanel getContainer() {
        return container;
    }
}