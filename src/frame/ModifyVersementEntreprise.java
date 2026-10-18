/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import DialogFram.Exite;
import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import config.DatabaseConnection;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Nomber;
import entity.VersementEntreprise;
import enums.ModePaiement;
import home.HomeForm;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class ModifyVersementEntreprise extends javax.swing.JDialog {

    /**
     * Creates new form ModifyVersementEntreprise
     */
    HomeForm homeForm;
    VersementEntreprise versementEntreprise;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    VersementEntreprise previousVersement = null;
    Connection connection;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    Nomber Nomber = new Nomber();
    Exite exite;
    MessageDialog messageDialog;
    ValidationMessageDialog validationMessageDialog;

    public ModifyVersementEntreprise(java.awt.Frame parent, boolean modal, VersementEntreprise versementEntreprise) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        this.versementEntreprise = versementEntreprise;
        initComponents();
        connection = DatabaseConnection.getInstance().getConnection();
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        exite = new Exite(this, homeForm);
        messageDialog = new MessageDialog(this);
        validationMessageDialog = new ValidationMessageDialog(this, homeForm);

        previousVersement = versementEntrepriseDAOImpl.getPreviousVersementEntreprise(versementEntreprise);
        SetInfoVersementInComponent();
        this.setLocationRelativeTo(null);
    }

    public void SetInfoVersementInComponent() {
        labNomEntreprise.setText(versementEntreprise.getEntreprise().getNom_ar());
        txtPrixVersement.setText(formatter.format(versementEntreprise.getMontant()));
        txtcreditVersement.setText(versementEntreprise.getReste_credit() >= 0 ? formatter.format(versementEntreprise.getReste_credit()) + "" : "0.00");
        labAugVersement.setText(versementEntreprise.getReste_credit() < 0 ? formatter.format(versementEntreprise.getReste_credit() * -1) + "" : "0.00");
        txtDateDeb.setText(versementEntreprise.getDate_versement() + "");
        if (previousVersement != null) {
            double previousCredit = previousVersement.getReste_credit();
            labCredit.setText((previousCredit >= 0) ? previousVersement.getReste_credit() + "" : "0.00");
            labAugment.setText((previousCredit < 0) ? previousVersement.getReste_credit() + "" : "0.00");
        }
        labAchat.setText(formatter.format(versementEntreprise.getTotal_credit()));
        String etat = "";
        System.out.println("" + versementEntrepriseDAOImpl.getEtat(versementEntreprise));
        if (versementEntrepriseDAOImpl.getEtat(versementEntreprise) == null) {
            chekLastVersement.setVisible(false);
        } else {
            etat = versementEntrepriseDAOImpl.getEtat(versementEntreprise);
            if (etat.equals("oui")) {
                chekLastVersement.setSelected(true);
            } else {
                chekLastVersement.setSelected(false);
            }
        }

    }

    public void recalculerResteCredit(VersementEntreprise versement) {
        List<VersementEntreprise> versements = versementEntrepriseDAOImpl.getALLNextVersementsEntreprise(versement);
        boolean allUpdate = true;
        //    versements.add(0, versement);
        for (VersementEntreprise v : versements) {
            VersementEntreprise previous = versementEntrepriseDAOImpl.getPreviousVersementEntreprise(v);
            double restePrecedent = (previous != null) ? previous.getReste_credit() : 0;
            double reste = v.getTotal_credit()
                    + restePrecedent
                    - v.getMontant();

            v.setReste_credit(reste);

            if (versementEntrepriseDAOImpl.update(v) < 0) {
                allUpdate = false;
            } else {
                System.out.println("update versement id " + v.getId());
            }

        }
        if (allUpdate) {
            validationMessageDialog.showMessagetoDialog("تأكيد", "تـم تحديث الدفعات بعد هذه الدفعة بـنـجـاح");
        } else {
            exite.showMessageDialog("خطأ", " حدث خطأ في تحديث الدفعات");
        }
    }

