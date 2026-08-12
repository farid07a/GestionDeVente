package frame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import material.design.SearchTextRound;

public class OrderTableDashboard extends JFrame {

    private JTable productsTable;      // جدول المنتجات المتاحة
    private JTable cartTable;          // جدول سلة المشتريات
    private DefaultTableModel productsModel;
    private DefaultTableModel cartModel;
    
    private JLabel lblTotal;
    private SearchTextRound txtSearch; // حقل البحث الدائري الخاص بك

    public OrderTableDashboard() {
        setTitle("نظام المبيعات - إدارة الجداول");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

        // ==========================================
        // 1. القسم الأيمن: الجداول والبحث
        // ==========================================
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // تقسيم رأسي بالتساوي للجدولين
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // الجزء العلوي: البحث + جدول المخزن
        JPanel topSearchAndTable = new JPanel(new BorderLayout(5, 5));
        topSearchAndTable.setOpaque(false);
        
        txtSearch = new SearchTextRound();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearch.setPreferredSize(new Dimension(0, 40));
        topSearchAndTable.add(txtSearch, BorderLayout.NORTH);

        // إنشاء وتعبئة جدول المنتجات (المخزن)
        String[] prodColumns = {"المعرف", "اسم المنتج", "المخزون الحالي", "السعر (دج)"};
        productsModel = new DefaultTableModel(prodColumns, 0);
        productsTable = new JTable(productsModel);
        productsTable.setRowHeight(25);
        
        // إضافة بيانات تجريبية (هنا تربطها بقاعدة بياناتك لاحقاً)
        productsModel.addRow(new Object[]{"101", "حليب جميدة 1 لتر", "50", 125.0});
        productsModel.addRow(new Object[]{"102", "قهوة بونال 250غ", "20", 350.0});
        productsModel.addRow(new Object[]{"103", "عصير رامي 1 لتر", "15", 160.0});
        productsModel.addRow(new Object[]{"104", "مقرون سفينة 500غ", "100", 95.0});

        // تفعيل ميزة البحث الفوري داخل الجدول بناءً على ما يكتبه المستخدم في حقل البحث
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(productsModel);
        productsTable.setRowSorter(sorter);
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = txtSearch.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    // البحث في عمود اسم المنتج (عامود رقم 1)
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        JScrollPane scrollProducts = new JScrollPane(productsTable);
        scrollProducts.setBorder(BorderFactory.createTitledBorder("📦 المنتجات المتوفرة بالمخزن (انقر مرتين للإضافة)"));
        topSearchAndTable.add(scrollProducts, BorderLayout.CENTER);
        rightPanel.add(topSearchAndTable);

        // الجزء السفلي: جدول سلة المشتريات الحالي
        String[] cartColumns = {"اسم المنتج", "السعر المفرد", "الكمية المطلوبة", "الإجمالي"};
        cartModel = new DefaultTableModel(cartColumns, 0);
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(25);
        JScrollPane scrollCart = new JScrollPane(cartTable);
        scrollCart.setBorder(BorderFactory.createTitledBorder("🛒 سلة المشتريات للطلب الحالي"));
        rightPanel.add(scrollCart);

        // حدث النقر المزدوج لنقل المنتج من جدول المخزن إلى جدول السلة
        productsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // عند الضغط مرتين
                    int row = productsTable.getSelectedRow();
                    if (row != -1) {
                        // تحويل مؤشر السطر ليتناسب مع الفلترة والترتيب
                        int modelRow = productsTable.convertRowIndexToModel(row);
                        String name = (String) productsModel.getValueAt(modelRow, 1);
                        double price = (double) productsModel.getValueAt(modelRow, 3);
                        
                        addProductToCart(name, price);
                    }
                }
            }
        });

        // ==========================================
        // 2. القسم الأيسر: الجانب المالي والدفع
        // ==========================================
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(220, 224, 230)));
        sidePanel.setPreferredSize(new Dimension(300, 0));

        JLabel lblTitle = new JLabel("ملخص الحساب");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidePanel.add(lblTitle);

        lblTotal = new JLabel("المجموع الكلي: 0.00 دج");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(new Color(37, 99, 235));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(20, 10, 40, 10));
        sidePanel.add(lblTotal);

        JButton btnConfirm = new JButton("🟢 تأكيد وحفظ الفاتورة");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnConfirm.setBackground(new Color(34, 197, 94));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setMaximumSize(new Dimension(260, 45));
        btnConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(btnConfirm);

        // إضافة الألواح للـ JFrame
        add(rightPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.WEST);
    }

    // منطق إضافة المنتجات المتعددة وتحديث الكميات في جدول السلة
    private void addProductToCart(String productName, double price) {
        boolean isExist = false;
        int existingRow = -1;

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (cartModel.getValueAt(i, 0).equals(productName)) {
                isExist = true;
                existingRow = i;
                break;
            }
        }

        if (isExist) {
            int currentQty = (int) cartModel.getValueAt(existingRow, 2);
            int newQty = currentQty + 1;
            cartModel.setValueAt(newQty, existingRow, 2);
            cartModel.setValueAt(newQty * price, existingRow, 3);
        } else {
            cartModel.addRow(new Object[]{productName, price, 1, price});
        }

        updateTotal();
        txtSearch.requestFocus(); // إعادة المؤشر لحقل البحث فوراً لتسريع العمل
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total += (double) cartModel.getValueAt(i, 3);
        }
        lblTotal.setText("المجموع الكلي: " + total + " دج");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderTableDashboard().setVisible(true));
    }
}