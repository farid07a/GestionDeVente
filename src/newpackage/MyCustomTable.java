/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MyCustomTable extends JTable {

    // 1. المشيد الافتراضي (مهم جداً لبرنامج NetBeans لكي تتمكن من سحب وإسقاط الجدول في الـ Frame)
    public MyCustomTable() {
        // إنشاء نموذج افتراضي بأعمدة تجريبية ليظهر في شاشة التصميم
        this(new DefaultTableModel(
            new Object[][]{{"1", "منتج تجريبي", "100", "1", ""}}, 
            new String[]{"المعرف", "اسم المنتج", "السعر", "الكمية", "العمليات"}
        ));
    }
    
    // 2. المشيد الأساسي الذي تستدعيه من الكود ببياناتك الخاصة
    public MyCustomTable(DefaultTableModel model) {
        super(model);
        
        // تحسينات عامة لمظهر الجدول ليناسب الأزرار
        setRowHeight(35); // رفع طول السطر لتظهر الأزرار مرتاحة
        
        // تم استدعاء الدالة الأصلية للـ JTable بنجاح الآن
        super.setSelectionBackground(new Color(220, 204, 182)); 
        
        // التحقق من أن الجدول يحتوي على أعمدة كافية قبل تطبيق مدير الأزرار لتجنب الأخطاء في شاشة التصميم
        if (getColumnCount() > 4) {
            int buttonsColumnIndex = 4; 
            ButtonsCellManager manager = new ButtonsCellManager(this);
            // ربط العرض والتفاعل بالعمود المخصص
            getColumnModel().getColumn(buttonsColumnIndex).setCellRenderer(manager);
            getColumnModel().getColumn(buttonsColumnIndex).setCellEditor(manager);
            
            // ضبط عرض عمود الأزرار ليكون مناسباً للـ 3 أزرار معاً
            getColumnModel().getColumn(buttonsColumnIndex).setPreferredWidth(120);
        }
    }
}