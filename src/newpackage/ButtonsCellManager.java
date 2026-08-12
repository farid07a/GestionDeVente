/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.EventObject;

public class ButtonsCellManager extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    
   // private final ActionButtonsPanel editPanel = new ActionButtonsPanel();
        private final pabButton editPanel = new pabButton();

    private JTable targetTable;

    public ButtonsCellManager(JTable table) {
        this.targetTable = table;

        // حدث زر الزيادة (+)
        editPanel.getBtnAdd().addActionListener(e -> {
            int row = targetTable.getEditingRow();
            if (row != -1) {
                DefaultTableModel model = (DefaultTableModel) targetTable.getModel();
                // بفرض أن عمود الكمية هو رقم 2 (عدّل الترتيب حسب جدولك)
                int currentQty = Integer.parseInt( model.getValueAt(row, 3).toString()); 
                model.setValueAt(currentQty + 1, row,3);
                
//                // تحديث السعر الإجمالي إذا كان في العمود رقم 3 (السعر المفرد في عمود 1)
//                double price = Double.parseDouble(model.getValueAt(row, 1).toString());
//                model.setValueAt((currentQty + 1) * price, row, 3);
//                
                fireEditingStopped(); // إغلاق المحرر لتحديث العرض فوراً
            }
        });

        // حدث زر النقصان (-)
        editPanel.getBtnMinus().addActionListener(e -> {
            int row = targetTable.getEditingRow();
            if (row != -1) {
                DefaultTableModel model = (DefaultTableModel) targetTable.getModel();
                int currentQty =Integer.parseInt( model.getValueAt(row, 3).toString());
                if (currentQty > 1) { // منع النزول عن 1
                    model.setValueAt(currentQty - 1, row,3);
                    
//                    double price =Double.parseDouble(model.getValueAt(row, 1).toString());
//                    model.setValueAt((currentQty - 1) * price, row, 3);
                }
                fireEditingStopped();
            }
        });

        // حدث زر الحذف (🗑️)
        editPanel.getBtnDelete().addActionListener(e -> {
            int row = targetTable.getEditingRow();
            if (row != -1) {
                fireEditingStopped(); // يجب إيقاف التعديل أولاً قبل الحذف تجنباً للمشاكل
                DefaultTableModel model = (DefaultTableModel) targetTable.getModel();
                model.removeRow(row);
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // لتغيير لون الخلفية عند تحديد السطر
        if (isSelected) {
           editPanel.setBackground(table.getSelectionBackground());
        } else {
            editPanel.setBackground(table.getBackground());
        }
        return editPanel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editPanel.setBackground(table.getSelectionBackground());
        return editPanel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true; // جعل الخلية قابلة للتفاعل والضغط
    }
}