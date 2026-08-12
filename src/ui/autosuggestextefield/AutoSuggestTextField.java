/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.autosuggestextefield;

import config.DatabaseConnection;
import dao.impl.CategorieDAOImpl;
import entity.Categorie;
import entity.Produit;
import frame.ArabicComparator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import material.design.TextFiledRound;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;


public class AutoSuggestTextField extends TextFiledRound {

     public String getSaveObject() {
        return saveObject;
    }

    public void setSaveObject(String saveObject) {
        this.saveObject = saveObject;
    }
    private final ModernPopupMenu popupMenu;
    private final JList<String> suggestionList;
    private final DefaultListModel<String> listModel;
    private List<String> dataDictionary;
    private boolean isAdjusting = false;
    
    private int radius = 14;
    private String saveObject="categorie";

   

    // Colors
    private final Color borderColor = new Color(220, 224, 230);   // #DCE0E6
    private final Color focusColor = new Color(37, 99, 235);       // #2563EB
    private final Color backgroundColor = Color.WHITE;

    // العلامة التي نميز بها سطر الإضافة برمجياً
    private static final String ADD_PREFIX = "[ADD_NEW]:";

    public AutoSuggestTextField() {
        super();
          setOpaque(false);
        setBackground(backgroundColor);
        setForeground(new Color(52, 58, 64));
        setCaretColor(new Color(52, 58, 64));
 setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
      //  setBorder(new EmptyBorder(20, 3, 10, 3));
        setSelectionColor(new Color(76, 204, 255));

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
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                animateHinText = getText().equals("");
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

        this.popupMenu = new ModernPopupMenu();
        this.listModel = new DefaultListModel<>();
        this.suggestionList = new JList<>(listModel);
        this.dataDictionary = new ArrayList<>();

        initComponent();
    }

    public void setDictionary(List<String> data) {
        this.dataDictionary = new ArrayList<>(data);
        Collections.sort(this.dataDictionary, new ArabicComparator());
    }

    private void initComponent() {
        suggestionList.setFocusable(false);
        suggestionList.setFont(new Font("Cairo", Font.BOLD, 16));

        // تعديل ألوان التحديد المفضلة لديك (الأزرق الفاتح والكتابة بالأبيض)
        suggestionList.setSelectionBackground(new Color(76, 204, 255));
        suggestionList.setSelectionForeground(Color.WHITE);
        suggestionList.setFixedCellHeight(50);
        suggestionList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        suggestionList.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // تفعيل الـ Renderer المخصص لعرض الأيقونة عند سطر الإضافة
        suggestionList.setCellRenderer(new SuggestionListRenderer());

        popupMenu.getScrollPane().setViewportView(suggestionList);
        popupMenu.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
        popupMenu.getScrollPane().setBorder(null);
        popupMenu.setFocusable(false);

        // مراقبة الكتابة
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isAdjusting) {
                    filter();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isAdjusting) {
                    filter();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!isAdjusting) {
                    filter();
                }
            }
        });

        // مستمع الماوس الموحد (تم إزالة التكرار)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!popupMenu.isShowing()) {
                    filter();
                }
            }

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

        // التحكم بالأسهم والأزرار
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (popupMenu.isShowing()) {
                    int index = suggestionList.getSelectedIndex();
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        suggestionList.setSelectedIndex(Math.min(index + 1, listModel.getSize() - 1));
                        suggestionList.ensureIndexIsVisible(suggestionList.getSelectedIndex());
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        suggestionList.setSelectedIndex(Math.max(index - 1, 0));
                        suggestionList.ensureIndexIsVisible(suggestionList.getSelectedIndex());
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        selectItem();
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        popupMenu.setVisible(false);
                    }
                }
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectItem();
                }
            }
        });

        // [إضافة]: مستمع لحركة الماوس داخل الـ JList لتفعيل الـ Hover الحقيقي فوراً
        suggestionList.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // تحديد السطر (index) بناءً على مكان الماوس الحالي
                int index = suggestionList.locationToIndex(e.getPoint());
                if (index != -1 && index != suggestionList.getSelectedIndex()) {
                    suggestionList.setSelectedIndex(index); // تحديث السطر المحدد ليقوم الـ Renderer بتلوينه
                }
            }
        });