//     public void calculeAchatAndCredit() {
//
//        double prixAchat = Nomber.getNbDouble( (txtPrixVersement.getText().isEmpty())? "0.00":txtPrixVersement.getText());
//        
//        double CreditLsatVersemnt = Nomber.getNbDouble(labCredit.getText());
//        double prixAchatAndCredit = prixAchat + CreditLsatVersemnt;
//        labAchatCredit.setText(formatter.format(prixAchatAndCredit));
//    }
    public void calculeNewCredit() {
        double MontantVersement = Nomber.getNbDouble((txtPrixVersement.getText().isEmpty()) ? "0.00" : txtPrixVersement.getText());
        double prixAchatAndLastCredit = Nomber.getNbDouble(labCredit.getText()) + Nomber.getNbDouble(labAchat.getText());
        double prixLastAugment = Nomber.getNbDouble(labAugment.getText());
        double newcredit = prixAchatAndLastCredit - (MontantVersement + prixLastAugment);
        txtcreditVersement.setText(newcredit >= 0 ? formatter.format(newcredit) : "00.00");
        labAugVersement.setText(newcredit < 0 ? formatter.format(newcredit * -1) : "00.00");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new datechooser.DateChooser();
        jPanel1 = new javax.swing.JPanel();
        labNomEntreprise = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPrixVersement = new ui.card.TextFieldPrice();
        panRound23 = new ui.card.panRound2();
        jLabel10 = new javax.swing.JLabel();
        labCredit = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        pan_gradiant8 = new ui.card.pan_gradiant();
        labAchat = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDateDeb = new material.design.TextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btn_save = new material.design.buttonRounder();
        btn_annuler = new material.design.buttonRounder();
        panRound24 = new ui.card.panRound2();
        jLabel11 = new javax.swing.JLabel();
        labAugment = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        pan_gradiant1 = new ui.card.pan_gradiant();
        jLabel17 = new javax.swing.JLabel();
        txtcreditVersement = new javax.swing.JLabel();
        pan_gradiant5 = new ui.card.pan_gradiant();
        labAugVersement = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        chekLastVersement = new material.design.JCheckBoxCustomAR();

        dateChooser1.setForeground(new java.awt.Color(51, 153, 255));
        dateChooser1.setTextRefernce(txtDateDeb);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labNomEntreprise.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labNomEntreprise.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labNomEntreprise.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 153)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setText("الشركة :");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel3.setText("مبلغ الدفعة");

        txtPrixVersement.setBackground(new java.awt.Color(235, 235, 255));
        txtPrixVersement.setForeground(new java.awt.Color(0, 0, 0));
        txtPrixVersement.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrixVersement.setText("0.00");
        txtPrixVersement.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        txtPrixVersement.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPrixVersementFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrixVersementFocusLost(evt);
            }
        });
        txtPrixVersement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrixVersementActionPerformed(evt);
            }
        });
        txtPrixVersement.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrixVersementKeyReleased(evt);
            }
        });

        panRound23.setColor1(new java.awt.Color(255, 238, 238));
        panRound23.setColor2(new java.awt.Color(255, 238, 238));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("دين سابق قبل الدفعة");

        labCredit.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labCredit.setForeground(new java.awt.Color(255, 0, 0));
        labCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCredit.setText("0.00");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setText("دج");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N

        javax.swing.GroupLayout panRound23Layout = new javax.swing.GroupLayout(panRound23);
        panRound23.setLayout(panRound23Layout);
        panRound23Layout.setHorizontalGroup(
            panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addGap(26, 26, 26)
                .addComponent(labCredit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panRound23Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addGap(32, 32, 32))
        );
        panRound23Layout.setVerticalGroup(
            panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound23Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound23Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labCredit)
                            .addComponent(jLabel15)))
                    .addComponent(jLabel23))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_gradiant8.setColor1(new java.awt.Color(221, 221, 255));
        pan_gradiant8.setColor2(new java.awt.Color(221, 221, 255));

        labAchat.setBackground(new java.awt.Color(226, 250, 202));
        labAchat.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labAchat.setForeground(new java.awt.Color(0, 102, 204));
        labAchat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAchat.setText("0.00");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setText("مشتريات الدفعة");

        javax.swing.GroupLayout pan_gradiant8Layout = new javax.swing.GroupLayout(pan_gradiant8);
        pan_gradiant8.setLayout(pan_gradiant8Layout);
        pan_gradiant8Layout.setHorizontalGroup(
            pan_gradiant8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labAchat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pan_gradiant8Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jLabel4)
                .addContainerGap(124, Short.MAX_VALUE))
        );
        pan_gradiant8Layout.setVerticalGroup(
            pan_gradiant8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_gradiant8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labAchat)
                .addGap(2, 2, 2))
        );

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setText("تاريخ الدفعة");

        txtDateDeb.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDateDeb.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtDateDeb.setLabelText("");
        txtDateDeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateDebActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setText("المبلغ الزائد من الدفعة");

        jLabel1.setBackground(new java.awt.Color(43, 43, 140));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("تعديل الدفعة : مبلغ الدفعة - التاريخ  ");
        jLabel1.setOpaque(true);

        btn_save.setBackground(new java.awt.Color(22, 163, 74));
        btn_save.setForeground(new java.awt.Color(255, 255, 255));
        btn_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-coche-emoji-48.png"))); // NOI18N
        btn_save.setText("تأكيد");
        btn_save.setFont(new java.awt.Font("Cairo", 1, 16)); // NOI18N
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_annuler.setBackground(new java.awt.Color(239, 68, 68));
        btn_annuler.setForeground(new java.awt.Color(255, 255, 255));
        btn_annuler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-cross-mark-48 (1).png"))); // NOI18N
        btn_annuler.setText("الغاء");
        btn_annuler.setFont(new java.awt.Font("Cairo", 1, 16)); // NOI18N
        btn_annuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_annulerActionPerformed(evt);
            }
        });

        panRound24.setColor1(new java.awt.Color(204, 255, 204));
        panRound24.setColor2(new java.awt.Color(204, 255, 204));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("دين سابق قبل الدفعة");

        labAugment.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labAugment.setForeground(new java.awt.Color(255, 0, 0));
        labAugment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAugment.setText("0.00");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setText("دج");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N

        javax.swing.GroupLayout panRound24Layout = new javax.swing.GroupLayout(panRound24);
        panRound24.setLayout(panRound24Layout);
        panRound24Layout.setHorizontalGroup(
            panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addGap(26, 26, 26)
                .addComponent(labAugment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panRound24Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(32, 32, 32))
        );
        panRound24Layout.setVerticalGroup(
            panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound24Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound24Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labAugment)
                            .addComponent(jLabel16)))
                    .addComponent(jLabel24))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_gradiant1.setBackground(new java.awt.Color(255, 246, 246));
        pan_gradiant1.setColor1(new java.awt.Color(255, 241, 241));
        pan_gradiant1.setColor2(new java.awt.Color(253, 232, 232));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 51));
        jLabel17.setText("دج");

        txtcreditVersement.setBackground(new java.awt.Color(255, 229, 229));
        txtcreditVersement.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtcreditVersement.setForeground(new java.awt.Color(255, 0, 51));
        txtcreditVersement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtcreditVersement.setText("0.00");

        javax.swing.GroupLayout pan_gradiant1Layout = new javax.swing.GroupLayout(pan_gradiant1);
        pan_gradiant1.setLayout(pan_gradiant1Layout);
        pan_gradiant1Layout.setHorizontalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtcreditVersement, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addContainerGap())
        );
        pan_gradiant1Layout.setVerticalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcreditVersement)
                    .addComponent(jLabel17))
                .addGap(11, 16, Short.MAX_VALUE))
        );

        pan_gradiant5.setColor1(new java.awt.Color(231, 246, 216));
        pan_gradiant5.setColor2(new java.awt.Color(231, 246, 216));

        labAugVersement.setBackground(new java.awt.Color(226, 250, 202));
        labAugVersement.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labAugVersement.setForeground(new java.awt.Color(51, 153, 0));
        labAugVersement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAugVersement.setText("0.00");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 153, 0));
        jLabel19.setText("دج");

        javax.swing.GroupLayout pan_gradiant5Layout = new javax.swing.GroupLayout(pan_gradiant5);
        pan_gradiant5.setLayout(pan_gradiant5Layout);
        pan_gradiant5Layout.setHorizontalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labAugVersement, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
        );
        pan_gradiant5Layout.setVerticalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labAugVersement)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setText("دين الباقي من الدفعة");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setText("دج");

        chekLastVersement.setBackground(new java.awt.Color(51, 204, 0));
        chekLastVersement.setText("أخر دفعة قديمة لشركة");
        chekLastVersement.setActionCommand("أخر دفعة استـثـنـائيــة");
        chekLastVersement.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        chekLastVersement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chekLastVersementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pan_gradiant8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panRound23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_gradiant5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_gradiant1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labNomEntreprise, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrixVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chekLastVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDateDeb, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labNomEntreprise, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrixVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pan_gradiant1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pan_gradiant5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                            .addComponent(txtDateDeb, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chekLastVersement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panRound23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panRound24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pan_gradiant8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPrixVersementFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrixVersementFocusGained
        if (txtPrixVersement.getText().equals("00.00")
                || txtPrixVersement.getText().equals("00")
                || txtPrixVersement.getText().equals("0")
                || txtPrixVersement.getText().equals("0.00")) {
            txtPrixVersement.setText("");
        }
    }//GEN-LAST:event_txtPrixVersementFocusGained

    private void txtPrixVersementFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrixVersementFocusLost
        if (txtPrixVersement.getText().isEmpty())
            txtPrixVersement.setText("0.00");
    }//GEN-LAST:event_txtPrixVersementFocusLost

    private void txtPrixVersementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrixVersementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrixVersementActionPerformed

    private void txtPrixVersementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrixVersementKeyReleased
