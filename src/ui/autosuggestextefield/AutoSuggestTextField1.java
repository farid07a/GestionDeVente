package ui.autosuggestextefield;

import config.DatabaseConnection;
import dao.impl.CategorieDAOImpl;
import entity.Categorie;
import frame.ArabicComparator;
import java.awt.Color;
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

public class AutoSuggestTextField1 extends TextFiledRound {

    private final ModernPopupMenu  popupMenu;
    private final JList<String> suggestionList;
    private final DefaultListModel<String> listModel;
    private List<String> dataDictionary;
    private boolean isAdjusting = false;

    CategorieDAOImpl categorieDAOImpl;
    public AutoSuggestTextField1() {
        super();
        setBorder(new EmptyBorder(20, 3, 10, 3));
        setSelectionColor(new Color(76, 204, 255));
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
    
    // 1. تعديل الخط ليصبح Cairo Bold 16 داخل القائمة
    suggestionList.setFont(new Font("Cairo", Font.BOLD, 16));
    
    // 2. تعديل لون الخلفية عند تحديد العنصر ليصبح أحمر (Red) ولون الكتابة أبيض
    suggestionList.setSelectionBackground(new Color(76, 204, 255));//Color.RED);
    suggestionList.setSelectionForeground(Color.WHITE);
    
    // ضبط ارتفاع كل سطر ليصبح متناسقاً مع الخط الكبير
    suggestionList.setFixedCellHeight(50);
    
    suggestionList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    
    // محاذاة النص إلى اليمين (يتوافق مع العربية RIGHT_TO_LEFT)
    suggestionList.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    
    // نضع الـ suggestionList داخل الـ scrollPane المخصص
    popupMenu.getScrollPane().setViewportView(suggestionList);
    popupMenu.getComponent().setCursor( new Cursor(Cursor.HAND_CURSOR));

    popupMenu.getScrollPane().setBorder(null);
    popupMenu.setFocusable(false);

    // مراقبة الكتابة في الحقل
    this.getDocument().addDocumentListener(new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) { if (!isAdjusting) filter(); }
        @Override public void removeUpdate(DocumentEvent e) { if (!isAdjusting) filter(); }
        @Override public void changedUpdate(DocumentEvent e) { if (!isAdjusting) filter(); }
    });
    
    // [إضافة]: عند الضغط بالماوس داخل الـ TextField تظهر القائمة كاملة
    this.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!popupMenu.isShowing()) {
                filter(); // استدعاء الفلترة لإظهار البيانات
            }
        }
        // الحفاظ على كود الماوس القديم الخاص بك لـ mouseEntered و mouseExited
        @Override public void mouseEntered(MouseEvent me) { mouseOver = true; repaint(); }
        @Override public void mouseExited(MouseEvent me) { mouseOver = false; repaint(); }
    });


    // [إضافة]: عند الضغط بالماوس داخل الـ TextField تظهر القائمة كاملة
    this.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!popupMenu.isShowing()) {
                filter(); // استدعاء الفلترة لإظهار البيانات
            }
        }
        // الحفاظ على كود الماوس القديم الخاص بك لـ mouseEntered و mouseExited
        @Override public void mouseEntered(MouseEvent me) { mouseOver = true; repaint(); }
        @Override public void mouseExited(MouseEvent me) { mouseOver = false; repaint(); }
    });

    // دعم التحكم عبر لوحة المفاتيح
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

    // عند اختيار عنصر بالفأرة
    suggestionList.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                selectItem();
            }
        }
    });
}
    
    private void filter() {
    listModel.clear();
    String text = getText().trim();

    // إذا كان الحقل فارغاً، نعرض كامل محتويات الديكشنري المتوفرة
    if (text.isEmpty()) {
        for (String s : dataDictionary) {
            listModel.addElement(s);
        }
    } else {
        // إذا كان يحتوي على نص، نفلتر كالعادة
        boolean found = false;
        for (String s : dataDictionary) {
            if (s.toLowerCase().contains(text.toLowerCase())) {
                listModel.addElement(s);
                found = true;
            }
        }
        if (!found) {
            listModel.addElement("➕ إضافة  \"  " + text + "\"");
        }
    }

    // إذا كانت القائمة فارغة تماماً ولا يوجد شيء لعرضه نقفل الـ Popup
    if (listModel.isEmpty()) {
        popupMenu.setVisible(false);
        return;
    }

    // حساب الارتفاع المرن بناءً على عدد العناصر (الارتفاع الافتراضي 50 للخلية الواحدة ليتناسب مع الخط)
    int height = Math.min(listModel.getSize() * 50 + 12, 250); 
    
    // ضبط الأبعاد وعرض الـ Popup المتطابق مع الحقل
    popupMenu.setPreferredSize(new Dimension(getWidth(), height));
    popupMenu.pack(); 
    
    // تحديد العنصر الأول افتراضياً لتسهيل التنقل بالأسهم
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
    if (value == null) return;

    isAdjusting = true;

    if (value.startsWith("➕")) {
        String txt = getText().trim();
        if (!txt.isEmpty()) {
            
            // 1. إذا كان الـ DAO متوفراً، نقوم بحفظ الفئة في قاعدة البيانات فوراً
            
                // افترضنا أن Constructor الكلاس Categorie يستقبل: (id, nom, description)
//                Categorie categorie  = new  Categorie(0, txt, ""); 
//                
//               
//                 categorieDAOImpl= new CategorieDAOImpl(DatabaseConnection.getInstance().getConnection());
//                if ( categorieDAOImpl.save(categorie) > 0) {
//                    System.out.println("تم حفظ الفئة بنجاح في قاعدة البيانات!");
//                } else {
//                    System.err.println("فشل الحفظ في قاعدة البيانات!");
//                }
            

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
//        if (value == null) return;
//
//        isAdjusting = true;
//
//        if (value.startsWith("➕")) {
//            String txt = getText().trim();
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
//    
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    
    private final Animator animator ;
    private boolean animateHinText = true;
    private float location;
    private int langue = 0 ;
    private boolean show;
    private boolean mouseOver = false;
    private String labelText = "Label";
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

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        int width = getWidth();
        int height = getHeight();
        if (mouseOver) {
            g2.setColor(lineColor);
        } else {
             if(getText().equals("")){
                g2.setColor(new Color(150, 150, 150));
             }else{
                 g2.setColor(lineColor);
             }
        }
        g2.fillRect(2, height - 1, width - 4, 1);
        createHintText(g2);
        createLineStyle(g2);
    
        g2.dispose();
    }
    
    private void createHintText(Graphics2D g2) {
        Insets in = getInsets();
        g2.setColor(Color.BLACK);
        setFont(new java.awt.Font("Cairo", 1, 15));     
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(labelText, g2);
        
        double height = getHeight() - in.top - in.bottom;
        double textY = (height - r2.getHeight()) / 2;
        
        double width = getWidth()- in.right - in.left;
        double textx = (width - r2.getWidth()) ;
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
       if (langue ==1){
           g2.drawString(labelText,(int)(in.left), (int) (in.top + textY + ft.getAscent() - size));
       } else {
           g2.drawString(labelText,(int)(in.left+textx), (int) (in.top + textY + ft.getAscent() - size));
       }
    }

    private void createLineStyle(Graphics2D g2) {
        if (isFocusOwner() ) {
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
}