// [إضافة]: عند خروج الماوس تماماً من القائمة لإلغاء تحديد الـ Hover
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                suggestionList.clearSelection(); // إلغاء التحديد عند خروج الماوس
            }
        });
    }

    private void filter() {
        listModel.clear();
        String text = getText().trim();

        if (text.isEmpty()) {
            for (String s : dataDictionary) {
                listModel.addElement(s);
            }
        } else {
            boolean found = false;
            for (String s : dataDictionary) {
                if (s.toLowerCase().contains(text.toLowerCase())) {
                    listModel.addElement(s);
                    found = true;
                }
            }
            if (!found) {
                // نضع بادئة خاصة لنميز سطر الإضافة داخل الـ Renderer
                listModel.addElement(ADD_PREFIX + text);
            }
        }

        if (listModel.isEmpty()) {
            popupMenu.setVisible(false);
            return;
        }

        int height = Math.min(listModel.getSize() * 50 + 12, heightmenu);
        popupMenu.setPreferredSize(new Dimension(getWidth(), height));
        popupMenu.pack();

        if (suggestionList.getSelectedIndex() == -1) {
            suggestionList.setSelectedIndex(0);
        }

        if (!popupMenu.isShowing() && this.isShowing()) {
            popupMenu.show(this, 0, getHeight());
            this.requestFocusInWindow();
        }
    }

    private void selectItem() {
        String value = suggestionList.getSelectedValue();
        if (value == null) {
            return;
        }

        isAdjusting = true;
        // التعديل هنا: نتحقق باستخدام البادئة البرمجية ADD_PREFIX التي تبدأ بـ [ADD_NEW]:
        if (value.startsWith(ADD_PREFIX)) {
            // استخراج النص الفعلي الذي كتبه المستخدم (حذف البادئة)
            String txt = value.substring(ADD_PREFIX.length()).trim();
            if (!txt.isEmpty()) {
                switch (saveObject) {
                    case "categorie":                       
                // 1. إذا كان الـ DAO متوفراً، نقوم بحفظ الفئة في قاعدة البيانات فوراً
                System.out.println(" save");
                // افترضنا أن Constructor الكلاس Categorie يستقبل: (id, nom, description)
                Categorie categorie = new Categorie(0, txt, "");
                CategorieDAOImpl categorieDAOImpl = new CategorieDAOImpl(DatabaseConnection.getInstance().getConnection());
                if (categorieDAOImpl.save(categorie) > 0) {
                    System.out.println("تم حفظ الفئة بنجاح في قاعدة البيانات!");
                } else {
                    System.err.println("فشل الحفظ في قاعدة البيانات!");
                }            
                 
                        break;
                     
                        
                    default:
                        break;
                }

                // 2. إضافة العنصر الجديد إلى القائمة الحالية المعروضة لتفادي التكرار
                if (!dataDictionary.contains(txt)) {
                    dataDictionary.add(txt);
                    Collections.sort(dataDictionary, new frame.ArabicComparator());
                }
            }
            setText(txt);
        } else {
            setText(value);
        }

        isAdjusting = false;
        popupMenu.setVisible(false);
    }

//    private void selectItem() {
//        String value = suggestionList.getSelectedValue();
//        if (value == null) {
//            return;
//        }
//
//        isAdjusting = true;
//
//        if (value.startsWith(ADD_PREFIX)) {
//            String txt = value.substring(ADD_PREFIX.length());
//            if (!dataDictionary.contains(txt) && !txt.isEmpty()) {
//                dataDictionary.add(txt);
//                Collections.sort(dataDictionary, new ArabicComparator());
//            }
//            setText(txt);
//        } else {
//            setText(value);
//        }
//
//        isAdjusting = false;
//        popupMenu.setVisible(false);
//    }
    // --- كلاس الـ Renderer الداخلي لرسم النص والأيقونة بشكل احترافي ---
    private class SuggestionListRenderer extends DefaultListCellRenderer {
        // إنشاء أيقونة زائد (+) دائرية وجميلة برمجياً

        // إنشاء أيقونة زائد (+) دائرية خضراء خفيفة مع علامة بيضاء برمجياً
        private final Icon addIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. رسم الدائرة الخلفية باللون الأخضر الخفيف الناصع (Mint/Light Green)
                g2.setColor(new Color(12, 174, 12));
                g2.fillOval(x, y, 35, 35);

// 2. رسم علامة الـ + بالداخل باللون الأبيض (White) في المنتصف تماماً
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(3.5f)); // سمك الخط الجديد

// حسابات المنتصف بدقة لحجم 35:
// المنتصف هو 17.5 .. وطول ضلع الزائد سيكون 16 بكسل (يبدأ من 9 وينتهي عند 25)
                g2.drawLine(x + 17, y + 9, x + 17, y + 25);  // الخط العمودي الصحيح (X ثابت، Y يتغير)
                g2.drawLine(x + 9, y + 17, x + 25, y + 17);  // الخط الأفقي الصحيح (X يتغير، Y ثابت)

                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return 24;
            }

            @Override
            public int getIconHeight() {
                return 24;
            }
        };

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            String itemText = (value != null) ? value.toString() : "";

            // إعطاء مسافة جمالية داخلية لكل عنصر في القائمة
            label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            // تفعيل خاصية التلوين الخلفي
            label.setOpaque(true);

            if (isSelected) {
                // [الـ Hover المخصص]: اللون الأزرق الفاتح الذي اخترته أنت عند مرور الماوس
                label.setBackground(new Color(76, 204, 255));
                label.setForeground(Color.WHITE); // لون الخط يصبح أبيض عند التحديد
            } else {
                // اللون الافتراضي للعنصر عندما لا يلمسه الماوس (خلفية بيضاء مثلاً)
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
            }

            // التحقق إذا كان العنصر هو سطر "الإضافة" لتركيب الأيقونة الخضراء
            if (itemText.startsWith(ADD_PREFIX)) {
                String pureText = itemText.substring(ADD_PREFIX.length());
                label.setText("إضافة " + "\"" + pureText + "\"");
                label.setIcon(addIcon); // الأيقونة الخضراء التي صممناها

                // إذا لم يكن الماوس فوقه، اجعل نص الإضافة مميز باللون الأخضر الغامق بدلاً من الأسود
                if (!isSelected) {
                    label.setForeground(new Color(12, 174, 12));
                }
            } else {
                label.setIcon(null); // العناصر العادية بدون أيقونة
            }

            // ضبط محاذاة النص والأيقونة لليمين لتتوافق مع اللغة العربية
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setHorizontalTextPosition(SwingConstants.LEFT);

            return label;
        }
