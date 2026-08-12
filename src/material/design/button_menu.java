package material.design;


import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;


public class button_menu extends JButton{

    JButton [] button;
    public button_menu() {
    
        setBackground(new java.awt.Color(255, 255, 255));
        //setFont(new java.awt.Font("Verdana", Font.BOLD, 14)); // NOI18N
        setFont(new java.awt.Font("Tahoma",Font.BOLD, 12));
        setText("jButton1");
        setForeground(new Color(153,153,153));
        setBorder(null);
        setContentAreaFilled(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setBorder( BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(153,153,153)));
    }
  
}
