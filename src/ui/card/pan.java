package ui.card;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import material.design.ScrollBar;
import material.design.designeTable;
import ui.table.TableCustom;

public class pan extends javax.swing.JPanel {

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public Color getColor1() {
        return color1;
    }

    public Color getColor2() {
        return color2;
    }
    private Color color1;
    private Color color2;

    public pan() {
        initComponents();
        setOpaque(false);
        color1 = Color.BLACK;
        color2 = Color.WHITE;
 
  
        new designeTable().setDesignTable(table_seance_to_day, jScrollPane11);
        table_seance_to_day.setRowHeight(35);
        resizeColumnWidth(table_seance_to_day);
        table_seance_to_day.setShowVerticalLines(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        labDate_seance.setText(LocalDate.now().format(formatter));
        lab_day.setText(LocalDate.now().getDayOfMonth()+"");
    }
    
    public JTable getTtable_seance_to_day(){
        return table_seance_to_day;
    
    }
    
    public static void resizeColumnWidth(JTable table) {
    final int margin = 10; // مسافة صغيرة لزيادة الوضوح

    for (int col = 0; col < table.getColumnCount(); col++) {
        TableColumn column = table.getColumnModel().getColumn(col);
        int maxWidth = 50; // حد أدنى للعرض

        // 🔸 أولاً: حساب العرض من العنوان (header)
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Component headerComp = headerRenderer.getTableCellRendererComponent(
                table, column.getHeaderValue(), false, false, 0, col);
        maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width);

        // 🔸 ثانياً: حساب العرض من الخانات (cells)
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
            Component comp = table.prepareRenderer(cellRenderer, row, col);
            maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
        }

        // 🔸 إضافة المارجن
        column.setPreferredWidth(maxWidth + margin);
    }
}

    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        labDate_seance = new javax.swing.JLabel();
        tableScrollButton11 = new ui.table.TableScrollButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        table_seance_to_day = new javax.swing.JTable();
        lab_day = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonRounder1 = new material.design.buttonRounder();

        setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("حصــص الــيــوم");
        add(jLabel1);
        jLabel1.setBounds(130, 0, 120, 40);

        labDate_seance.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labDate_seance.setForeground(new java.awt.Color(255, 255, 255));
        labDate_seance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labDate_seance.setText("01-01-2024");
        add(labDate_seance);
        labDate_seance.setBounds(20, 0, 100, 40);

        table_seance_to_day.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idS_I", "N", "التوقيت", "التوقيت", "رقم الحصة", "الفوج", "الأستاذ", "المادة ", "القسم"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(table_seance_to_day);

        tableScrollButton11.add(jScrollPane11, java.awt.BorderLayout.CENTER);

        add(tableScrollButton11);
        tableScrollButton11.setBounds(12, 80, 550, 480);

        lab_day.setBackground(new java.awt.Color(255, 255, 255));
        lab_day.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lab_day.setForeground(new java.awt.Color(204, 204, 204));
        lab_day.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lab_day.setText("01");
        lab_day.setOpaque(true);
        add(lab_day);
        lab_day.setBounds(420, 50, 20, 20);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/daily-routine (1)_1.png"))); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(410, 0, 80, 70);

        buttonRounder1.setBackground(new java.awt.Color(235, 235, 235));
        buttonRounder1.setForeground(new java.awt.Color(255, 255, 255));
        buttonRounder1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/fast-time (4).png"))); // NOI18N
        buttonRounder1.setText("حصص قيد الدراسة");
        add(buttonRounder1);
        buttonRounder1.setBounds(10, 40, 160, 30);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        //cercl white transparent
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillOval(getWidth() - (getHeight() / 2), 10, getHeight(), getHeight());
        g2.fillOval(getWidth() - (getHeight() / 2) - 20, getHeight() / 2 + 20, getHeight(), getHeight());

        super.paintComponent(grphcs);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private material.design.buttonRounder buttonRounder1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JLabel labDate_seance;
    private javax.swing.JLabel lab_day;
    private ui.table.TableScrollButton tableScrollButton11;
    private javax.swing.JTable table_seance_to_day;
    // End of variables declaration//GEN-END:variables
}
