/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package material.design;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import javax.swing.JPanel;

import javax.swing.JDialog;

public class TitleBarPanel1 extends JPanel {

    private  JDialog dialog;

    private JLabel lblTitle;
    private JButton btnClose;

    private int mouseX;
    private int mouseY;

    private AWTEventListener outsideMouseListener;

    
    
    public TitleBarPanel1(
            JDialog dialog,
            String title) {

        this.dialog = dialog;

        initComponents();

        lblTitle.setText(title);

        initDrag();

        initOutsideClick();

        initCloseListener();
    }

    private void initComponents() {

        setLayout(null);

        setBackground(
                new Color(45, 55, 72)
        );

        setPreferredSize(
                new Dimension(0, 45)
        );

        // =========================
        // TITLE
        // =========================

        lblTitle = new JLabel();

        lblTitle.setText("Title");

        lblTitle.setForeground(Color.WHITE);

        lblTitle.setFont(
                new Font(
                        "Cairo",
                        Font.BOLD,
                        14
                )
        );

        lblTitle.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        lblTitle.setBounds(
                60,
                0,
                300,
                45
        );

        add(lblTitle);

        // =========================
        // CLOSE
        // =========================

        btnClose = new JButton();

        btnClose.setText("×");

        btnClose.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        btnClose.setForeground(Color.WHITE);

        btnClose.setBorderPainted(false);

        btnClose.setFocusPainted(false);

        btnClose.setContentAreaFilled(false);

        btnClose.setBounds(
                0,
                0,
                50,
                45
        );

        add(btnClose);

        // Hover
        btnClose.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseEntered(
                    MouseEvent e) {

                btnClose.setForeground(
                        new Color(255, 90, 90)
                );
            }

            @Override
            public void mouseExited(
                    MouseEvent e) {

                btnClose.setForeground(
                        Color.WHITE
                );
            }
        }
        );

        // Close
        btnClose.addActionListener(e -> {

            dialog.dispose();

        });
    }

    // =====================================================
    // تحريك الـ Dialog
    // =====================================================

    private void initDrag() {

        MouseAdapter adapter =
                new MouseAdapter() {

            @Override
            public void mousePressed(
                    MouseEvent e) {

                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseDragged(
                    MouseEvent e) {

                Point location =
                        dialog.getLocation();

                int x =
                        location.x
                        + e.getX()
                        - mouseX;

                int y =
                        location.y
                        + e.getY()
                        - mouseY;

                dialog.setLocation(x, y);
            }
        };

        addMouseListener(adapter);

        addMouseMotionListener(adapter);

        lblTitle.addMouseListener(adapter);

        lblTitle.addMouseMotionListener(adapter);
    }

    // =====================================================
    // الضغط خارج الـ Dialog
    // =====================================================

    private void initOutsideClick() {

        outsideMouseListener =
                new AWTEventListener() {

            @Override
            public void eventDispatched(
                    AWTEvent event) {

                if (!(event instanceof MouseEvent)) {
                    return;
                }

                MouseEvent e =
                        (MouseEvent) event;

                if (e.getID()
                        != MouseEvent.MOUSE_PRESSED) {

                    return;
                }

                if (!dialog.isVisible()) {
                    return;
                }

                Point mouse =
                        MouseInfo
                                .getPointerInfo()
                                .getLocation();

                Rectangle bounds =
                        dialog.getBounds();

                if (!bounds.contains(mouse)) {

                    shakeDialog();
                }
            }
        };

        Toolkit
                .getDefaultToolkit()
                .addAWTEventListener(
                        outsideMouseListener,
                        AWTEvent.MOUSE_EVENT_MASK
                );
    }

    // =====================================================
    // SHAKE
    // =====================================================
private void shakeDialog() {

    final int originalX = dialog.getX();
    final int originalY = dialog.getY();

    new Thread(() -> {

        try {

            int[] offsets = {
                -8, 8, -8, 8, -5, 5, -3, 3, 0
            };

            for (int offset : offsets) {

                SwingUtilities.invokeLater(() -> {

                    dialog.setLocation(
                            originalX + offset,
                            originalY
                    );
                });

                Thread.sleep(35);
            }

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();
        }

    }).start();
}
    
    
    
//    private void shakeDialog() {
//
//        final int originalX =
//                dialog.getX();
//
//        final int originalY =
//                dialog.getY();
//
//        new Thread(() -> {
//
//            try {
//
//                int[] positions = {
//                    -8, 8, -6, 6, -4, 4, 0
//                };
//
//                for (int offset : positions) {
//
//                    SwingUtilities.invokeLater(() -> {
//
//                        dialog.setLocation(
//                                originalX,
//                                originalY
//                        );
//
//                    });
//
//                    Thread.sleep(35);
//                }
//
//                SwingUtilities.invokeLater(() -> {
//
//                    dialog.setLocation(
//                            originalX,
//                            originalY
//                    );
//
//                });
//
//            } catch (InterruptedException ex) {
//
//                Thread.currentThread()
//                        .interrupt();
//            }
//
//        }).start();
//    }

    // =====================================================
    // حذف Listener عند إغلاق Dialog
    // =====================================================

    private void initCloseListener() {

        dialog.addWindowListener(
                new WindowAdapter() {

            @Override
            public void windowClosed(
                    WindowEvent e) {

                Toolkit
                        .getDefaultToolkit()
                        .removeAWTEventListener(
                                outsideMouseListener
                        );
            }

            @Override
            public void windowClosing(
                    WindowEvent e) {

                Toolkit
                        .getDefaultToolkit()
                        .removeAWTEventListener(
                                outsideMouseListener
                        );
            }
        }
        );
    }

    // =====================================================
    // تغيير العنوان
    // =====================================================

    public void setTitle(String title) {

        lblTitle.setText(title);
    }
public TitleBarPanel1() {
    initComponents();
}

}