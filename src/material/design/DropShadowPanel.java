package material.design;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DropShadowPanel extends JPanel {

    private final int MAX_SHADOW_SIZE = 25; // الحجم الثابت للإطار الخارجي
    private int currentShadowSpread = 25;   // انتشار الظل الفعلي (يتغير من 20 إلى 10)
    private float shadowOpacity = 0.35f;
    private Color shadowColor =new Color(220,220,220);
    private int cornerRadius = 15;

    private Timer pulseTimer;
    private float animationStep = 0f;

    public DropShadowPanel() {
        setOpaque(false);
    }
public void pulse() {
    if (pulseTimer != null && pulseTimer.isRunning()) {
        return;
    }

    // قفزات مباشرة خاطفة بدون إطارات متوسطة: 20 -> 10 -> 20 -> 10 -> 20
    final int[] shadowSteps = {20, 10, 20, 10, 20};
    final int[] index = {0};

    // delay = 0 تعني أسرع معالجة ممكنة على الـ Event Dispatch Thread
    pulseTimer = new Timer(3, e -> {
        if (index[0] >= shadowSteps.length) {
            currentShadowSpread = 20;
            paintImmediately(0, 0, getWidth(), getHeight());
            ((Timer) e.getSource()).stop();
            return;
        }

        currentShadowSpread = shadowSteps[index[0]];
        index[0]++;

        // استخدام paintImmediately للرسم الفوري الخاطف وتفادي تأخير repaint
        paintImmediately(0, 0, getWidth(), getHeight());
    });

    // ضبط القيمة الابتدائية وتأخير الـ Cooldown بـ 25ms بين كل ومضة
    pulseTimer.setInitialDelay(0);
    pulseTimer.setDelay(25); 
    pulseTimer.start();
}

//    public void pulse() {
//        if (pulseTimer != null && pulseTimer.isRunning()) {
//            return;
//        }
//
//        animationStep = 0f;
//
//        // دورتان كاملتان (مرتين): Math.PI * 4
//        pulseTimer = new Timer(20, e -> {
//            animationStep += 0.25f; // سرعة الوميض
//
//            if (animationStep >= Math.PI * 4) {
//                animationStep = 0;
//                currentShadowSpread = 20;
//                repaint();
//                ((Timer) e.getSource()).stop();
//                return;
//            }
//
//            // التغير من 20 إلى 10 والعودة إلى 20 بشكل منتظم
//            float sinVal = (float) Math.sin(animationStep); // بين -1 و 1
//            // تحويل القيمة لتردد بين 0 و 1
//            float factor = (1f - sinVal) / 2f; 
//            
//            // حساب انتشار الظل من 20 ينزل إلى 10 ويرجع لـ 20
//            currentShadowSpread = 20 - (int) (factor * 10);
//
//            repaint();
//        });
//
//        pulseTimer.start();
//    }

    @Override
    public Insets getInsets() {
        // تثبيت الـ Insets بالحجم الأقصى حتى لا يتغير حجم الـ Panel أو مكان الـ TitleBar إطلاقاً
        return new Insets(MAX_SHADOW_SIZE, MAX_SHADOW_SIZE, MAX_SHADOW_SIZE, MAX_SHADOW_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() - (MAX_SHADOW_SIZE * 2);
        int height = getHeight() - (MAX_SHADOW_SIZE * 2);
        int x = MAX_SHADOW_SIZE;
        int y = MAX_SHADOW_SIZE;

        // رسم الظل بناءً على currentShadowSpread المتغير بدون تغيير ابعاد الـ Panel
        for (int i = 0; i < currentShadowSpread; i++) {
            float alpha = (shadowOpacity / currentShadowSpread) * (currentShadowSpread - i);
            g2.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), (int) (alpha * 255)));
            g2.fillRoundRect(x - i, y - i, width + (i * 2), height + (i * 2), cornerRadius + i, cornerRadius + i);
        }

        // رسم خلفية الـ Panel الثابتة
        g2.setColor(getBackground());
        g2.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}