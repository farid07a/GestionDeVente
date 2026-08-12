/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import DialogFram.Exite;
import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import ReportsF.PrintingService;
import ReportsF.ReportNames;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.AchatDetailDAOImpl;
import dao.impl.ClientDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import entity.Achat;
import entity.AchatDetail;
import entity.Client;
import entity.Entreprise;
import frame.AchatDetaillForm;
import frame.Nouvelle_Achat;
import home.HomeForm;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import material.design.designeTable;

/**
 *
 * @author pc
 */
public class panAchat extends javax.swing.JPanel {

    AchatDAOImpl achatDAOImpl;
    Connection connection;
DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));   
HomeForm homeForm;
    MessageDialog messageDialog;
    ValidationMessageDialog validationMessageDialog;
    Exite exite;
 PrintingService service_print = new PrintingService();
 Map<String, Object> params = new HashMap<>();
public panAchat(HomeForm homeForm) {
        initComponents();
        
        connection= DatabaseConnection.getInstance().getConnection();
        achatDAOImpl = new AchatDAOImpl(connection);
        this.homeForm=homeForm ;
        
        
        validationMessageDialog = new ValidationMessageDialog(homeForm);
        messageDialog = new MessageDialog(homeForm);
        exite = new Exite(homeForm);
        
        new designeTable().setDesignTable(tab, jScrollPane2);
        new designeTable().SearchTable(tab, txt_search);
         TableColumn column = tab.getColumnModel().getColumn(0);
        tab.getColumnModel().removeColumn(column);

        setInfoAchatInTab();
        SetInfoInCard();
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.addTableModelListener(e -> {
               SetInfoInCard();
        });
        
       print();
        
    }


    public void print() {
        PrintingService service_print = new PrintingService();
        final Map<String, Object> params = new HashMap<>();
        btnImp.addPopupItem("الـفـاتـورة", e -> {
          
        });
        btnImp.addPopupItem("مـشـتـريـات الزبـون", e -> {

            if (tab.getSelectedRow() != -1) {
                final Map<String, Object> params2 = new HashMap<>();
                int row = tab.getSelectedRow();
                int id = (int) tab.getModel().getValueAt(row, 0);
                Achat achat = achatDAOImpl.findById(id);
                Client client = achat.getClient();
       params.put("CLIENT_ID", client.getId());
        params.put("FName", client.getNom() + " " + client.getPrenom());
        if (!client.getEntreprise().getNom_fr().isEmpty()) {
            params.put("ENTERPRISE_NAME_FR", client.getEntreprise().getNom_fr());
        } else {
            params.put("ENTERPRISE_NAME_FR", client.getEntreprise().getNom_ar());
        }

        service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_ID, params);
            }
        });

    }


      public void SetInfoInCard() {
        int rowsCount = tab.getRowCount();
        if(rowsCount > 0 ){
        double prixTotal = 0;
        for (int row = 0; row < rowsCount; row++) {
            
      int modelRow = tab.convertRowIndexToModel(row);
            Object val =tab.getModel().getValueAt(modelRow, 2).toString();
          String cleanValue = val.toString()
                           .replace(",", "")          
                           .replace(" ", "")         
                           .replace("\u00A0", "")    
                           .replaceAll("\\s+", "")    
                           .trim();
            double prix = Double.parseDouble(cleanValue);
            prixTotal = prixTotal + prix;
        }
        labPrixTotal.setText(formatter.format(prixTotal));
        labNbAchat.setText(rowsCount+"");
        lab_nbTable.setText(rowsCount + "");
        
        List<Entreprise> entreprises = new EntrepriseDAOImpl(connection).findAll();
        List<Client> clients = new ClientDAOImpl(connection).findAll();
        labNbEntreprice.setText(entreprises.size()+"");
        lanNbClient.setText(clients.size()+"");
        
        }
    }
      
    public void setInfoAchatInTab(){
         DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
         List <Achat> achats = achatDAOImpl.findAll();
         String nom_entreprise ="";
        for (Achat achat: achats) {
            int id = achat.getId();
            String matricul= achat.getClient().getMatricule();
            String nom = achat.getClient().getNom();
            String prenom = achat.getClient().getPrenom();
            
            double prixTotal = achat.getPrix_total();
            
            LocalDate date= achat.getDate_achat();
            Entreprise entreprice = achat.getClient().getEntreprise();
          
            nom_entreprise =" "; 
          
            if(entreprice!= null   ){
                    nom_entreprise = entreprice.getNom_ar();
            }
            model.addRow(new Object[]{id, date,formatter.format(prixTotal), 
                nom_entreprise, prenom
              + " "+ nom,matricul} ) ;
        }
        lab_nbTable.setText(achats.size()+"");
     
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panTop = new javax.swing.JPanel();
        panRound3 = new ui.card.panRound();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labNbEntreprice = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lanNbClient = new javax.swing.JLabel();
        panRound5 = new ui.card.panRound();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        labPrixTotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        panRound6 = new ui.card.panRound();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labNbAchat = new javax.swing.JLabel();
        panCenter = new javax.swing.JPanel();
        panRound4 = new ui.card.panRound();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        txt_search = new material.design.SearchTextRound();
        panRound1 = new ui.card.panRound();
        jLabel1 = new javax.swing.JLabel();
        lab_nbTable = new javax.swing.JLabel();
        button1 = new material.design.button();
        btnNewAchat = new material.design.buttonRounder();
        btnSupprim = new material.design.buttonRounder();
        btnImp = new material.design.buttonMenu();
        panButtom = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        panTop.setBackground(new java.awt.Color(255, 255, 255));
        panTop.setMaximumSize(new java.awt.Dimension(32767, 100));
        panTop.setMinimumSize(new java.awt.Dimension(100, 100));
        panTop.setPreferredSize(new java.awt.Dimension(100, 100));

        panRound3.setColor1(new java.awt.Color(255, 255, 255));
        panRound3.setColor2(new java.awt.Color(254, 152, 254));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/compagnies.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 204, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("الـشـركــات");

        labNbEntreprice.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labNbEntreprice.setForeground(new java.awt.Color(0, 0, 153));
        labNbEntreprice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labNbEntreprice.setText("00");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-groupe-d&#39;utilisateurs-50.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 0, 153));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("الـزبـائـن");

        lanNbClient.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lanNbClient.setForeground(new java.awt.Color(153, 0, 153));
        lanNbClient.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lanNbClient.setText("00");

        javax.swing.GroupLayout panRound3Layout = new javax.swing.GroupLayout(panRound3);
        panRound3.setLayout(panRound3Layout);
        panRound3Layout.setHorizontalGroup(
            panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound3Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lanNbClient, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(labNbEntreprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        panRound3Layout.setVerticalGroup(
            panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound3Layout.createSequentialGroup()
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labNbEntreprice, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lanNbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panRound5.setColor1(new java.awt.Color(255, 255, 255));
        panRound5.setColor2(new java.awt.Color(221, 221, 251));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/facture.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 153, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("اجمالي الفواتير");

        labPrixTotal.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labPrixTotal.setForeground(new java.awt.Color(0, 153, 51));
        labPrixTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labPrixTotal.setText("00");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setText("دج");

        javax.swing.GroupLayout panRound5Layout = new javax.swing.GroupLayout(panRound5);
        panRound5.setLayout(panRound5Layout);
        panRound5Layout.setHorizontalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound5Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound5Layout.createSequentialGroup()
                        .addComponent(labPrixTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(107, 107, 107)))
                .addGap(17, 17, 17))
        );
        panRound5Layout.setVerticalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labPrixTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(19, 19, 19))
        );

        panRound6.setColor1(new java.awt.Color(255, 255, 255));
        panRound6.setColor2(new java.awt.Color(249, 236, 176));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-60.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 51, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("طـلـبـيـات الشـراء");

        labNbAchat.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labNbAchat.setForeground(new java.awt.Color(255, 51, 0));
        labNbAchat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labNbAchat.setText("00");

        javax.swing.GroupLayout panRound6Layout = new javax.swing.GroupLayout(panRound6);
        panRound6.setLayout(panRound6Layout);
        panRound6Layout.setHorizontalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound6Layout.createSequentialGroup()
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addContainerGap(34, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(labNbAchat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        panRound6Layout.setVerticalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labNbAchat, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout panTopLayout = new javax.swing.GroupLayout(panTop);
        panTop.setLayout(panTopLayout);
        panTopLayout.setHorizontalGroup(
            panTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTopLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(panRound5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14)
                .addComponent(panRound6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(panRound3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(77, 77, 77))
        );
        panTopLayout.setVerticalGroup(
            panTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTopLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panRound6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panRound5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panRound3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(panTop);

        panCenter.setBackground(new java.awt.Color(255, 255, 255));

        panRound4.setColor1(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        tab.setFont(new java.awt.Font("Cairo", 1, 15)); // NOI18N
        tab.setForeground(new java.awt.Color(102, 102, 102));
        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "التاريخ", "المـبـلغ", "الشركة ", "الاسم و اللقب", "رقـم الـتـعـريـف"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tab);

        tableScrollButton1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panRound4Layout = new javax.swing.GroupLayout(panRound4);
        panRound4.setLayout(panRound4Layout);
        panRound4Layout.setHorizontalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(319, 319, 319))
        );
        panRound4Layout.setVerticalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(txt_search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        panRound1.setBackground(new java.awt.Color(255, 255, 255));
        panRound1.setColor1(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 204, 0));
        jLabel1.setText("العدد : ");

        lab_nbTable.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lab_nbTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lab_nbTable.setText("00");

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lab_nbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(127, 127, 127))
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_nbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-petits-caractères-48.png"))); // NOI18N
        button1.setText("مـعـايـنـة عــمـلـية الـبـيـع");
        button1.setColor1(new java.awt.Color(51, 153, 255));
        button1.setColor2(new java.awt.Color(51, 153, 255));
        button1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        button1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        button1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        btnNewAchat.setBackground(new java.awt.Color(0, 153, 0));
        btnNewAchat.setForeground(new java.awt.Color(255, 255, 255));
        btnNewAchat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-30.png"))); // NOI18N
        btnNewAchat.setText("عـمـلـيـة بـيـع جــديـدة");
        btnNewAchat.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnNewAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAchatActionPerformed(evt);
            }
        });

        btnSupprim.setBackground(new java.awt.Color(220, 0, 0));
        btnSupprim.setForeground(new java.awt.Color(255, 255, 255));
        btnSupprim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-trash-64.png"))); // NOI18N
        btnSupprim.setText("حذف ");
        btnSupprim.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSupprim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimActionPerformed(evt);
            }
        });

        btnImp.setBackground(new java.awt.Color(51, 204, 255));
        btnImp.setForeground(new java.awt.Color(255, 255, 255));
        btnImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-print-48.png"))); // NOI18N
        btnImp.setText("طــباعـة التـقـاريــر");
        btnImp.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        javax.swing.GroupLayout panCenterLayout = new javax.swing.GroupLayout(panCenter);
        panCenter.setLayout(panCenterLayout);
        panCenterLayout.setHorizontalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panCenterLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                .addComponent(btnImp, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNewAchat, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
            .addGroup(panCenterLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panCenterLayout.createSequentialGroup()
                        .addComponent(panRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panCenterLayout.createSequentialGroup()
                        .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20))))
        );
        panCenterLayout.setVerticalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panCenterLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btnNewAchat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(panRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(panCenter);

        panButtom.setBackground(new java.awt.Color(255, 255, 255));
        panButtom.setMaximumSize(new java.awt.Dimension(32767, 50));
        panButtom.setMinimumSize(new java.awt.Dimension(100, 50));
        panButtom.setPreferredSize(new java.awt.Dimension(100, 50));

        javax.swing.GroupLayout panButtomLayout = new javax.swing.GroupLayout(panButtom);
        panButtom.setLayout(panButtomLayout);
        panButtomLayout.setHorizontalGroup(
            panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 916, Short.MAX_VALUE)
        );
        panButtomLayout.setVerticalGroup(
            panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        add(panButtom);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        if (tab.getSelectedRow() != -1){
            int row = tab.getSelectedRow();
            int modelRow = tab.convertRowIndexToModel(row);
            
            int idAchat = (int) tab.getModel().getValueAt(modelRow, 0);
            Achat achat = new AchatDAOImpl(connection).findById(idAchat);
            new AchatDetaillForm(this.homeForm, true, achat).setVisible(true);
        }
    }//GEN-LAST:event_button1ActionPerformed

    private void btnNewAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAchatActionPerformed
        new Nouvelle_Achat(this.homeForm, true).setVisible(true);
    }//GEN-LAST:event_btnNewAchatActionPerformed

    private void btnSupprimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimActionPerformed
        if (tab.getSelectedRow() != -1) {
            int row = tab.getSelectedRow();
            int id = (int) tab.getModel().getValueAt(row, 0);
            Achat achat = achatDAOImpl.findById(id);
            messageDialog.ShowConfirmMessageInFrame("تـأكـيد الـحـذف", "هـل أنت متـأكـد مـن حـذف عملـيـة الـبـيـع");
            if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
                if (achatDAOImpl.delete(id) >0){   
                validationMessageDialog.showMessage("حـذف", "تم حذف عـمـلـيـة الـبـيـع بنجاح");
                setInfoAchatInTab();
                }
                else {
                    exite.showMessage("خــطـأ", "لا يمكنك حـذف عـمـلـيـةالـبـيـع  ");
                }
            }
        }
    }//GEN-LAST:event_btnSupprimActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private material.design.buttonMenu btnImp;
    private material.design.buttonRounder btnNewAchat;
    private material.design.buttonRounder btnSupprim;
    private material.design.button button1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labNbAchat;
    private javax.swing.JLabel labNbEntreprice;
    private javax.swing.JLabel labPrixTotal;
    private javax.swing.JLabel lab_nbTable;
    private javax.swing.JLabel lanNbClient;
    private javax.swing.JPanel panButtom;
    private javax.swing.JPanel panCenter;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound3;
    private ui.card.panRound panRound4;
    private ui.card.panRound panRound5;
    private ui.card.panRound panRound6;
    private javax.swing.JPanel panTop;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
