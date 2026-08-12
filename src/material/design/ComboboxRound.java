package material.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class ComboboxRound<E> extends JComboBox<E> {

    private int radius = 18;
    private final Color borderColor = new Color(220, 224, 230);
    private final Color focusColor = new Color(37, 99, 235);
    private boolean mouseOver;

    public ComboboxRound() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        setOpaque(false);
        setFocusable(true);
        setUI(new ComboUI(this));

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
                Component com = super.getListCellRendererComponent(jlist, o, i, bln, bln1);
                if (bln) {
                    com.setBackground(new Color(240, 240, 240));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return com;
            }
        });
    }

    private class ComboUI extends BasicComboBoxUI {

        private final Animator animator;
        private boolean animateHinText = true;
        private float location;
        private boolean show;
        private ComboboxRound combo;

        public ComboUI(ComboboxRound combo) {
            this.combo = combo;
            setFont(new Font("Tahoma", Font.BOLD, 12));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent me) {
                    mouseOver = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    mouseOver = false;
                    repaint();
                }
            });
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent fe) {
                    showing(false);
                }

                @Override
                public void focusLost(FocusEvent fe) {
                    showing(true);
                }
            });
            addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent ie) {
                    if (!isFocusOwner()) {
                        if (getSelectedIndex() == -1) {
                            showing(true);
                        } else {
                            showing(false);
                        }
                    }
                }
            });
            addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                    arrowButton.setBackground(new Color(200, 200, 200));
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                    arrowButton.setBackground(new Color(150, 150, 150));
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent pme) {
                    arrowButton.setBackground(new Color(150, 150, 150));
                }
            });
            TimingTarget target = new TimingTargetAdapter() {
                @Override
                public void begin() {
                    animateHinText = getSelectedIndex() == -1;
                }

                @Override
                public void timingEvent(float fraction) {
                    location = fraction;
                    repaint();
                }
            };
            animator = new Animator(300, target);
            animator.setResolution(0);
            animator.setAcceleration(0.5f);
            animator.setDeceleration(0.5f);
        }

        @Override
        public void paintCurrentValueBackground(Graphics grphcs, Rectangle rctngl, boolean bln) {
            // نتركها فارغة لمنع الرسم الافتراضي للخلفية المستطيلة للمكون الحالي
        }

        @Override
        protected JButton createArrowButton() {
            return new ArrowButton();
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup pop = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {

                    JScrollPane scroll = new JScrollPane(list);

                    scroll.setBackground(Color.WHITE);

                    list.setFixedCellHeight(40);

                    ScrollBarCustom bar = new ScrollBarCustom();

                    bar.setUnitIncrement(40);

                    scroll.setVerticalScrollBar(bar);

                    scroll.setHorizontalScrollBarPolicy(
                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                    );

                    return scroll;
                }
            };
            pop.setBorder(new LineBorder(new Color(200, 200, 200), 1));
            return pop;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. إنشاء شكل الزوايا الدائرية
            Shape roundRect = new RoundRectangle2D.Float(0, 0, combo.getWidth(), combo.getHeight(), radius, radius);

            // 2. تفعيل الـ Clip لمنع أي رسم خارجي (هو ده اللي بيحل مشكلة خروج اللون الأبيض)
            g2.setClip(roundRect);

            // 3. رسم الخلفية البيضاء داخل الحدود الدائرية فقط
            g2.setColor(combo.getBackground());
            g2.fill(roundRect);

            // 4. رسم محتوى الـ Combobox الأساسي (النصوص والعناصر) مع احترام الـ Clip
            super.paint(g2, c);
            g2.dispose();

            // 5. رسم الـ Border الخارجي في النهاية بدون Clip عشان تطلع الحواف ناعمة جداً وسلسة (Anti-aliased)
            g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (combo.isFocusOwner()) {
                g2.setColor(focusColor);
                g2.setStroke(new BasicStroke(2f));
            } else {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
            }

            g2.drawRoundRect(1, 1, combo.getWidth() - 3, combo.getHeight() - 3, radius, radius);
            g2.dispose();
        }

        private void showing(boolean action) {
            if (animator.isRunning()) {
                animator.stop();
            } else {
                location = 1;
            }
            animator.setStartFraction(1f - location);
            show = action;
            location = 1f - location;
            animator.start();
        }

        private class ArrowButton extends JButton {

            public ArrowButton() {
                setContentAreaFilled(false);
                setBorder(new EmptyBorder(5, 5, 5, 5));
                setBackground(new Color(150, 150, 150));
            }

            @Override
            public void paint(Graphics grphcs) {
                super.paint(grphcs);
                Graphics2D g2 = (Graphics2D) grphcs;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                int size = 10;
                int x = (width - size) / 2;
                int y = (height - size) / 2 + 5;
                int px[] = {x, x + size, x + size / 2};
                int py[] = {y, y, y + size};
                g2.setColor(getBackground());
                g2.fillPolygon(px, py, px.length);
                g2.dispose();
            }
        }
    }
}
