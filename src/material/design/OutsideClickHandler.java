
package material.design;

import java.awt.AWTEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

public class OutsideClickHandler {

    private final JDialog dialog;
    private final DropShadowPanel shadowPanel;
    private AWTEventListener mouseListener;

    public OutsideClickHandler(JDialog dialog, DropShadowPanel shadowPanel) {
        this.dialog = dialog;
        this.shadowPanel = shadowPanel;
    }

    public void start() {
        if (mouseListener != null) return;

        mouseListener = event -> {
            if (!(event instanceof MouseEvent)) return;
            MouseEvent e = (MouseEvent) event;

            // كشف الضغط الخارجي فور النقرة MOUSE_PRESSED
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                if (!dialog.isShowing()) return;

                Point mouse = MouseInfo.getPointerInfo().getLocation();
                
                // حساب المساحة الفعلية الملونة للـ Panel باستثناء حواف الظل الشفافة (20px)
                Rectangle bounds = dialog.getBounds();
                int shadowSize = 20; // نفس MAX_SHADOW_SIZE في DropShadowPanel
                
                Rectangle innerBounds = new Rectangle(
                        bounds.x + shadowSize,
                        bounds.y + shadowSize,
                        bounds.width - (shadowSize * 2),
                        bounds.height - (shadowSize * 2)
                );

                // إذا كانت النقرة خارج نطاق المحتوى الداخلي
                if (!innerBounds.contains(mouse)) {
                    SwingUtilities.invokeLater(() -> shadowPanel.pulse());
                }
            }
        };

        Toolkit.getDefaultToolkit().addAWTEventListener(
                mouseListener, 
                AWTEvent.MOUSE_EVENT_MASK
        );
    }

    public void stop() {
        if (mouseListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(mouseListener);
            mouseListener = null;
        }
    }
} 
//package material.design;
//
//import java.awt.AWTEvent;
//import java.awt.MouseInfo;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.Toolkit;
//import java.awt.event.AWTEventListener;
//import java.awt.event.MouseEvent;
//import javax.swing.JDialog;
//
//public class OutsideClickHandler {
//
//    private final JDialog dialog;
//    private final DropShadowPanel shadowPanel;
//    private AWTEventListener mouseListener;
//
//    public OutsideClickHandler(JDialog dialog, DropShadowPanel shadowPanel) {
//        this.dialog = dialog;
//        this.shadowPanel = shadowPanel;
//    }
//
//    public void start() {
//        if (mouseListener != null) return;
//
//        mouseListener = new AWTEventListener() {
//            @Override
//            public void eventDispatched(AWTEvent event) {
//                if (!(event instanceof MouseEvent)) return;
//                
//                MouseEvent e = (MouseEvent) event;
//
//                // التقاط الضغطات عند MOUSE_PRESSED
//                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
//                    if (!dialog.isShowing()) return;
//
//                    Point mouse = MouseInfo.getPointerInfo().getLocation();
//                    Rectangle bounds = dialog.getBounds();
//
//                    // إذا كان النقر خارج حدود النافذة
//                    if (!bounds.contains(mouse)) {
//                        shadowPanel.pulse();
//                    }
//                }
//            }
//        };
//
//        // استخدام MOUSE_EVENT_MASK و MOUSE_MOTION_EVENT_MASK للالتقاط حتى مع Modal
//        Toolkit.getDefaultToolkit().addAWTEventListener(
//                mouseListener, 
//                AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK
//        );
//    }
//
//    public void stop() {
//        if (mouseListener != null) {
//            Toolkit.getDefaultToolkit().removeAWTEventListener(mouseListener);
//            mouseListener = null;
//        }
//    }
//}