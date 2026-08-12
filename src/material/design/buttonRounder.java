package material.design;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class buttonRounder extends JButton {

    public Color getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
    }

    private Animator animator;
    private int targetSize;
    private float animatSize;
    private Point pressedPoint;
    private float alpha;
    // private Color effectColor = new Color(204,204,204);
    private Color effectColor = new Color(255, 255, 255);
    // private Color effectColor = new Color(215, 215, 215);

    public buttonRounder() {

        setFont(new Font("Tahoma", Font.BOLD, 13));
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeIcon();
            }
        });   // icon change wid-hei
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                targetSize = Math.max(getWidth(), getHeight()) * 2;
                animatSize = 0;
                pressedPoint = me.getPoint();
                alpha = 0.5f;
                if (animator.isRunning()) {
                    animator.stop();
                }
                animator.start();
            }
        });
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (fraction > 0.5f) {
                    alpha = 1 - fraction;
                }
                animatSize = fraction * targetSize;
                repaint();
            }
        };
        animator = new Animator(700, target);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0);
        setHorizontalTextPosition(SwingConstants.LEADING);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        int width = getWidth();
        int height = getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, 15, 15);
        if (pressedPoint != null) {
            g2.setColor(effectColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g2.fillOval((int) (pressedPoint.x - animatSize / 2), (int) (pressedPoint.y - animatSize / 2), (int) animatSize, (int) animatSize);
        }
        g2.dispose();
        grphcs.drawImage(img, 0, 0, null);
        super.paintComponent(grphcs);
    }
   private ImageIcon originalIcon;

@Override
public void setIcon(Icon icon) {
    if (icon instanceof ImageIcon) {
        originalIcon = (ImageIcon) icon;

        if (getWidth() > 0 && getHeight() > 0) {
            resizeIcon();
        } else {
            super.setIcon(icon);
        }
    } else {
        super.setIcon(icon);
    }
}

private void resizeIcon() {
  int width = getWidth();
    int height = getHeight();

    // شرط الحماية: التحقق من أن العرض والارتفاع أكبر من الصفر
    if (width <= 0 || height <= 0) {
        return; // إلغاء العملية مؤقتاً حتى يتلقى الزر قياساته الصحيحة
    }

    if (originalIcon == null) {
        return;
    }
      if (getIcon() != null) {

    // حجم الأيقونة = 50% من ارتفاع الزر
    int size =(int) (Math.min(getWidth(), getHeight()) * 0.80);

    Image img = originalIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
    super.setIcon(new ImageIcon(img));
      }
}


}
