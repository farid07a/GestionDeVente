/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.menufr;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TopNavigationBar extends JPanel {

    private final List<NavItem> items = new ArrayList<>();
    private NavItem activeItem = null;
    
    // الألوان كـ Attributes قابلة للتعديل من خصائص NetBeans
    private Color primaryColor = new Color(37, 99, 235);     // لون الزر النشط بالكامل (أزرق ملكي)
    private Color activeTextColor = Color.WHITE;             // لون نص الزر النشط
    private Color textColor = new Color(71, 85, 105);        // لون النص العادي (رمادي)
    private Color hoverBgColor = new Color(226, 232, 240);   // لون الـ Focus والـ Hover الغامق والواضح (Gray 200)
    private Font menuFont = new Font("Tahoma", Font.BOLD, 13);

    public TopNavigationBar() {
        // ترتيب العناصر من اليمين لليسار متوافق مع الواجهة العربية
        setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 8)); 
        setBackground(Color.WHITE); 
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)));
        setPreferredSize(new Dimension(800, 65)); // ارتفاع مريح ومناسب للتلوين الكامل
    }

    /**
     * دالة إضافة العناصر المعتادة
     */
    public void addMenuItem(String text, ImageIcon icon, Runnable action) {
        NavItem item = new NavItem(text, icon, action);
        items.add(item);
        add(item);
        if (items.size() == 1) {
            setActiveItem(item);
        }
    }

    private void setActiveItem(NavItem item) {
        if (activeItem != null) {
            activeItem.setActive(false);
        }
        activeItem = item;
        activeItem.setActive(true);
        repaint();
    }

    // =========================================================================
    // الـ Getters والـ Setters لكي تظهر الألوان في لوحة Properties الخاصة بـ NetBeans
    // =========================================================================
    
    public Color getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(Color primaryColor) { 
        this.primaryColor = primaryColor; 
        repaint();
    }

    public Color getActiveTextColor() { return activeTextColor; }
    public void setActiveTextColor(Color activeTextColor) { 
        this.activeTextColor = activeTextColor; 
        repaint();
    }

    public Color getTextColor() { return textColor; }
    public void setTextColor(Color textColor) { 
        this.textColor = textColor; 
        repaint();
    }

    public Color getHoverBgColor() { return hoverBgColor; }
    public void setHoverBgColor(Color hoverBgColor) { 
        this.hoverBgColor = hoverBgColor; 
        repaint();
    }

    public Font getMenuFont() { return menuFont; }
    public void setMenuFont(Font menuFont) { 
        this.menuFont = menuFont; 
        repaint();
    }

    /**
     * كلاس داخلي للتحكم في رسم الزر بالكامل مع التلوين الحديث
     */
    private class NavItem extends JPanel {
        private final JLabel label;
        private final Runnable action;
        private boolean isActive = false;
        private boolean isHovered = false;

        public NavItem(String text, ImageIcon icon, Runnable action) {
            this.action = action;
            setOpaque(false); // لجعل الحواف الدائرية تظهر بشكل نظيف
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
            setPreferredSize(new Dimension(140, 48)); // أبعاد الزر الملون

            label = new JLabel(text, icon, JLabel.RIGHT);
            label.setFont(menuFont);
            label.setForeground(textColor);
            label.setIconTextGap(8);
            label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            
            add(label, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    setActiveItem(NavItem.this);
                    if (action != null) action.run();
                }
            });
        }

        public void setActive(boolean active) {
            this.isActive = active;
            label.setForeground(active ? activeTextColor : textColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            // تفعيل التنعيم للحواف الدائرية (Anti-Aliasing)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isActive) {
                // تلوين الزر بالكامل باللون الأساسي للبراند مع حواف دائرية ناعمة جداً
                g2.setColor(primaryColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            } else if (isHovered) {
                // تلوين خلفية الـ Focus/Hover بلون رمادي أدكن وواضح (Solid وممتلئ)
                g2.setColor(hoverBgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }

            g2.dispose();
        }
    }
}