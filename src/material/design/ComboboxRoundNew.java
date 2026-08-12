/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package material.design;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ComboboxRoundNew<E> extends JComboBox<E> {

    private int radius = 18;
    private final Color borderColor = new Color(220, 224, 230);
    private final Color focusColor = new Color(37, 99, 235);
    private boolean mouseOver;

    // --- الإضافات: النص والتسمية الجانبية وتعديل الارتفاع ---
    private String labelText = "";
    private int itemHeight = 40; // الارتفاع الافتراضي لعناصر القائمة المنسدلة

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        repaint();
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
        repaint();
    }

    public ComboboxRoundNew() {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(12, 12, 6, 12));
        setOpaque(false);
        setFocusable(true);

        // 1. استدعاء UI بدون تمرير parameters
        setUI(new ComboUI());

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
                Component com = super.getListCellRendererComponent(jlist, o, i, bln, bln1);
                setBorder(new EmptyBorder(4, 8, 4, 8));
                if (bln) {
                    com.setBackground(new Color(240, 242, 245));
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return com;
            }
        });
    }

    private class ComboUI extends BasicComboBoxUI {

        private final Animator animator;
        private boolean animateHinText = true;
        private float location;
        private boolean show;

        public ComboUI() {
            setFont(new Font("Time New Romen", Font.BOLD, 14));

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
                    arrowButton.setBackground(new Color(37, 99, 235));
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
            // نتركها فارغة لمنع خلفية المستطيل الافتراضية
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
                    list.setFixedCellHeight(30);
                    JScrollPane scroll = new JScrollPane(list);
                    scroll.setBackground(Color.WHITE);
                    ScrollBarCustom sb = new ScrollBarCustom();
                    sb.setUnitIncrement(30);
                    sb.setForeground(new Color(180, 180, 180));
                    scroll.setVerticalScrollBar(sb);
                    return scroll;
                }
            };
            pop.setBorder(new LineBorder(new Color(200, 200, 200), 1));
            return pop;
        }

//        @Override
//        protected ComboPopup createPopup() {
//            BasicComboPopup pop = new BasicComboPopup(comboBox) {
//                @Override
//                protected JScrollPane createScroller() {
//                    // الوصول لارتفاع العناصر عبر الكلاس الخارجي مباشرة
//                    list.setFixedCellHeight(ComboboxRoundNew.this.getItemHeight());
//
//                    JScrollPane scroll = new JScrollPane(list);
//                    scroll.setBackground(Color.WHITE);
//                    return scroll;
//                }
//            };
//            pop.setBorder(new LineBorder(new Color(220, 224, 230), 1));
//            return pop;
//        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            // 1. خلفية الحواف الدائرية
            Shape roundRect = new RoundRectangle2D.Float(0, 0, ComboboxRoundNew.this.getWidth(), ComboboxRoundNew.this.getHeight(), radius, radius);

            g2.setClip(roundRect);
            g2.setColor(ComboboxRoundNew.this.getBackground());
            g2.fill(roundRect);

            // 2. رسم العناصر
            super.paint(g2, c);
            g2.dispose();

            // 3. رسم LabelText جهة اليمين
            g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            createLabelTextRight(g2);

            // 4. رسم الـ Border الخارجي
            if (ComboboxRoundNew.this.isFocusOwner()) {
                g2.setColor(focusColor);
                g2.setStroke(new BasicStroke(1.2f));
            } else {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1f));
            }

            g2.drawRoundRect(1, 1, ComboboxRoundNew.this.getWidth() - 3, ComboboxRoundNew.this.getHeight() - 3, radius, radius);
            g2.dispose();
        }

        // --- رسم LabelText في جهة اليمين (Right Side) ---
        private void createLabelTextRight(Graphics2D g2) {
            if (ComboboxRoundNew.this.getLabelText() == null || ComboboxRoundNew.this.getLabelText().trim().isEmpty()) {
                return;
            }

            Insets in = getInsets();
            g2.setFont(ComboboxRoundNew.this.getFont().deriveFont(Font.BOLD, 11f));

            if (ComboboxRoundNew.this.isFocusOwner()) {
                g2.setColor(focusColor);
            } else {
                g2.setColor(new Color(130, 140, 150));
            }

            FontMetrics ft = g2.getFontMetrics();
            Rectangle2D r2 = ft.getStringBounds(ComboboxRoundNew.this.getLabelText(), g2);

            double size;
            if (animateHinText) {
                if (show) {
                    size = 14 * (1 - location);
                } else {
                    size = 14 * location;
                }
            } else {
                size = 14;
            }

            double textX = ComboboxRoundNew.this.getWidth() - in.right - r2.getWidth() - 15;
            double textY = in.top + ft.getAscent() - size;

            g2.drawString(ComboboxRoundNew.this.getLabelText(), (int) textX, (int) textY);
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
                int size = 8;
                int x = (width - size) / 2;
                int y = (height - size) / 2 + 2;
                int px[] = {x, x + size, x + size / 2};
                int py[] = {y, y, y + size / 2 + 1};
                g2.setColor(getBackground());
                g2.fillPolygon(px, py, px.length);
                g2.dispose();
            }
        }
    }
}