/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

import javax.swing.*;
import java.awt.*;

public class ActionButtonsPanel extends JPanel {
    public JButton btnAdd = new JButton("+");
    public JButton btnMinus = new JButton("-");
    public JButton btnDelete = new JButton("🗑️"); // يمكنك استبدالها بـ ImageIcon

    public ActionButtonsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(true);
        setBackground(Color.WHITE);

        // تحسين مظهر الأزرار لتبدو فخمة وعصرية
        configureButton(btnAdd, new Color(34, 197, 94));   // أخضر
        configureButton(btnMinus, new Color(234, 179, 8)); // أصفر
        configureButton(btnDelete, new Color(239, 68, 68)); // أحمر

        // إضافة الأزرار للوحة مع مسافات صغيرة بينها
        add(Box.createHorizontalGlue());
        add(btnAdd);
        add(Box.createHorizontalStrut(5));
        add(btnMinus);
        add(Box.createHorizontalStrut(5));
        add(btnDelete);
        add(Box.createHorizontalGlue());
    }

    private void configureButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(color);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}