package material.design;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class buttonMenu extends JButton {

    // =========================
    // Ripple
    // =========================
    private Animator animator;
    private int targetSize;
    private float animatSize;
    private Point pressedPoint;
    private float alpha;

    private Color effectColor = new Color(255, 255, 255);

    // =========================
    // Popup & Panel
    // =========================
    private JPopupMenu popupMenu;
    private JPanel menuPanel;

    public buttonMenu() {

        setFont(new Font("Tahoma", Font.BOLD, 13));
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // =========================
        // Popup
        // =========================
        createPopupMenu();

        // =========================
        // Resize icon
        // =========================
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeIcon();
            }
        });

        // =========================
        // Mouse
        // =========================
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {

                // Ripple
                targetSize = Math.max(getWidth(), getHeight()) * 2;
                animatSize = 0;
                pressedPoint = me.getPoint();
                alpha = 0.5f;

                if (animator.isRunning()) {
                    animator.stop();
                }

                animator.start();

                // Popup
                SwingUtilities.invokeLater(() -> {
                    showPopup();
                });
            }
        });

        // =========================
        // Animator
        // =========================
        TimingTarget target = new TimingTargetAdapter() {

            @Override
            public void timingEvent(float fraction) {

                if (fraction > 0.5f) {
                    alpha = 1 - fraction;
                }

                animatSize = fraction * targetSize;

                repaint();
            }
        };

        animator = new Animator(700, target);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0);

        setHorizontalTextPosition(SwingConstants.LEADING);
    }

    // ============================================================
    // إنشاء Popup بنفس الـ ScrollBarCustom الموجود بالـ Combobox
    // ============================================================
    private void createPopupMenu() {
        popupMenu = new JPopupMenu();
        popupMenu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        popupMenu.setBackground(Color.WHITE);
        popupMenu.setOpaque(true);
        popupMenu.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // البانل الداخلي للايتمز
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 1));
        menuPanel.setBackground(Color.WHITE);

        // إعداد JScrollPane مع ScrollBarCustom تماماً مثل ComboboxRoundNew
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ربط ScrollBarCustom نفس المستعمل في الـ Combobox
        ScrollBarCustom sb = new ScrollBarCustom();
        sb.setUnitIncrement(30);
        sb.setForeground(new Color(180, 180, 180));
        scrollPane.setVerticalScrollBar(sb);

        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(scrollPane, BorderLayout.CENTER);
    } 

    // ============================================================
    // حذف جميع Items
    // ============================================================
    public void removePopupItems() {
        menuPanel.removeAll();
    }

    // ============================================================
    // إظهار Popup
    // ============================================================
    private void showPopup() {
        int itemCount = menuPanel.getComponentCount();
        if (itemCount == 0) {
            return;
        }

        int popupWidth = getWidth();
        int itemHeight = 39; // نفس ارتفاع عناصر الكومبوبوكس
        
        int totalHeight = itemCount * itemHeight + 4;
        int maxHeight = 210; // يظهر السكرول بار إذا جاوزت القائمة هذا الارتفاع
        int finalHeight = Math.min(totalHeight, maxHeight);

        for (Component c : menuPanel.getComponents()) {
            c.setPreferredSize(new Dimension(popupWidth, itemHeight));
            c.setBackground(Color.WHITE);
        }

        popupMenu.setPopupSize(popupWidth, finalHeight);
        popupMenu.show(this, 0, getHeight() + 2);
    }

    // ============================================================
    // Popup Item
    // ============================================================
    private class PopupItem extends JButton {

        private boolean mouseOver = false;

        public PopupItem(String text) {
            super(text);

            setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            setHorizontalAlignment(SwingConstants.RIGHT);

            setFont(new Font("Time New Romen", Font.BOLD, 15));
            setForeground(Color.BLACK);

            setBackground(Color.WHITE);
            setOpaque(true);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);

            setBorder(new EmptyBorder(4, 8, 4, 8));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseOver = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseOver = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // نفس لون الهوفر بالكومبوبوكس (240, 242, 245)
            if (mouseOver) {
                g2.setColor(new  Color(76, 204, 255));//Color(240, 242, 245));
            } else {
                g2.setColor(Color.WHITE);
            }
            
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ============================================================
    // Button Paint
    // ============================================================
    @Override
    protected void paintComponent(Graphics grphcs) {

        int width = getWidth();
        int height = getHeight();

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, 15, 15);

        if (pressedPoint != null) {
            g2.setColor(effectColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g2.fillOval(
                    (int) (pressedPoint.x - animatSize / 2),
                    (int) (pressedPoint.y - animatSize / 2),
                    (int) animatSize,
                    (int) animatSize
            );
        }

        g2.dispose();
        grphcs.drawImage(img, 0, 0, null);

        super.paintComponent(grphcs);
    }

    // ============================================================
    // Icon Resize
    // ============================================================
    private ImageIcon originalIcon;

    @Override
    public void setIcon(Icon icon) {
        if (icon instanceof ImageIcon) {
            originalIcon = (ImageIcon) icon;
            if (getWidth() > 0 && getHeight() > 0) {
                resizeIcon();
            } else {
                super.setIcon(icon);
            }
        } else {
            super.setIcon(icon);
        }
    }

    private void resizeIcon() {
        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0 || originalIcon == null) {
            return;
        }

        int size = (int) (Math.min(width, height) * 0.80);
        Image img = originalIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        super.setIcon(new ImageIcon(img));
    }

    // ============================================================
    // Effect Color
    // ============================================================
    public Color getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
    }
    
    public void addPopupItem(String text, ActionListener action) {
        PopupItem item = new PopupItem(text);

        item.addActionListener(e -> {
            if (action != null) {
                action.actionPerformed(e);
            }
            popupMenu.setVisible(false);
        });

        menuPanel.add(item);
    }
}