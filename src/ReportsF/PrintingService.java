/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReportsF;

import ReportsF.ReportNames;
import config.DatabaseConnection;
import dao.impl.ClientDAOImpl;
import entity.Client;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author farid
 */
public class PrintingService {

    private static final String REPORTS_PATH = "/reports/";

    public PrintingService() {

    }

    public void ImprimTousLesClients() {

        try {
            String TestQuery = "SELECT * FROM Client";
            JasperReport jasperreport;
            InputStream file = getClass().getResourceAsStream("/Reports/ListClient.jrxml");
            JasperDesign jasperdesign = JRXmlLoader.load(file);
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(TestQuery);
            jasperdesign.setQuery(newQuery);
            Map parametres = new HashMap<String, Object>();
            jasperreport = JasperCompileManager.compileReport(jasperdesign);
            Connection Cnx1 = DatabaseConnection.getInstance().getConnection();
            JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, parametres, Cnx1);
            // jp=JasperFillManager.fillReport(jr, parametres, cnx.getConnect());
            JasperViewer JspViewr = new JasperViewer(jasperprint, false);
            JspViewr.viewReport(jasperprint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in jasper Report" + e.getMessage());
        }
    }

    public void PrintClientById(int id, Map args) {

        try {
            String TestQuery = " SELECT Client.nom,Client.prenom,Produit.designation,Achat_detaille.prix_unitaire,  Achat_detaille.qty,  Achat_detaille.prix_total,  Achat_detaille.date_achat"
                    + " FROM (( Client INNER JOIN Achat ON Client.id = Achat.id_client)   "
                    + " INNER JOIN   Achat_detaille ON Achat.id = Achat_detaille.id_achat ) "
                    + " INNER JOIN Produit ON  Achat_detaille.id_produit = Produit.id  WHERE Client.id = " + id;

            JasperReport jasperreport;
            InputStream file = getClass().getResourceAsStream("/Reports/ListAchatByClientAndDateAchat.jrxml");
            JasperDesign jasperdesign = JRXmlLoader.load(file);
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(TestQuery);
            jasperdesign.setQuery(newQuery);
            Map parametres = new HashMap<String, Object>();

            parametres.put("FNameClient", args.get("FNameClient"));
            parametres.put("DateAchat", args.get("DateAchat"));
            parametres.put("Enterprise", args.get("Enterprise"));

            jasperreport = JasperCompileManager.compileReport(jasperdesign);
            Connection Cnx1 = DatabaseConnection.getInstance().getConnection();
            JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, parametres, Cnx1);
            // jp=JasperFillManager.fillReport(jr, parametres, cnx.getConnect());
            JasperViewer JspViewr = new JasperViewer(jasperprint, false);
            JspViewr.viewReport(jasperprint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in jasper Report" + e.getMessage());
        }
    }

