package frame;

import DialogFram.ValidationMessageDialog;
import com.sun.javafx.application.PlatformImpl;
import config.DatabaseConnection;
import dao.impl.EntrepriseConfigDAOImp;
import entity.EntrepriseConfig;
import home.HomeForm;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.List;

import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class EntrepriseForm extends javax.swing.JDialog {

    EntrepriseConfigDAOImp entrepriseConfigDAOImp;
    Connection connection;
    ValidationMessageDialog validationMessageDialog;
    HomeForm homeForm;

    EntrepriseConfig entrepriseConfig = null;
    File imagefile = null;

    public EntrepriseForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        initComponents();
        setLocationRelativeTo(this.homeForm);

        connection = DatabaseConnection.getInstance().getConnection();
        entrepriseConfigDAOImp = new EntrepriseConfigDAOImp(connection);
        validationMessageDialog = new ValidationMessageDialog(this, homeForm);

        List<EntrepriseConfig> entrepriseConfigs = entrepriseConfigDAOImp.findAll();

        if (!entrepriseConfigs.isEmpty()) {
            entrepriseConfig = entrepriseConfigs.get(0);
        }
        if (entrepriseConfig != null) {
            txt_num.setText(entrepriseConfig.getNum() != null ? entrepriseConfig.getNum() : "");
            txt_nom.setText(entrepriseConfig.getNom_ar() != null ? entrepriseConfig.getNom_ar() : "");
            txt_nom_fr.setText(entrepriseConfig.getNom_fr() != null ? entrepriseConfig.getNom_fr() : "");
            txt_adress.setText(entrepriseConfig.getAdresse() != null ? entrepriseConfig.getAdresse() : "");
            txt_gmail.setText(entrepriseConfig.getEmail() != null ? entrepriseConfig.getEmail() : "");
            txt_tel.setText(entrepriseConfig.getTel() != null ? entrepriseConfig.getTel() : "");

            oldImagePath = entrepriseConfig.getImage();
            showImageFromPath(oldImagePath);
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        txt_num.setText("");
        txt_nom.setText("");
        txt_nom_fr.setText("");
        txt_adress.setText("");
        txt_gmail.setText("");
        txt_tel.setText("");
        labLogo.setIcon(null);
        imagefile = null;
    }

    private void selectImage() {
        PlatformImpl.startup(() -> {
            FileChooser fileChooser = new FileChooser();
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {

                imagePath = file.getAbsolutePath();
                showImageFromPath(imagePath);
            }
        });
    }
//private void selectImage() {
//   
//
//    javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
//    fileChooser.setDialogTitle("اختيار شعار المؤسسة");
//    
//    javax.swing.filechooser.FileNameExtensionFilter filter =
//            new javax.swing.filechooser.FileNameExtensionFilter("Images (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png");
//    fileChooser.setFileFilter(filter);
//
//    int result = fileChooser.showOpenDialog(this);
//
//    if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
//        File file = fileChooser.getSelectedFile();
//        imagePath = file.getAbsolutePath(); 
//        showImageFromPath(imagePath);       
//    }
//}

    private void showImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            labLogo.setIcon(null);
            return;
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

            if (bufferedImage == null) {
                labLogo.setIcon(null);
                return;
            }
            int width = labLogo.getWidth() > 0 ? labLogo.getWidth() : 130;
            int height = labLogo.getHeight() > 0 ? labLogo.getHeight() : 130;

            Image scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            labLogo.setIcon(new ImageIcon(scaledImage));
            labLogo.setText("");
            labLogo.repaint();
            labLogo.revalidate();

        } catch (IOException ex) {
            ex.printStackTrace();
            labLogo.setIcon(null);
        }
    }
    String imagePath = null;
    String oldImagePath = null;

    private void showImageFromPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            labLogo.setIcon(null);
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            labLogo.setIcon(null);
            return;
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                labLogo.setIcon(null);
                return;
            }

            int width = labLogo.getWidth() > 10 ? labLogo.getWidth() : 202;
            int height = labLogo.getHeight() > 10 ? labLogo.getHeight() : 129;

            Image scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            labLogo.setIcon(new ImageIcon(scaledImage));
            labLogo.setText("");
            labLogo.repaint();
            labLogo.revalidate();

        } catch (IOException ex) {
            ex.printStackTrace();
            labLogo.setIcon(null);
        }
    }

    // Matiere matiere = matiereDAOImpl.getMatiereNiveauOfCategory(matiere_comb.getSelectedItem().toString(),
    //                ClassInNiveau.getSelectedItem().toString(), NiveauCatg_Etude.getSelectedItem().toString());
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser = new datechooser.DateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnSave = new material.design.buttonRounder();
        btnSave1 = new material.design.buttonRounder();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_nom = new ui.card.TextFieldRound();
        txt_num = new ui.card.TextFieldRound();
        txt_nom_fr = new ui.card.TextFieldRound();
        txt_adress = new ui.card.TextFieldRound();
        txt_gmail = new ui.card.TextFieldRound();
        txt_tel = new ui.card.TextFieldRound();
        labLogo = new javax.swing.JLabel();

        dateChooser.setName(""); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(43, 43, 153));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("معلومات المؤسسة ");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("اسم المؤسسة");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("اسم الؤسسة بالفرنسية");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setText("N :");

        btnSave.setBackground(new java.awt.Color(22, 163, 74));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-coche-emoji-48.png"))); // NOI18N
        btnSave.setText("حفظ");
        btnSave.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnSave1.setBackground(new java.awt.Color(0, 51, 153));
        btnSave1.setForeground(new java.awt.Color(255, 255, 255));
        btnSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-télécharger-64.png"))); // NOI18N
        btnSave1.setText("شعار المؤسسة");
        btnSave1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("العنوان");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("البريد الالكتروني");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("الهاتف");

        txt_nom.setBorder(null);
        txt_nom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nom.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_nom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nomActionPerformed(evt);
            }
        });
        txt_nom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nomKeyPressed(evt);
            }
        });

        txt_num.setBorder(null);
        txt_num.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_num.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_num.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_numActionPerformed(evt);
            }
        });
        txt_num.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_numKeyPressed(evt);
            }
        });

        txt_nom_fr.setBorder(null);
        txt_nom_fr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nom_fr.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_nom_fr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nom_frActionPerformed(evt);
            }
        });
        txt_nom_fr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nom_frKeyPressed(evt);
            }
        });

        txt_adress.setBorder(null);
        txt_adress.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_adress.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_adress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_adressActionPerformed(evt);
            }
        });
        txt_adress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_adressKeyPressed(evt);
            }
        });

        txt_gmail.setBorder(null);
        txt_gmail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_gmail.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_gmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_gmailActionPerformed(evt);
            }
        });
        txt_gmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_gmailKeyPressed(evt);
            }
        });

        txt_tel.setBorder(null);
        txt_tel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_tel.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_tel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telActionPerformed(evt);
            }
        });
        txt_tel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_telKeyPressed(evt);
            }
        });

        labLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labLogo.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(txt_num, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(130, 130, 130))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(351, 351, 351)
                                .addComponent(jLabel2)
                                .addGap(30, 30, 30))))
                    .addComponent(txt_adress, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(250, 250, 250)
                                .addComponent(jLabel3))
                            .addComponent(txt_nom_fr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                            .addComponent(txt_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_gmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(166, 166, 166))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4))
                            .addComponent(txt_num, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labLogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(3, 3, 3)
                        .addComponent(txt_nom_fr, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_adress, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_gmail, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
public byte[] getImageLogo() throws IOException {
        if (imagefile == null) {
            return null;
        }
        return Files.readAllBytes(imagefile.toPath());
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String finalPath = (imagePath != null) ? imagePath : oldImagePath;

        EntrepriseConfig entrepriseConfigNew = new EntrepriseConfig(
                0,
                txt_nom.getText().trim(),
                txt_nom_fr.getText().trim(),
                "",
                txt_adress.getText().trim(),
                txt_tel.getText().trim(),
                txt_gmail.getText().trim(),
                txt_num.getText().trim(),
                finalPath
        );

        if (entrepriseConfig == null) {
            int result = entrepriseConfigDAOImp.save(entrepriseConfigNew);
            if (result > 0) {
                this.dispose();
                validationMessageDialog.showMessagetoDialog("تأكيد", "تم حفظ معلومات المؤسسة بنجاح");
            }
        } else {
            entrepriseConfigNew.setId(entrepriseConfig.getId());
            int result = entrepriseConfigDAOImp.update(entrepriseConfigNew);
            if (result > 0) {
                this.dispose();
                validationMessageDialog.showMessagetoDialog("تأكيد", "تم تحديث معلومات المؤسسة بنجاح");
            }
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed

        selectImage();

//        PlatformImpl.startup(() -> {
//            FileChooser fileChooser = new FileChooser();
//            configureFileChooser(fileChooser);
//            File file = fileChooser.showOpenDialog(null);
//            if (file != null) {
//                BufferedImage img = null;
//                try {
//                    img = ImageIO.read(new File(file.getPath()));
//                    imagefile = new File(file.getPath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Image dimg = img.getScaledInstance(labLogo.getWidth() - 2, labLogo.getHeight() - 2,
//                        Image.SCALE_SMOOTH);
//                ImageIcon imageIcon;
//                imageIcon = new ImageIcon(dimg);
//
//                //  jLabel2.setIcon(icon);
//                labLogo.setIcon(imageIcon);
//
//                //lab_image.setText(name_food.getText());
//                // lab_image.setHorizontalTextPosition(JLabel.CENTER);
//            } else {
//
//            }
//        });

    }//GEN-LAST:event_btnSave1ActionPerformed

    private void txt_nomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nomActionPerformed

    private void txt_nomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nomKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_nom_fr.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_nomKeyPressed

    private void txt_numActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_numActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_numActionPerformed

    private void txt_numKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_numKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_nom.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_numKeyPressed

    private void txt_nom_frActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nom_frActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nom_frActionPerformed

    private void txt_nom_frKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nom_frKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_adress.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_nom_frKeyPressed

    private void txt_adressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_adressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_adressActionPerformed

    private void txt_adressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_adressKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_gmail.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_adressKeyPressed

    private void txt_gmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_gmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_gmailActionPerformed

    private void txt_gmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_gmailKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            txt_tel.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_gmailKeyPressed

    private void txt_telActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telActionPerformed

    private void txt_telKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telKeyPressed


    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JFIF", "*.jfif")
        );
    }

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
            java.util.logging.Logger.getLogger(EntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EntrepriseForm dialog = new EntrepriseForm(null, true);
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
    private material.design.buttonRounder btnSave;
    private material.design.buttonRounder btnSave1;
    private datechooser.DateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labLogo;
    private ui.card.TextFieldRound txt_adress;
    private ui.card.TextFieldRound txt_gmail;
    private ui.card.TextFieldRound txt_nom;
    private ui.card.TextFieldRound txt_nom_fr;
    private ui.card.TextFieldRound txt_num;
    private ui.card.TextFieldRound txt_tel;
    // End of variables declaration//GEN-END:variables
}
