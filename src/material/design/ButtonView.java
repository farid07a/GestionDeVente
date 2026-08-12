/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package material.design;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/**
 *
 * @author horizon
 */
public class ButtonView extends JButton implements MouseListener{
    
    private boolean rounded;
    private boolean backgroundPainted;
    private boolean linePainted;
    private boolean entred;
    private boolean pressed;
    
    private Color entredColor;
    private Color pressedColor;
    private Color gradientBackgroundColor;
    private Color gradientLineColor;
    private Color lineColor;

    public ButtonView() {
    super();
    rounded=false;
    backgroundPainted=false;
    linePainted=false;
    entred=false;
    pressed=false;
    
    entredColor=getBackground().brighter();
    pressedColor=getBackground().darker();
    lineColor=Color.BLACK;
    setContentAreaFilled(false);
    setFocusPainted(false);
        addMouseListener(this);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg); //To change body of generated methods, choose Tools | Templates.
        entredColor=bg.brighter();
        pressedColor=bg.darker();
    }

    @Override
    protected void paintComponent(Graphics g) {
         Graphics2D g2=(Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s=(rounded)? new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, getHeight()-2 ,getHeight()-2)
                :new Rectangle2D.Float(1, 1, getWidth()-2, getHeight()-2);
        
        if (backgroundPainted || (pressed && !backgroundPainted)) {
            if (gradientBackgroundColor==null) {
                g2.setColor(color());
            }else{
                GradientPaint gradPaint=new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), gradientBackgroundColor);
            }
            g2.fill(s);
        }
        if (linePainted) {
            if (gradientLineColor==null) {
                g2.setColor(isEnabled()?lineColor:new Color(204, 204, 204));
            }else {
            GradientPaint gradientpaint=new GradientPaint(0, 0, lineColor, getWidth(), getHeight(), gradientLineColor);
            g2.setPaint(gradientpaint);
            }
            g2.draw(s);
        }
        
        
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
/*
    @Override
    protected void paintComp(Graphics g) {
        
        Graphics2D g2=(Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s=(rounded)? new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, getHeight()-2 ,getHeight()-2)
                :new Rectangle2D.Float(1, 1, getWidth()-2, getHeight()-2);
        
        if (backgroundPainted || (pressed && !backgroundPainted)) {
            if (gradientBackgroundColor==null) {
                g2.setColor(color());
            }else{
                GradientPaint gradPaint=new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), gradientBackgroundColor);
            }
            g2.fill(s);
        }
        if (linePainted) {
            if (gradientLineColor==null) {
                g2.setColor(isEnabled()?lineColor:new Color(204, 204, 204));
            }else {
            GradientPaint gradientpaint=new GradientPaint(0, 0, lineColor, getWidth(), getHeight(), gradientLineColor);
            g2.setPaint(gradientpaint);
            }
            g2.draw(s);
        }
        super.paintChildren(g);
        
    }
*/
    private Color color(){
        if (!isEnabled()) {
            return new Color(204, 204, 204);
            
        }
        Color temp=getBackground();
        if (pressed) return pressedColor;
        if (entred ) return entredColor;
        
        
        return temp;
        
    
    }
    
    
    
    
    
    /**
     * @param rounded the rounded to set
     */
    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }

    /**
     * @param backgroundPainted the backgroundPainted to set
     */
    public void setBackgroundPainted(boolean backgroundPainted) {
        this.backgroundPainted = backgroundPainted;
    }

    /**
     * @param linePainted the linePainted to set
     */
    public void setLinePainted(boolean linePainted) {
        this.linePainted = linePainted;
    }

    /**
     * @param entred the entred to set
     */
    public void setEntred(boolean entred) {
        this.entred = entred;
    }

    /**
     * @param pressed the pressed to set
     */
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    /**
     * @param entredColor the entredColor to set
     */
    public void setEntredColor(Color entredColor) {
        this.entredColor = entredColor;
    }

    /**
     * @param pressedColor the pressedColor to set
     */
    public void setPressedColor(Color pressedColor) {
        this.pressedColor = pressedColor;
    }

    /**
     * @param gradientBackgroundColor the gradientBackgroundColor to set
     */
    public void setGradientBackgroundColor(Color gradientBackgroundColor) {
        this.gradientBackgroundColor = gradientBackgroundColor;
    }

    /**
     * @param gradientLineColor the gradientLineColor to set
     */
    public void setGradientLineColor(Color gradientLineColor) {
        this.gradientLineColor = gradientLineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    pressed=true;
    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    pressed=false;    
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    entred=true;
    this.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
    }

    @Override
    public void mouseExited(MouseEvent e) {
    entred=false;    
    this.setCursor( Cursor.getDefaultCursor() );
    }

 
    
    
    
}