//        @Override
//        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//            
//            String itemText = (value != null) ? value.toString() : "";
//            
//            // إعطاء مسافة جمالية داخلية لكل عنصر في القائمة
//            label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
//            
//            if (itemText.startsWith(ADD_PREFIX)) {
//                // استخراج النص الحقيقي بدون البادئة البرمجية
//                String pureText = itemText.substring(ADD_PREFIX.length());
//                label.setText("إضافة " + "\"" + pureText + "\"");
//                label.setIcon(addIcon); // تركيب الأيقونة المجهزة
//                
//                // جعل نص الإضافة مميز بلون مختلف قليلاً إذا لم يكن محدداً
//                if (!isSelected) {
//                    label.setForeground(new Color(3, 155, 216));
//                }
//            } else {
//                label.setIcon(null); // العناصر العادية لا تحتاج أيقونة زائد
//                if (!isSelected) {
//                    label.setForeground(Color.BLACK);
//                }
//            }
//            
//            // ضبط محاذاة النص والأيقونة لليمين لتتوافق مع اللغة العربية
//            label.setHorizontalAlignment(SwingConstants.RIGHT);
//            label.setHorizontalTextPosition(SwingConstants.LEFT); 
//            
//            return label;
//        }
    }

    // --- بقية الأكواد الخاصة بـ دالة Paint والمتغيرات كما هي بدون تغيير ---
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public void setHeightmenu(int heightmenu) {
        this.heightmenu = heightmenu;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    private final Animator animator;
    private boolean animateHinText = true;
    private float location;
    private int langue = 0;
    private boolean show;
    private boolean mouseOver = false;
    private String labelText = "Label";
    private int heightmenu = 250;
    private Color lineColor = new Color(3, 155, 216);

    public void setLangue(int langue) {
        this.langue = langue;
    }

    public int getLangue() {
        return langue;
    }

    public String getLabelText() {
        return labelText;
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

//    @Override
//    public void paint(Graphics grphcs) {
//        super.paint(grphcs);
//        Graphics2D g2 = (Graphics2D) grphcs;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
//        int width = getWidth();
//        int height = getHeight();
//        if (mouseOver) {
//            g2.setColor(lineColor);
//        } else {
//            if (getText().equals("")) {
//                g2.setColor(new Color(150, 150, 150));
//            } else {
//                g2.setColor(lineColor);
//            }
//        }
//        g2.fillRect(2, height - 1, width - 4, 1);
//        createHintText(g2);
//        createLineStyle(g2);
//        g2.dispose();
//    }
    
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
        g2.setStroke(new BasicStroke(1.5f));

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

    private void createHintText(Graphics2D g2) {
        Insets in = getInsets();
        g2.setColor(Color.BLACK);
        setFont(new java.awt.Font("Times New Roman", 1, 15));
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(labelText, g2);

        double height = getHeight() - in.top - in.bottom;
        double textY = (height - r2.getHeight()) / 2;

        double width = getWidth() - in.right - in.left;
        double textx = (width - r2.getWidth());
        double size;
        if (animateHinText) {
            if (show) {
                size = 18 * (1 - location);
            } else {
                size = 18 * location;
            }
        } else {
            size = 18;
        }
        if (langue == 1) {
            g2.drawString(labelText, (int) (in.left), (int) (in.top + textY + ft.getAscent() - size));
        } else {
            g2.drawString(labelText, (int) (in.left + textx), (int) (in.top + textY + ft.getAscent() - size));
        }
    }

    private void createLineStyle(Graphics2D g2) {
        if (isFocusOwner()) {
            double width = getWidth() - 4;
            int height = getHeight();
            g2.setColor(lineColor);
            double size;
            if (show) {
                size = width * (1 - location);
            } else {
                size = width * location;
            }
            double x = (width - size) / 2;
            g2.fillRect((int) (x + 2), height - 2, (int) size, 2);
        }
    }

    @Override
    public void setText(String string) {
        if (!getText().equals(string)) {
            showing(string.equals(""));
        }
        super.setText(string);
    }
    
    public void Select(String text) {

    if (text == null || text.trim().isEmpty()) {
        return;
    }

    isAdjusting = true;

    setText(text);

    isAdjusting = false;

    popupMenu.setVisible(false);
}

}
