package material.design;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author RAVEN
 */
public class TableActionCellEditor extends DefaultCellEditor {

    private TableActionEvent event;
    JTable Table;
    int col;

    public TableActionCellEditor(TableActionEvent event, JTable Table,int col) {
        super(new JCheckBox());
        this.event = event;
        this.Table=Table;
        this.col= col;
        
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
        PanelAction action = new PanelAction();
        action.initEvent(event, row,Table,col);
        action.setBackground(jtable.getSelectionBackground());
        return action;
    }
}
