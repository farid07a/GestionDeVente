/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.table;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class CustomTable extends JTable {

    private int hoveredRow = -1;
    // يمكنك تغيير "Tahoma" إلى "Cairo" إذا كان الخط مُثبتاً في مشروعك
    private final Font arabicFontBold = new Font("Tahoma", Font.BOLD, 13);
    private final Font arabicFontPlain = new Font("Tahoma", Font.PLAIN, 13);

    public CustomTable() {
        super();
        applySolidModernDesign();
    }

    public CustomTable(DefaultTableModel model) {
        super(model);
        applySolidModernDesign();
    }

    private void applySolidModernDesign() {
        setRowHeight(45); 
        setShowGrid(false); 
        setIntercellSpacing(new Dimension(0, 0)); 
        setBackground(Color.WHITE);
        setFocusable(false); 
        
        // تطبيق الخط العربي النظيف على نصوص الجدول
        setForeground( Color.BLACK);//(30, 41, 59)); 
        setFont(arabicFontPlain);
        
        setSelectionBackground(new Color(243, 244, 246)); 
        setSelectionForeground(new Color(29, 78, 216));   
        
        // ----------------------------------------------------
        // حل مشكلة الهيدر المقسم والخلفية الباهتة جذرياً:
        // ----------------------------------------------------
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        // إنشاء Renderer جديد تماماً للهيدر لإلغاء تقسيمات النظام واللون الباهت
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // لون أزرق ملكي صريح ونقي (Solid وبدون بهتان)
                setBackground(new Color(37, 99, 235)); 
                setForeground(Color.WHITE); 
                setFont(arabicFontBold);
                setHorizontalAlignment(JLabel.RIGHT); // محاذاة لليمين
                
                // إضافة مساحة داخلية (Padding) وإلغاء التقسيمات العمودية تماماً
                setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); 
                
                return this;
            }
        });

        // تأثير الـ Hover عند مرور الفأرة
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    repaint(); 
                }
            }
        });
    }

    /**
     * الـ Renderer المطور للأعمدة بـ خط Tahoma ومحاذاة عربية صحيحة
     */
    public static class IconTableCellRenderer extends DefaultTableCellRenderer {
        private Icon icon;
        private boolean isIconColumn;
        private final Font cellFont = new Font("Tahoma", Font.PLAIN, 13);

        public IconTableCellRenderer(Icon icon) {
            this.icon = icon;
            this.isIconColumn = true;
            setupComponent();
        }

        public IconTableCellRenderer() {
            this.isIconColumn = false;
            setupComponent();
        }

        private void setupComponent() {
            setHorizontalAlignment(JLabel.RIGHT); 
            setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); 
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setText(value != null ? value.toString() : "");
            setFont(cellFont); // تطبيق الخط هنا أيضاً لضمان التناسق
            
            if (isIconColumn) {
                setIcon(icon);
                setIconTextGap(12); 
            } else {
                setIcon(null);
            }
            
            // خط سفلي رمادي ناعم جداً ومسطح للفصل بين الأسطر
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)), 
                    BorderFactory.createEmptyBorder(0, 20, 0, 20) 
            ));

            CustomTable customTable = (CustomTable) table;
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else if (row == customTable.hoveredRow) {
                setBackground(new Color(249, 250, 251)); 
                setForeground(new Color(15, 23, 42)); 
            } else {
                setBackground(Color.WHITE); 
                setForeground(new Color(71, 85, 105)); 
            }
            
            return this;
        }
    }
}