//        if (txtPrixVersement.getText().isEmpty()) {
//            return;
//        }
        calculeNewCredit();
    }//GEN-LAST:event_txtPrixVersementKeyReleased

    private void txtDateDebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateDebActionPerformed
        dateChooser1.showPopup();
    }//GEN-LAST:event_txtDateDebActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        VersementEntreprise lastVersementEntreprise = versementEntrepriseDAOImpl.getLastVersementEntreprise(versementEntreprise.getEntreprise());
        double montant = new Nomber().getNbDouble(txtPrixVersement.getText());
        double Credit = new Nomber().getNbDouble(txtcreditVersement.getText());
        double Augment = new Nomber().getNbDouble(labAugVersement.getText()) * -1;
        //  ModePaiement mode_paiement = getModePaiement();
        LocalDate date = LocalDate.parse(txtDateDeb.getText());
        
        
        if (montant <= 0) {
            exite.showMessageDialog("تـنـبـيه", "الـرجـاء إدخـال مـبـلـغ الـدفـعـة ");
            return;
        }

        VersementEntreprise nextVersementEntreprise = versementEntrepriseDAOImpl.getNextVersementEntreprise(versementEntreprise);

        if (previousVersement != null
                && date.isBefore(previousVersement.getDate_versement())) {

            exite.showMessageDialog(
                    "تـنـبـيه",
                    "تاريخ الدفعة غير مقبول\n"
                    + "يجب أن يكون تاريخ الدفعة الحالية بعد تاريخ الدفعة السابقة:\n"
                    + previousVersement.getDate_versement()
            );
            return;
        }

        if (nextVersementEntreprise != null
                && date.isAfter(nextVersementEntreprise.getDate_versement())) {

            exite.showMessageDialog(
                    "تـنـبـيه",
                    "تاريخ الدفعة غير مقبول!\n"
                    + "يجب أن يكون تاريخ الدفعة الحالية قبل تاريخ الدفعة الموالية:\n"
                    + nextVersementEntreprise.getDate_versement()
            );
            return;
        }

        String etatActuel = versementEntrepriseDAOImpl.getEtat(versementEntreprise);

       // VersementEntreprise nextVersementEntreprise = versementEntrepriseDAOImpl.getNextVersementEntreprise(versementEntreprise);

        String etatNext = null;

        if (nextVersementEntreprise != null) {
            etatNext = versementEntrepriseDAOImpl.getEtat(nextVersementEntreprise);
        }

        boolean checked = chekLastVersement.isSelected();

