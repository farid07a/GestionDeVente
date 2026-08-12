/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package material.design;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
  

public class CelEditorcheck extends DefaultCellEditor{
    
    JCheckBoxCustomfr boxCustomfr;
    public CelEditorcheck() {
        super(new JTextField());
        boxCustomfr = new JCheckBoxCustomfr() ;    
    }
    
   @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
      super.getTableCellEditorComponent(jtable, o, bln, row, column);
        return boxCustomfr;
    }
    
}

