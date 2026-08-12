package material.design;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class Table extends JTable{
    
    public Table(){
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setShowHorizontalLines(true); //show lines
        setGridColor(new Color(230,230,230)); // color lines 
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);// No change cln tab
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object ob, boolean isSelected, boolean hasFocus, int row, int column) {
                   TableHeader header = new TableHeader(ob+"");
                   if(column == 4){
                       header.setHorizontalAlignment(JLabel.CENTER);
                   }
                   return header;
                }
                
        
        } );
        
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object ob, boolean isSelected, boolean hasFocus, int row, int column) {
//                if (column !=4){
//                    Component com=  super.getTableCellRendererComponent(table, ob, isSelected, hasFocus, row, column);
//                    com.setBackground(Color.WHITE);
//                    setBorder(noFocusBorder);
//                    if(isSelected){
//                        com.setForeground(new Color(15,89,140));
//                    }else{
//                        com.setForeground(new Color(102,102,102));
//                    }
//                    return com;
//                }
//               // return new  JLabel("Testing");status 
//                else{
//                    StatusType type= (StatusType) ob;
//                    CellStatus cell =new CellStatus(type);
//                    return cell;
//                }
            Component com=  super.getTableCellRendererComponent(table, ob, isSelected, hasFocus, row, column);
                    com.setBackground(Color.WHITE);
                    setBorder(noFocusBorder);
                    if(isSelected){
                        com.setForeground(new Color(15,89,140));
                    }else{
                        com.setForeground(new Color(102,102,102));
                    }
                    return com;
            }
        });
           
        
        
    }
    public void addRow(Object[] row){
        DefaultTableModel model= (DefaultTableModel) getModel();
        model.addRow(row);
    
    }
}
