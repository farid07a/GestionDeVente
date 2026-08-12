package ui.table;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;


public class TableCustom {
    
    public static void apply(JScrollPane scroll, TableType type) {

    JTable table = (JTable) scroll.getViewport().getComponent(0);

    // Selection
    table.setSelectionBackground(new Color(123, 207, 255));
    table.setSelectionForeground(table.getForeground());

    // Header
    table.getTableHeader().setReorderingAllowed(false);
    table.getTableHeader().setDefaultRenderer(new TableHeaderCustomCellRender(table));

    // Renderer
    HoverIndex hoverRow = new HoverIndex();

    if (type == TableType.DEFAULT) {
        table.setDefaultRenderer(Object.class, new TableCustomCellRender(hoverRow));
    } else {
        table.setDefaultRenderer(Object.class, new TextAreaCellRenderer(hoverRow));
    }

    // Table
    table.setRowHeight(30);
    table.setShowVerticalLines(true);
    table.setGridColor(new Color(220, 220, 220));

    // لا تفرض أي خط أو لون هنا
    // table.setForeground(...)
    // table.setFont(...)

    // Scroll
    scroll.setBorder(BorderFactory.createEmptyBorder());
    scroll.setViewportBorder(BorderFactory.createEmptyBorder());

    JPanel panel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(new Color(220, 220, 220));
            g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
    };

    panel.setBackground(new Color(250, 250, 250));
    scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panel);

    scroll.getViewport().setBackground(Color.WHITE);
    scroll.getVerticalScrollBar().setUI(new ScrollBarCustomUI());
    scroll.getHorizontalScrollBar().setUI(new ScrollBarCustomUI());

    scroll.setCursor(new Cursor(Cursor.HAND_CURSOR));

    table.getTableHeader().setBackground(new Color(250, 250, 250));

    table.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e) {
            hoverRow.setIndex(-1);
            table.repaint();
        }
    });

    table.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            if (row != hoverRow.getIndex()) {
                hoverRow.setIndex(row);
                table.repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            if (row != hoverRow.getIndex()) {
                hoverRow.setIndex(row);
                table.repaint();
            }
        }
    });
}
//
//    public static void apply(JScrollPane scroll, TableType type) {
//        JTable table = (JTable) scroll.getViewport().getComponent(0);
//        table.setSelectionBackground( new Color(123, 207, 255));
//        table.getTableHeader().setReorderingAllowed(false);
//        table.getTableHeader().setDefaultRenderer(new TableHeaderCustomCellRender(table));
//        table.setRowHeight(30);
//        HoverIndex hoverRow = new HoverIndex();
//        TableCellRenderer cellRender;
//        if (type == TableType.DEFAULT) {
//            cellRender = new TableCustomCellRender(hoverRow);
//        } else {
//            cellRender = new TextAreaCellRenderer(hoverRow);
//        }
//        table.setDefaultRenderer(Object.class, cellRender);
//        table.setShowVerticalLines(true);
//        table.setGridColor(new Color(220, 220, 220));
//        table.setForeground(new Color(51, 51, 51));
//        table.setSelectionForeground(new Color(51, 51, 51));
//        
//        //table.setFont(new Font("time New Romen ",Font.BOLD ,  24));
//        
//       // scroll.setBorder(new LineBorder(new Color(220, 220, 220)));
//       scroll.setBorder(BorderFactory.createEmptyBorder());
//scroll.setViewportBorder(BorderFactory.createEmptyBorder());
//        JPanel panel = new JPanel() {
//            @Override
//            public void paint(Graphics grphcs) {
//                super.paint(grphcs);
//                grphcs.setColor(new Color(220, 220, 220));
//                grphcs.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
//                grphcs.dispose();
//            }
//        };
//        panel.setBackground(new Color(250, 250, 250));
//        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panel);
//        scroll.getViewport().setBackground(Color.WHITE);
//        scroll.getVerticalScrollBar().setUI(new ScrollBarCustomUI());
//        scroll.getHorizontalScrollBar().setUI(new ScrollBarCustomUI());
//        scroll.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        table.getTableHeader().setBackground(new Color(250, 250, 250));
//        table.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseExited(MouseEvent e) {
//                hoverRow.setIndex(-1);
//                table.repaint();
//            }
//
//        });
//        table.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                int row = table.rowAtPoint(e.getPoint());
//                if (row != hoverRow.getIndex()) {
//                    hoverRow.setIndex(row);
//                    table.repaint();
//                }
//            }
//
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                int row = table.rowAtPoint(e.getPoint());
//                if (row != hoverRow.getIndex()) {
//                    hoverRow.setIndex(row);
//                    table.repaint();
//                }
//            }
//        });
//    }

    public static enum TableType {
        MULTI_LINE, DEFAULT
    }
}
