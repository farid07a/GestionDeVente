package material.design;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author RAVEN
 */
public class TableActionCellRender extends DefaultTableCellRenderer {

    Component component;

    public TableActionCellRender(Component component) {
        this.component = component;
    }
    
    
    
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSeleted, boolean bln1, int row, int column) {
        Component com = super.getTableCellRendererComponent(jtable, o, isSeleted, bln1, row, column);
        PanelAction action = new PanelAction();
       
       if(component instanceof PanelAction ){
        //   System.out.println("instanceof");
        if (isSeleted == false && row % 2 == 0) {
            action.setBackground(Color.WHITE);
        } else {
            action.setBackground(com.getBackground());
        }
        return action;
       }else{
           JLabel lab = new JLabel();
           lab.setText("lab");
           lab.setBackground(Color.red);
           return lab;
        }
    }
}
