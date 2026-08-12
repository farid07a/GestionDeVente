/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package material.design;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ui.table.TableCustom;

public class designeTable {

    public designeTable() {
    }

    public void SearchTable(JTable table, JTextField textField) {
        TableRowSorter<TableModel> sort = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sort);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String str = textField.getText();
                if (str.trim().length() == 0) {
                    sort.setRowFilter(null);

                } else {
                    //(?i) recherche insensible à la casse
                    sort.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String str = textField.getText();
                if (str.trim().length() == 0) {
                    sort.setRowFilter(null);
                } else {
                    sort.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    public void setDesignTable(JTable tab, JScrollPane scrol) {

        TableCustom.apply(scrol, TableCustom.TableType.DEFAULT);

        // Header Renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setForeground(Color.WHITE);//new Color(52, 58, 64));
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerRenderer.setBackground( new Color (0,119,182));//22,22,115) );//(235,232,232));//230, 232, 236));

        // Cell Renderer
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBorder(noFocusBorder);

                setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    setBackground(Color.WHITE);
                    setForeground(new Color(60, 60, 60));
                } else {
                    setBackground(new Color(230, 240, 255));
                    setForeground(Color.BLACK);
                }

                return this;
            }
        };

        // Apply Renderer
        for (int i = 0; i < tab.getColumnCount(); i++) {
            tab.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            tab.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Read Only
        tab.setDefaultEditor(Object.class, null);

        // Table Design
        tab.setRowHeight(35);
        tab.setFont(new Font("Times New Roman", 1, 15));
        tab.setForeground( new Color(102,102,102)); //73, 80, 87));
   
        tab.getTableHeader().setFont(new Font("Times New Roman", 1, 14));
        tab.getTableHeader().setPreferredSize(new Dimension(0, 38));

        tab.getTableHeader().setReorderingAllowed(false);
        tab.getTableHeader().setResizingAllowed(false);

        tab.setSelectionBackground(new Color(230, 240, 255));
        tab.setSelectionForeground(Color.BLACK);

        tab.setShowHorizontalLines(true);
        tab.setShowVerticalLines(false);
        tab.setGridColor(new Color(230, 230, 230));
        tab.setIntercellSpacing(new Dimension(0, 1));

        // Scroll
        scrol.setVerticalScrollBar(new ScrollBar());
        scrol.getVerticalScrollBar().setBackground(Color.WHITE);
        scrol.getViewport().setBackground(Color.WHITE);

        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        scrol.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        
        
        Font customFont = new Font("Times New Roman", Font.BOLD, 14);
    Font headerFont = new Font("Times New Roman", Font.BOLD, 18);
     tab.setForeground(new Color(30, 41, 59)); // نص رمادي داكن واحترافي
     tab.setFont(new Font("Segoe UI", Font.PLAIN, 14));
  //  tab.setFont(customFont);
  //  tab.setForeground(Color.BLACK);
    tab.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    public void setDesignTable1(JTable tab, JScrollPane scrol) {
        TableCustom.apply(scrol, TableCustom.TableType.DEFAULT);
        scrol.setBorder(BorderFactory.createEmptyBorder());

        //tab.getTableHeader().setFont(new Font("", Font.BOLD, 15));
        // tab.getTableHeader().setBackground(new Color(153,153,255));
        DefaultTableCellRenderer MyCellrendar = new DefaultTableCellRenderer();
       // DefaultTableCellRenderer MyHeaderRender = new DefaultTableCellRenderer();
        //  DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)tab.getDefaultRenderer(Object.class);
        MyCellrendar.setForeground(Color.BLACK);  // new Color(0,0,204));//Color.WHITE);
        MyCellrendar.setFont(new java.awt.Font("Cairo", Font.BOLD, 16));
        //  MyHeaderRender.setBackground(new Color (235,235,235));//(206,230,255));//(204, 204, 204));//153,153,255));
        tab.setShowHorizontalLines(true); //show lines
        tab.setGridColor(new Color(230, 230, 230)); // color lines 
        MyCellrendar.setHorizontalAlignment(SwingConstants.CENTER);
        //  DefaultTableCellRenderer rendererTableHeader= (DefaultTableCellRenderer) tab.getTableHeader().getDefaultRenderer();
        MyCellrendar.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tab.getColumnCount(); i++) {
            tab.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(MyCellrendar);
            tab.getColumnModel().getColumn(i).setCellRenderer(MyCellrendar);
        }
//        tab.getTableHeader().setFont(new Font("Times New Roman",  Font.BOLD, 16));
//       tab.setFont(new Font("Arial", Font.PLAIN, 14)); 
        //tab.setFont(new Font("", Font.BOLD, 14));
        //  tab.setFont(new java.awt.Font("Times New Roman", 0, 13));
        tab.getTableHeader().setPreferredSize(new Dimension(MyCellrendar.getWidth(), 32));
        scrol.setVerticalScrollBar(new ScrollBar());
        scrol.getVerticalScrollBar().setBackground(Color.WHITE);
        scrol.getViewport().setBackground(Color.white);// make table without rouw white
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        scrol.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        //   tab.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        // Set font for the table header
        //    tab.getTableHeader().setFont(new Font("Times New Roman", Font.PLAIN, 18));
        // tab.setFont(new Font("Times New Roman", Font.BOLD, 13));
        // Set font for the table header
        tab.setRowHeight(40); // ارتفاع الصف
        //tab.setFont(new Font("Cairo", Font.BOLD, 15));
        tab.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        tab.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
     //   tab.getTableHeader().setForeground(Color.red);
//tab.setSelectionBackground(new Color(230,240,255));
//tab.setSelectionForeground(Color.BLACK);
        tab.setShowVerticalLines(false);

    }

}