    public void printReport(String reportName,Component component) {

        try {
            InputStream reportStream
                    = getClass().getResourceAsStream(REPORTS_PATH + reportName);

            Connection connection = DatabaseConnection.getInstance().getConnection();

            JasperPrint print = JasperFillManager.fillReport(
                    reportStream,
                    null,
                    connection
            );

         //   JasperViewer.viewReport(print, false);
            JasperViewer viewer = new JasperViewer(print, false);

        viewer.setAlwaysOnTop(true);
        viewer.setLocationRelativeTo(null);
        viewer.setVisible(true);

        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void printReport(String reportName,
            Map<String, Object> parameters) {

        try {
            InputStream reportStream
                    = getClass().getResourceAsStream(REPORTS_PATH + reportName);

            Connection connection = DatabaseConnection.getInstance().getConnection();

            JasperPrint print = JasperFillManager.fillReport(
                    reportStream,
                    parameters,
                    connection
            );

            JasperViewer.viewReport(print, false);

        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    
    
//    public void printReport(String reportName, Map<String, Object> parameters) {
//
//    try {
//        // Load the .jrxml file
//        InputStream reportStream = getClass().getResourceAsStream(REPORTS_PATH + reportName + ".jrxml");
//
//        // Compile the report
//        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
//
//        // Get database connection
//        Connection connection = DatabaseConnection.getInstance().getConnection();
//
//        // Fill the report
//        JasperPrint print = JasperFillManager.fillReport(
//                jasperReport,
//                parameters,
//                connection
//        );
//
//        // Display the report
//        JasperViewer.viewReport(print, false);
//
//    } catch (JRException ex) {
//        ex.printStackTrace();
//       // JOptionPane.showMessageDialog(null, ex.getMessage());
//    }
//}
public void printReport(String reportName, Map<String, Object> parameters,java.awt.Window parent) {

    try {
        String reportPath = REPORTS_PATH + reportName;

        System.out.println("Report path = " + reportPath);

        InputStream reportStream =
                getClass().getResourceAsStream(reportPath);

        System.out.println("Report stream = " + reportStream);

        if (reportStream == null) {
            throw new RuntimeException(
                    "Report not found: " + reportPath
            );
        }

        Connection connection =
                DatabaseConnection.getInstance().getConnection();

        JasperPrint print = JasperFillManager.fillReport(
                reportStream,
                parameters,
                connection
        );
JasperViewer viewer = new JasperViewer(print, false);

viewer.setLocationRelativeTo(parent);
viewer.setVisible(true);

    } catch (JRException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
                null,
                "Error in JasperReport:\n" + ex.getMessage()
        );
    }
}

public void printReportDialog(String reportName, Map<String, Object> parameters, java.awt.Window parent) {

    try {
        String reportPath = REPORTS_PATH + reportName;

        System.out.println("Report path = " + reportPath);

        InputStream reportStream = getClass().getResourceAsStream(reportPath);

        if (reportStream == null) {
            throw new RuntimeException("Report not found: " + reportPath);
        }

        Connection connection = DatabaseConnection.getInstance().getConnection();

        JasperPrint print = JasperFillManager.fillReport(
                reportStream,
                parameters,
                connection
        );

        // 1. إنشاء JasperViewer بدون إظهاره مباشرة
        JasperViewer viewer = new JasperViewer(print, false);

        // 2. إنشاء JDialog جديد وربطه بالـ Dialog الحالي (parent)
        // إعطاء modal = false ليتسنى للزبون التنقل بين التقرير والـ Dialog
        JDialog reportDialog = new JDialog(parent, "معاينة الفاتورة", Dialog.ModalityType.MODELESS);
        
        // 3. وضع محتوى الـ Viewer داخل الـ Dialog
        reportDialog.setContentPane(viewer.getContentPane());
        reportDialog.setSize(parent.getOwner() != null ? parent.getOwner().getSize() : new Dimension(1024, 768));
        
        // 4. تحديد الموضع فوق الـ Dialog الحالي
        reportDialog.setLocationRelativeTo(parent);
        
        // 5. إظهار النافذة
        reportDialog.setVisible(true);

    } catch (JRException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
                null,
                "Error in JasperReport:\n" + ex.getMessage()
        );
    }
}

public static void main(String[] args) {
        //new PrintingService().ImprimTousLesClients();
        PrintingService service_print = new PrintingService();
        Map<String, Object> params = new HashMap<>();
//        int clientId = 4;
//        ClientDAOImpl clientDao = new ClientDAOImpl(null);
//        Client client  = clientDao.findById(clientId);
//        
//        //params.put("CLIENT_ID", client.getId());
        params.put("CLIENT_ID", 4);
        params.put("FName", "Farid KHEBBACHE");
        params.put("ENTERPRISE_NAME_FR","CILAS");

        service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_ID, params,null);
//        LocalDate specificDate = LocalDate.of(2026, 8,12);
//        
//        
//        params.put("Date_Achat", java.sql.Date.valueOf(specificDate));
//        params.put("ENTERPRISE_AFF",client.getEntreprise().getNom_ar());
//        
        //service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_DATE, params);
        //service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISES);
        // Map<String, Object> params = new HashMap<>();
        params = new HashMap<>();
        params.put("ENTERPRISE_NAME_FR", "CILAS");
        service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_NAME_FR,params);

        /**
         * ***************************************************************************************
         */
        
        // pour test
        params.put("CLIENT_ID", 4);
        params.put("ENTERPRISE_AFF", "CILAS");
        params.put("YEAR", 2026);
        params.put("FName", "Farid KHEBBACHE");
        service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_YEAR, params,null);

        
        /**
         * *********************************************************************
         */
        // pour test
        params = new HashMap<>();
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
//        params.put("YEAR", 2026);
//        params.put("FName", "Farid KHEBBACHE");
     //   service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID, params);
        
        
        /**********************************************************************/
         params = new HashMap<>();
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
//        params.put("YEAR", 2026);
//        params.put("FName", "Farid KHEBBACHE");
     //   service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID, params);
        
        params = new HashMap<>();
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
//        params.put("YEAR", 2026);
//        params.put("FName", "Farid KHEBBACHE");
        service_print.printReport(ReportNames.LIST8CLIENT_BY_ID8ENTERPRISE, params,null);
        
        
        
        
    }

}
