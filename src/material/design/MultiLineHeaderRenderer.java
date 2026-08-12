/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package material.design;

/**
 *
 * @author farid
 */
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MultiLineHeaderRenderer implements TableCellRenderer {

    private final MultiLineLabel label;

    public MultiLineHeaderRenderer(String text) {
        label = new MultiLineLabel(text);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBackground(UIManager.getColor("TableHeader.background"));
        label.setForeground(UIManager.getColor("TableHeader.foreground"));
        label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        label.setText((String) value);
        return label;
    }
}