// NON -> OUI
        if ("non".equals(etatActuel)
                && checked
                && nextVersementEntreprise != null) {

            exite.showMessageDialog(
                    "تـنـبـيه",
                    //"لا يمكنك تفعيل : آخر دفعة قديمة للشركة ، لأنه هناك دفعات قديمة بعد هذه الدفعة ."
                    "لا يمكنك تفعيل : آخر دفعة قديمة للشركة ."+" \n لأنه يوجد دفعات قديمة بعد هذه الدفعة"

            );
            return;
        }

// OUI -> NON
        if ("oui".equals(etatActuel)
                && !checked
                && nextVersementEntreprise != null
                && etatNext == null) {

            exite.showMessageDialog(
                    "تـنـبـيه",
                    "لا يمكنك إلغاء تفعيل: آخر دفعة قديمة للشركة "
                            +"\n لأن هذه آخر دفعة قديمة"+
                             "و قد تم تسجبل دفعات عادية بعدها ."
            );
            return;
        }

        //  String numCheque = txtNumCheque.getText();
        if(!versementEntreprise.getRemarque().isEmpty() &&checked){
        versementEntrepriseDAOImpl.setEtat(versementEntreprise, "oui");
        }
        if(!versementEntreprise.getRemarque().isEmpty() && !checked){
        versementEntrepriseDAOImpl.setEtat(versementEntreprise, "non");
        }
        
        versementEntreprise.setMontant(montant);
        versementEntreprise.setReste_credit((Augment == 0) ? Credit : Augment);

        if (versementEntrepriseDAOImpl.getEtat(versementEntreprise) != null) {
            versementEntreprise.setRemarque(date + "");
        }

        messageDialog.showMessagetoDialog("حــفـظ", "هـل أنت متـأكـد مـن عـمـلـيـة الحـفـظ");

        if (messageDialog.getMessageType() != MessageDialog.MessageType.YES) {
            return;
        }

        System.out.println("versementEntreprise" + montant);
        if (versementEntrepriseDAOImpl.update(versementEntreprise) > 0) {

            this.dispose();
            validationMessageDialog.showMessage("تأكيد", "تـم الحـفـظ بـنـجـاح");
            System.out.println("lastVersementEntreprise : " + lastVersementEntreprise.getId()
                    + "versementEntreprise : " + versementEntreprise.getId());
            if (!lastVersementEntreprise.equals(versementEntreprise)) {
                recalculerResteCredit(versementEntreprise);
            }
            // this.homeForm.getPanVersement().setVersmentOnTab();
            this.homeForm.getpanVersementSpecialAchat().setVersementSpecialOnTab();
            this.homeForm.getPanVersement().setVersmentOnTab();
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_annulerActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_annulerActionPerformed

    private void chekLastVersementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chekLastVersementActionPerformed
//        if (chekLastVersement.isSelected()) {
//            messageDialog.ShowConfirmMessageInDialog("تـنـبـيه", "هل أنت متأكد أن هذه أخر دفعة استثنائية لشركة \n لأنه لا يمكنك ادخال دفعة استثنائية بعدها الحفظ");
//            if (messageDialog.getMessageType() == MessageDialog.MessageType.CANCEL) {
//                chekLastVersement.setSelected(false);
//            }
//        }
    }//GEN-LAST:event_chekLastVersementActionPerformed

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
            java.util.logging.Logger.getLogger(ModifyVersementEntreprise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifyVersementEntreprise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifyVersementEntreprise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifyVersementEntreprise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ModifyVersementEntreprise dialog = new ModifyVersementEntreprise(new javax.swing.JFrame(), true, null);
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
    private material.design.buttonRounder btn_annuler;
    private material.design.buttonRounder btn_save;
    private material.design.JCheckBoxCustomAR chekLastVersement;
    private datechooser.DateChooser dateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labAchat;
    private javax.swing.JLabel labAugVersement;
    private javax.swing.JLabel labAugment;
    private javax.swing.JLabel labCredit;
    private javax.swing.JLabel labNomEntreprise;
    private ui.card.panRound2 panRound23;
    private ui.card.panRound2 panRound24;
    private ui.card.pan_gradiant pan_gradiant1;
    private ui.card.pan_gradiant pan_gradiant5;
    private ui.card.pan_gradiant pan_gradiant8;
    private material.design.TextField txtDateDeb;
    private ui.card.TextFieldPrice txtPrixVersement;
    private javax.swing.JLabel txtcreditVersement;
    // End of variables declaration//GEN-END:variables
}
