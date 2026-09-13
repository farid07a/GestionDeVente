/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import entity.ArchiveSuppression;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import material.design.designeTable;
import ui.table.TableCustom;

/**
 *
 * @author pc
 */
public class ArchiveSuppressionForm extends javax.swing.JDialog {

    /**
     * Creates new form NewJDialog
     */
   public ArchiveSuppressionForm(java.awt.Frame parent, boolean modal) {
    super(parent, modal);

    initComponents();

    ArchiveSuppression.afficherDansTableArabe(jTable1);

    setDesignTable(jTable1, jScrollPane1);

    ajusterLargeurColonnes();

    new designeTable().SearchTable(jTable1, txt_search);

    setLocationRelativeTo(this);
}

    private ImageIcon loadIcon(String path) {

        java.net.URL url = getClass().getResource(path);

        if (url == null) {
            System.out.println("Icon introuvable : " + path);
            return null;
        }

        ImageIcon original = new ImageIcon(url);

        Image image = original.getImage().getScaledInstance(
                30,
                30,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(image);
    }

    public void setDesignTable(JTable tab, JScrollPane scrol) {

        TableCustom.apply(scrol, TableCustom.TableType.DEFAULT);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();

        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setBackground(new Color(0, 119, 182));
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 15));

        tab.getTableHeader().setDefaultRenderer(headerRenderer);
        tab.getTableHeader().setPreferredSize(new Dimension(0, 38));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        false,
                        row,
                        column
                );

                // -------------------------------------------------
                // GENERAL DESIGN
                // -------------------------------------------------
                setBorder(noFocusBorder);

                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);

                // Important
                setIcon(null);

                // -------------------------------------------------
                // SELECTED ROW
                // -------------------------------------------------
                if (isSelected) {

                    setBackground(new Color(4, 164, 246));
                    setForeground(Color.WHITE);

                    setFont(
                            new Font(
                                    "Times New Roman",
                                    Font.BOLD,
                                    16
                            )
                    );

                } else {

                    // -------------------------------------------------
                    // ALTERNATE ROW COLORS
                    // -------------------------------------------------
                    setBackground(
                            row % 2 == 0
                                    ? Color.WHITE
                                    : new Color(248, 250, 252)
                    );

                    setForeground(Color.BLACK);

                    setFont(
                            new Font(
                                    "Times New Roman",
                                    Font.PLAIN,
                                    16
                            )
                    );
                }

                // =================================================
                // ICON COLUMN
                // =================================================
                if (column == 2 && value != null) {

                    String type = value.toString();

                    ImageIcon icon = null;

                    // -------------------------------------------------
                    // USER
                    // -------------------------------------------------
                    if (type.equals("مستخدم")) {

                        icon = loadIcon("/icon/icons8-compte-test-64 (1).png");

                        // -------------------------------------------------
                        // CLIENT
                        // -------------------------------------------------
                    } else if (type.equals("زبون")) {

                        icon = loadIcon("/icon/client.png");

                        // -------------------------------------------------
                        // CLIENT PAYE PAR ENTREPRISE
                        // -------------------------------------------------
                    } else if (type.equals("زبون مدفوع من طرف المؤسسة")) {

                        icon = loadIcon("/icon/client.png");

                        // -------------------------------------------------
                        // ENTREPRISE
                        // -------------------------------------------------
                    } else if (type.equals("مؤسسة")) {

                        icon = loadIcon("/icon/company.png");

                        // -------------------------------------------------
                        // PRODUIT
                        // -------------------------------------------------
                    } else if (type.equals("منتج")) {

                        icon = loadIcon("/icon/boite.png");

                        // -------------------------------------------------
                        // CATEGORIE
                        // -------------------------------------------------
                    } else if (type.equals("فئة")) {

                        icon = loadIcon("/icon/check-mark.png");

                        // -------------------------------------------------
                        // ACHAT
                        // -------------------------------------------------
                    } else if (type.equals("مشتريات")) {

                        icon = loadIcon("/icon/icons8-panier-rapide-30.png");

                        // -------------------------------------------------
                        // ACHAT DETAIL
                        // -------------------------------------------------
                    } else if (type.equals("تفاصيل المشتريات")) {

                        icon = loadIcon("/icon/panier2.png");

                        // -------------------------------------------------
                        // VERSEMENT
                        // -------------------------------------------------
                    } else if (type.equals("تسديد")) {

                        icon = loadIcon("/icon/icons8-sac-d'argent-64 (2).png");

                        // -------------------------------------------------
                        // VERSEMENT ENTREPRISE
                        // -------------------------------------------------
                    } else if (type.equals("تسديد المؤسسة")) {

                        icon = loadIcon("/icon/icons8-sac-d'argent-64 (2).png");
                    }

                    // -------------------------------------------------
                    // SET ICON
                    // -------------------------------------------------
                    if (icon != null) {

                        setIcon(icon);

                        // النص + icon في الوسط
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setIconTextGap(8);
                    }
                }

                return this;
            }
        };

        // =========================================================
        // APPLY CELL RENDERER
        // =========================================================
        tab.setDefaultRenderer(Object.class, cellRenderer);

        // =========================================================
        // TABLE SETTINGS
        // =========================================================
        tab.setRowHeight(42);

        tab.setShowHorizontalLines(false);
        tab.setShowVerticalLines(false);

        tab.setIntercellSpacing(new Dimension(0, 0));

        tab.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // =========================================================
        // HEADER SETTINGS
        // =========================================================
        tab.getTableHeader().setReorderingAllowed(false);
        tab.getTableHeader().setResizingAllowed(true);

        // =========================================================
        // SCROLLPANE
        // =========================================================
        scrol.getVerticalScrollBar().setUnitIncrement(16);
    }

private void ajusterLargeurColonnes() {

    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    TableColumnModel columnModel = jTable1.getColumnModel();

    // تاريخ الحذف
    columnModel.getColumn(0).setPreferredWidth(140);
    columnModel.getColumn(0).setMinWidth(120);
    columnModel.getColumn(0).setMaxWidth(160);

    // العنصر المحذوف
    columnModel.getColumn(1).setPreferredWidth(600);
    columnModel.getColumn(1).setMinWidth(300);

    // النوع
    columnModel.getColumn(2).setPreferredWidth(220);
    columnModel.getColumn(2).setMinWidth(160);
    columnModel.getColumn(2).setMaxWidth(280);
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_search = new material.design.SearchTextRound();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(0, 51, 153));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-supprimer-pour-toujours-29.png"))); // NOI18N
        jLabel1.setText("المحذوفات ");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel1.setOpaque(true);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        txt_search.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Informations", "Date suppression"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(312, Short.MAX_VALUE)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(309, 309, 309))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(122, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyReleased
      
    }//GEN-LAST:event_txt_searchKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ArchiveSuppressionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ArchiveSuppressionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ArchiveSuppressionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ArchiveSuppressionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ArchiveSuppressionForm dialog = new ArchiveSuppressionForm(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
