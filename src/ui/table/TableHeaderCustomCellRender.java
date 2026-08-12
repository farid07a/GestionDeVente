package ui.table;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;


public class TableHeaderCustomCellRender extends DefaultTableCellRenderer {

    private final JTable table;

    public TableHeaderCustomCellRender(JTable table) {
        this.table = table;
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        setBorder(new EmptyBorder(8, 10, 8, 10));
         setOpaque(true);
        // يأخذ جميع الخصائص من JTable
        setFont(table.getTableHeader().getFont());
        setForeground(table.getTableHeader().getForeground());
        setBackground(table.getTableHeader().getBackground());

        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(table.getGridColor());
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
        g2.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);

        g2.dispose();
    }
}
//public class TableHeaderCustomCellRender extends DefaultTableCellRenderer {
//
//    private final JTable table;
//
//    public TableHeaderCustomCellRender(JTable table) {
//        this.table = table;
//    }
//
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        setBorder(new EmptyBorder(8, 10, 8, 10));
//        //com.setFont(table.getTableHeader().getFont());
//        setFont(new Font("Time New Romen",Font.BOLD, 12));
//        com.setBackground(table.getTableHeader().getBackground());
//        return com;
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setColor(table.getGridColor());
//        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
//        g2.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
//        g2.dispose();
//    }
//}
