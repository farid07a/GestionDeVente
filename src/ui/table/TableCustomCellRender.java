package ui.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.Color;

import java.awt.Color;

public class TableCustomCellRender extends DefaultTableCellRenderer {

    private final HoverIndex hoverRow;

    public TableCustomCellRender(HoverIndex hoverRow) {
        this.hoverRow = hoverRow;
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(
                table, value, isSelected, false, row, column);

        setBorder(new EmptyBorder(10, 10, 10, 10));

        // استعمال خصائص الجدول
//        setFont(table.getFont());
//        setForeground(table.getForeground());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            if (row == hoverRow.getIndex()) {
                setBackground(new Color(230, 230, 230));
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(new Color(242, 242, 242));
                }
            }
        }

        return this;
    }
}

//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Font;
//import javax.swing.JTable;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//
//public class TableCustomCellRender extends DefaultTableCellRenderer {
//
//    private final HoverIndex hoverRow;
//
//    public TableCustomCellRender(HoverIndex hoverRow) {
//        this.hoverRow = hoverRow;
//    }
//
//    @Override
//
//    public Component getTableCellRendererComponent(JTable table, Object value,
//            boolean isSelected, boolean hasFocus, int row, int column) {
//
//        Component com = super.getTableCellRendererComponent(
//                table, value, isSelected, false, row, column);
//
//        setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        if (isSelected) {
//            com.setBackground(table.getSelectionBackground());
//            com.setForeground(Color.BLACK);
//        } else {
//            if (row == hoverRow.getIndex()) {
//                com.setBackground(new Color(230, 230, 230));
//            } else {
//                if (row % 2 == 0) {
//                    com.setBackground(Color.WHITE);
//                } else {
//                    com.setBackground(new Color(242, 242, 242));
//                }
//            }
//
//           // com.setForeground(Color.BLACK);
//        }
//
//        // استعمال خط الجدول
//setFont(new Font("Cairo", Font.BOLD, 15));
//setForeground(Color.BLACK);
//        return com;
//    }
////    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
////        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
////        setBorder(new EmptyBorder(10, 10, 10, 10));
////        if (isSelected) {
////            com.setBackground(table.getSelectionBackground());
////        } else {
////            if (row == hoverRow.getIndex()) {
////                com.setBackground(new Color(230, 230, 230));
////            } else {
////                if (row % 2 == 0) {
////                    com.setBackground(Color.WHITE);
////                } else {
////                    com.setBackground(new Color(242, 242, 242));
////                }
////            }
////        }
////        
////        com.setFont(table.getFont());
////       
////       
////      // com.setFont(new Font("Segeo UI ",1 ,  12));
////        return com;
////    }
//}
