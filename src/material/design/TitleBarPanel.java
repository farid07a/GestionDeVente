package material.design;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TitleBarPanel extends JPanel {

    private JLabel lblTitle;
    private JButton btnClose;

    private Window window;

    private int mouseX;
    private int mouseY;

    public TitleBarPanel() {

        initComponents();
    }

    private void initComponents() {

        setLayout(null);

        setPreferredSize(
                new Dimension(0, 45)
        );

        setBackground(
                new Color(45, 55, 72)
        );

        // =====================================
        // TITLE
        // =====================================

        lblTitle = new JLabel("Title");

        lblTitle.setForeground(
                Color.WHITE
        );

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

        // =====================================
        // CLOSE BUTTON
        // =====================================

        btnClose = new JButton("×");

        btnClose.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        btnClose.setForeground(
                Color.WHITE
        );

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

        // =====================================
        // CLOSE
        // =====================================

        btnClose.addActionListener(e -> {

            if (window != null) {

                window.dispose();
            }
        });

        // =====================================
        // HOVER CLOSE
        // =====================================

        btnClose.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseEntered(
                    MouseEvent e) {

                btnClose.setForeground(
                        new Color(
                                255,
                                90,
                                90
                        )
                );
            }

            @Override
            public void mouseExited(
                    MouseEvent e) {

                btnClose.setForeground(
                        Color.WHITE
                );
            }
        });

        // =====================================
        // DRAG
        // =====================================

        MouseAdapter dragAdapter =
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

                if (window == null) {
                    return;
                }

                Point location =
                        window.getLocation();

                int x =
                        location.x
                        + e.getX()
                        - mouseX;

                int y =
                        location.y
                        + e.getY()
                        - mouseY;

                window.setLocation(
                        x,
                        y
                );
            }
        };

        addMouseListener(
                dragAdapter
        );

        addMouseMotionListener(
                dragAdapter
        );

        // =====================================
        // DRAG TITLE
        // =====================================

        lblTitle.addMouseListener(
                dragAdapter
        );

        lblTitle.addMouseMotionListener(
                dragAdapter
        );
    }

    // =====================================
    // SET WINDOW
    // =====================================

    public void setWindow(Window window) {

        this.window = window;
    }

    // =====================================
    // SET TITLE
    // =====================================

    public void setTitle(String title) {

        lblTitle.setText(title);
    }

    // =====================================
    // GET TITLE
    // =====================================

    public String getTitle() {

        return lblTitle.getText();
    }
}