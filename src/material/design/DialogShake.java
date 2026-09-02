package material.design;

import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.Timer;

public class DialogShake {

    private final JDialog dialog;
    private Timer timer;

    public DialogShake(JDialog dialog) {
        this.dialog = dialog;
    }

    public void shakeDialog() {
        if (dialog == null || !dialog.isVisible()) return;

        // إيقاف أي اهتزاز سابق لمنع التضارب عند الضغط السريع
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        final int originalX = dialog.getX();
        final int originalY = dialog.getY();
        final int[] offsets = {-10, 10, -8, 8, -5, 5, -3, 3, 0};
        final int[] index = {0};

        timer = new Timer(20, (ActionEvent e) -> {
            if (index[0] >= offsets.length) {
                dialog.setLocation(originalX, originalY);
                timer.stop();
                return;
            }

            dialog.setLocation(originalX + offsets[index[0]], originalY);
            index[0]++;
        });

        timer.start();
    }
}