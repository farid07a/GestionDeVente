package ui.menufr;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class customMenu extends javax.swing.JPanel {

    private EventMenuSelected event;

    int lang;

    public void addEventMenuSelected(EventMenuSelected event) {
        this.event = event;
        listMenu121.addEventMenuSelected(event);
    }

    public customMenu() {
        initComponents();
        setOpaque(false);
        listMenu121.setOpaque(false);
        init();
    }

    private void init() {
        //System.out.println("com.raven.component.Menu.init()");
        listMenu121.addItem(new Model_Menu("icons8-vitesse-64 (1)", " الرئيسية", Model_Menu.MenuType.MENU));
        // listMenu121.addItem(new Model_Menu("","",Model_Menu.MenuType.EMPTY));
        listMenu121.addItem(new Model_Menu("", "", Model_Menu.MenuType.EMPTY));

        listMenu121.addItem(new Model_Menu("icons8-organisation-64 (2)", "الشركات", Model_Menu.MenuType.MENU));
        listMenu121.addItem(new Model_Menu("icons8-conférence-64 (1)", "الزبائن", Model_Menu.MenuType.MENU));
        listMenu121.addItem(new Model_Menu("boite", " المنتجات", Model_Menu.MenuType.MENU));

        listMenu121.addItem(new Model_Menu("", "", Model_Menu.MenuType.EMPTY));

        listMenu121.addItem(new Model_Menu("icons8-vendre-les-stock-64", "المبيعات ", Model_Menu.MenuType.MENU));
        listMenu121.addItem(new Model_Menu("icons8-sac-d'argent-64 (2)", "المدفوعات ", Model_Menu.MenuType.MENU));
        //  listMenu121.addItem(new Model_Menu("approvisionnement","الفواتير ",Model_Menu.MenuType.MENU));
        listMenu121.addItem(new Model_Menu("", "", Model_Menu.MenuType.EMPTY));

        //listMenu121.addItem(new Model_Menu("","",Model_Menu.MenuType.EMPTY));
//listMenu121.addItem(new Model_Menu("audit","التقارير",Model_Menu.MenuType.MENU));
        //listMenu121.addItem(new Model_Menu("","",Model_Menu.MenuType.EMPTY));
//listMenu121.addItem(new Model_Menu("icons8-compte-test-64 (1)","المستخدمون",Model_Menu.MenuType.MENU));
        listMenu121.addItem(new Model_Menu("paramètres", "الإعدادات", Model_Menu.MenuType.MENU));

//listMenu121.addItem(new Model_Menu("","",Model_Menu.MenuType.EMPTY));
        listMenu121.addItem(new Model_Menu("fermer", " تسجيل الخروج", Model_Menu.MenuType.MENU));
//  
//     listMenu121.addItem(new Model_Menu("","",Model_Menu.MenuType.TITLE));
//     //listMenu1.addItem(new Model_Menu("","empty",Model_Menu.MenuType.EMPTY));
//     listMenu121.addItem(new Model_Menu("7","",Model_Menu.MenuType.MENU));
//     listMenu121.addItem(new Model_Menu("8","",Model_Menu.MenuType.MENU));

    }

// public TitleBar getTitelBar(){
//        return titleBar1;
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGradient1 = new ui.menufr.panelGradient();
        listMenu121 = new ui.menufr.ListMenu12<>();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(200, 680));
        setLayout(new java.awt.CardLayout());

        panelGradient1.setPreferredSize(new java.awt.Dimension(150, 680));

        listMenu121.setFont(new java.awt.Font("Cairo", 1, 10)); // NOI18N
        listMenu121.setPreferredSize(new java.awt.Dimension(212, 680));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/logoGestionV2_1.png"))); // NOI18N

        javax.swing.GroupLayout panelGradient1Layout = new javax.swing.GroupLayout(panelGradient1);
        panelGradient1.setLayout(panelGradient1Layout);
        panelGradient1Layout.setHorizontalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGradient1Layout.createSequentialGroup()
                        .addComponent(listMenu121, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 45, Short.MAX_VALUE))
                    .addGroup(panelGradient1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelGradient1Layout.setVerticalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listMenu121, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        add(panelGradient1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // GradientPaint g =new GradientPaint(0, 0, Color.decode("#1CB5E0"),0,getHeight(),Color.decode("#000046"));
        ///  GradientPaint g = new GradientPaint(0, 0, Color.decode("#000000"),0,getHeight(),Color.decode("#f1c96a"));//#f1c96a
        //  g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    private int x, y;

    public void initMoving(JFrame frame) {
        panelGradient1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        panelGradient1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
            }
        });

    }

//    public void setLogoIcon(Icon icon) {
//        Image scaledImage = scaleImage(((ImageIcon) icon).getImage(), 50, 50); // Set desired width and height
//        buttonRounder1.setIcon(new ImageIcon(scaledImage));
//        // jLabel1.setIcon(new ImageIcon(scaledImage));
//
//    }

    private static Image scaleImage(Image originalImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return scaledImage;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private ui.menufr.ListMenu12<String> listMenu121;
    private ui.menufr.panelGradient panelGradient1;
    // End of variables declaration//GEN-END:variables
}
