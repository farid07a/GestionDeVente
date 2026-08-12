/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enums;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableFilter {

    private JTable table;
    private TableRowSorter<TableModel> sorter;

    public TableFilter(JTable table){

        this.table = table;

        sorter = new TableRowSorter<>(table.getModel());

        table.setRowSorter(sorter);
    }
    
    public void filter(String search, String entreprise, String year, String month) {

    List<RowFilter<Object, Object>> filters = new ArrayList<>();

    // 1. البحث العام نصياً (في كل الأعمدة)
    if (search != null && !search.trim().isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + search.trim()));
    }

    // 2. فلترة المؤسسة (العمود 0 بالضبط)
    if (entreprise != null && !entreprise.trim().isEmpty()) {
        filters.add(RowFilter.regexFilter("^" + Pattern.quote(entreprise.trim()) + "$", 6));
    }

if (year != null && !year.trim().isEmpty()) {
        // ^2026 يطابق أول 4 أرقام في خانة التاريخ تماماً
        filters.add(RowFilter.regexFilter("^" + year.trim(), 1));
    }

    // 4. فلترة الشهر من نفس العمود 1 (2026-02-01)
        if (month != null && !month.trim().isEmpty()) {
            // تحويل أرقام الشهور الفردية مثل 2 إلى 02 لكي تطابق 2026-02-01
            String formattedMonth;
            try {
                formattedMonth = String.format("%02d", Integer.parseInt(month.trim()));
            } catch (NumberFormatException e) {
                formattedMonth = month.trim();
            }
            // -02- يضمن مطابقة الشهر فقط وعدم خلطه مع اليوم (مثلاً يوم 02)
            filters.add(RowFilter.regexFilter("-" + formattedMonth + "-", 1));

        }
    // 5. تطبيق الفلاتر على الجدول (خارج شروط الفلاتر)
        if (filters.isEmpty()) {
            sorter.setRowFilter(null); // إظهار الكل عند عدم اختيار أي فلتر
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }
    public void clear(){

        sorter.setRowFilter(null);

    }

}
