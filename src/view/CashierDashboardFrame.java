package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import module.*; 

public class CashierDashboardFrame extends JFrame {
    private DatabaseManager db;
    private Cashier cashier;
    private Transaction currentTransaction;
    
    // Komponen UI
    private JTable productTable;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    
    // --- KOMPONEN BARU UNTUK RINCIAN HARGA ---
    private JLabel lblSubtotal, lblTax, lblGrandTotal;
    private JTextField txtDiscount;
    private JButton btnProcessDiscount;

    public CashierDashboardFrame(DatabaseManager db, Cashier cashier) {
        this.db = db;
        this.cashier = cashier;
        
        initNewTransaction();

        // 1. Setup Dasar
        setTitle("Kasir: " + cashier.getNama() + " | MiraiMono Hobby Shop");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. HEADER
        JLabel lblHeader = new JLabel("MiraiMono Point of Sales", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Verdana", Font.BOLD, 22));
        lblHeader.setForeground(new Color(50, 50, 150));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblHeader, BorderLayout.NORTH);

        // 3. PANEL KIRI (Etalase Produk)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder(" Daftar Produk "));
        ((javax.swing.border.TitledBorder)leftPanel.getBorder()).setTitleFont(new Font("Verdana", Font.BOLD, 14));
        
        productTable = new JTable();
        productTable.setRowHeight(25);
        productTable.getTableHeader().setBackground(new Color(230, 230, 250));
        refreshProductTable(); 
        leftPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        
        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAddToCart = createStyledButton("Tambah ke Keranjang (+)", new Color(50, 100, 200)); 
        pnlAdd.add(btnAddToCart);
        leftPanel.add(pnlAdd, BorderLayout.SOUTH);

        // 4. PANEL KANAN (Keranjang & Pembayaran)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder(" Keranjang Belanja "));
        ((javax.swing.border.TitledBorder)rightPanel.getBorder()).setTitleFont(new Font("Verdana", Font.BOLD, 14));
        rightPanel.setPreferredSize(new Dimension(500, 700));

        cartModel = new DefaultTableModel();
        cartModel.addColumn("Nama Barang");
        cartModel.addColumn("Qty");
        cartModel.addColumn("Subtotal");
        
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setBackground(new Color(230, 250, 230)); 
        rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // --- UPDATE BAGIAN CHECKOUT (BAWAH KANAN) ---
        JPanel checkoutContainer = new JPanel(new BorderLayout());
        checkoutContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // A. Panel Rincian Harga (Subtotal, Diskon, PPN, Total)
        JPanel calcPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        
        // Baris Subtotal
        JPanel pnlSub = new JPanel(new BorderLayout());
        pnlSub.add(new JLabel("Subtotal:"), BorderLayout.WEST);
        lblSubtotal = new JLabel("Rp0");
        lblSubtotal.setFont(new Font("SansSerif", Font.BOLD, 14));
        pnlSub.add(lblSubtotal, BorderLayout.EAST);
        calcPanel.add(pnlSub);

        // Baris Input Diskon
        JPanel pnlDisc = new JPanel(new BorderLayout());
        pnlDisc.add(new JLabel("Diskon (%): "), BorderLayout.WEST);
        
        JPanel pnlInputDisc = new JPanel(new BorderLayout());
        txtDiscount = new JTextField("0");
        txtDiscount.setHorizontalAlignment(JTextField.RIGHT);
        btnProcessDiscount = new JButton("Set");
        btnProcessDiscount.setMargin(new Insets(2, 5, 2, 5));
        
        pnlInputDisc.add(txtDiscount, BorderLayout.CENTER);
        pnlInputDisc.add(btnProcessDiscount, BorderLayout.EAST);
        pnlDisc.add(pnlInputDisc, BorderLayout.CENTER);
        calcPanel.add(pnlDisc);

        // Baris PPN
        JPanel pnlTax = new JPanel(new BorderLayout());
        pnlTax.add(new JLabel("PPN (11%):"), BorderLayout.WEST);
        lblTax = new JLabel("Rp0");
        pnlTax.add(lblTax, BorderLayout.EAST);
        calcPanel.add(pnlTax);

        // Garis Pemisah
        calcPanel.add(new JSeparator());

        // Baris Grand Total
        JPanel pnlTotal = new JPanel(new BorderLayout());
        JLabel lTotalTitle = new JLabel("TOTAL BAYAR:");
        lTotalTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        pnlTotal.add(lTotalTitle, BorderLayout.WEST);
        
        lblGrandTotal = new JLabel("Rp0");
        lblGrandTotal.setFont(new Font("Verdana", Font.BOLD, 20));
        lblGrandTotal.setForeground(new Color(200, 50, 50));
        pnlTotal.add(lblGrandTotal, BorderLayout.EAST);
        calcPanel.add(pnlTotal);

        checkoutContainer.add(calcPanel, BorderLayout.CENTER);

        // B. Panel Tombol Aksi (Bayar, Reset, Logout)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton btnPay = createStyledButton("BAYAR", new Color(50, 150, 50)); 
        JButton btnClear = createStyledButton("Reset", new Color(200, 50, 50));
        JButton btnLogout = createStyledButton("Keluar", new Color(100, 100, 100));
        
        buttonPanel.add(btnPay);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnLogout);
        
        checkoutContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        rightPanel.add(checkoutContainer, BorderLayout.SOUTH);

        // Gabungkan Panel Utama
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(650); 
        splitPane.setDividerSize(10);
        add(splitPane, BorderLayout.CENTER);

        // --- EVENT LISTENER ---
        
        // 1. Logika Set Diskon
        btnProcessDiscount.addActionListener(e -> {
            try {
                double disc = Double.parseDouble(txtDiscount.getText());
                if (disc < 0 || disc > 100) {
                    JOptionPane.showMessageDialog(this, "Diskon harus 0-100%!");
                    txtDiscount.setText("0");
                    currentTransaction.setDiscountPercent(0);
                } else {
                    currentTransaction.setDiscountPercent(disc);
                }
                refreshCartTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan angka saja!");
            }
        });

        // 2. Logika Logout
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin Logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); 
                new LoginFrame().setVisible(true); 
            }
        });
        
        // 3. Logika Tambah Barang
        btnAddToCart.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) productTable.getValueAt(selectedRow, 0); 
                String nama = (String) productTable.getValueAt(selectedRow, 1);
                
                String qtyStr = JOptionPane.showInputDialog(this, "Masukkan Jumlah Beli untuk " + nama + ":");
                if (qtyStr != null && !qtyStr.isEmpty()) {
                    try {
                        int qty = Integer.parseInt(qtyStr);
                        addToCart(id, qty);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Harus masukkan angka!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk dulu di tabel kiri!");
            }
        });

        btnPay.addActionListener(e -> processPayment());

        btnClear.addActionListener(e -> {
            initNewTransaction();
            refreshCartTable();
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    private void initNewTransaction() {
        String trxID = "TRX-" + System.currentTimeMillis();
        this.currentTransaction = new Transaction(trxID, cashier);
        if (cartModel != null) cartModel.setRowCount(0);
        if (txtDiscount != null) txtDiscount.setText("0");
        if (lblSubtotal != null) lblSubtotal.setText("Rp0");
        if (lblTax != null) lblTax.setText("Rp0");
        if (lblGrandTotal != null) lblGrandTotal.setText("Rp0");
    }

    private void refreshProductTable() {
        productTable.setModel(db.getAllProductsForTable());
    }

    private void addToCart(String productId, int qty) {
        Product p = db.searchProductByID(productId);
        
        if (p != null) {
            if (p.getStock() >= qty) {
                for (int i = 0; i < qty; i++) {
                    currentTransaction.getCart().add(p);
                }
                refreshCartTable();
            } else {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup! Sisa: " + p.getStock());
            }
        }
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0); 
        ArrayList<Product> cart = currentTransaction.getCart();
        
        Map<String, Integer> counts = new HashMap<>();
        Map<String, Double> subtotals = new HashMap<>();
        
        for (Product p : cart) {
            counts.put(p.getProductName(), counts.getOrDefault(p.getProductName(), 0) + 1);
            subtotals.put(p.getProductName(), p.getPrice());
        }

        for (String name : counts.keySet()) {
            int qty = counts.get(name);
            double price = subtotals.get(name);
            cartModel.addRow(new Object[]{
                name,
                qty,
                db.formatRupiah(price * qty)
            });
        }
        
        // --- UPDATE LABEL RINCIAN HARGA (PENTING) ---
        lblSubtotal.setText(db.formatRupiah(currentTransaction.getSubtotal()));
        lblTax.setText(db.formatRupiah(currentTransaction.getTaxAmount()));
        lblGrandTotal.setText(db.formatRupiah(currentTransaction.totalHarga()));
    }

    private void processPayment() {
        double total = currentTransaction.totalHarga();
        if (total <= 0) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!");
            return;
        }

        String bayarStr = JOptionPane.showInputDialog(this, 
            "Total Belanja: " + db.formatRupiah(total) + "\nMasukkan Uang Pembayaran:");
            
        if (bayarStr != null) {
            try {
                double bayar = Double.parseDouble(bayarStr);
                if (bayar >= total) {
                    // 1. Simpan ke Database
                    db.saveTransaction(currentTransaction);
                    
                    Map<String, Integer> itemsToReduce = new HashMap<>();
                    for (Product p : currentTransaction.getCart()) {
                        itemsToReduce.put(p.getProductID(), itemsToReduce.getOrDefault(p.getProductID(), 0) + 1);
                    }
                    for (Map.Entry<String, Integer> entry : itemsToReduce.entrySet()) {
                        db.updateStock(entry.getKey(), entry.getValue());
                    }

                    // 2. HITUNG KEMBALIAN
                    double kembalian = bayar - total;

                    // 3. MUNCULKAN STRUK
                    printStruk(total, bayar, kembalian);
                    
                    // 4. Reset Transaksi
                    initNewTransaction();
                    refreshProductTable(); 
                    refreshCartTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Uang tidak cukup!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Input angka valid!");
            }
        }
    }
    
    // --- METHOD CETAK STRUK ---
    private void printStruk(double total, double bayar, double kembalian) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=====================================\n");
        sb.append("        MIRAIMONO HOBBY SHOP         \n");
        sb.append("     Jln. Wibu No. 1, Akihabara      \n");
        sb.append("=====================================\n");
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sb.append("Tanggal : " + sdf.format(currentTransaction.getDate()) + "\n");
        sb.append("ID Trx  : " + currentTransaction.getTransactionID() + "\n");
        sb.append("Kasir   : " + cashier.getNama() + "\n");
        sb.append("-------------------------------------\n");
        
        Map<String, Integer> qtyMap = new HashMap<>();
        Map<String, Double> priceMap = new HashMap<>(); 
        Map<String, String> nameMap = new HashMap<>();
        
        for (Product p : currentTransaction.getCart()) {
            String id = p.getProductID();
            qtyMap.put(id, qtyMap.getOrDefault(id, 0) + 1);
            priceMap.put(id, p.getPrice());
            nameMap.put(id, p.getProductName());
        }
        
        for (String id : qtyMap.keySet()) {
            String nama = nameMap.get(id);
            int qty = qtyMap.get(id);
            double hargaSatuan = priceMap.get(id);
            double subtotal = hargaSatuan * qty;
            
            sb.append(nama + "\n"); 
            sb.append("  " + qty + " x " + db.formatRupiah(hargaSatuan) + " = " + db.formatRupiah(subtotal) + "\n");
        }
        
        sb.append("-------------------------------------\n");
        // TAMBAHAN RINCIAN DISKON & PAJAK DI STRUK
        sb.append("Subtotal  : " + db.formatRupiah(currentTransaction.getSubtotal()) + "\n");
        
        if (currentTransaction.getDiscountPercent() > 0) {
            sb.append("Diskon (" + (int)currentTransaction.getDiscountPercent() + "%) : -" + 
                      db.formatRupiah(currentTransaction.getDiscountAmount()) + "\n");
        }
        
        sb.append("PPN (11%) : " + db.formatRupiah(currentTransaction.getTaxAmount()) + "\n");
        sb.append("-------------------------------------\n");
        
        sb.append("TOTAL     : " + db.formatRupiah(total) + "\n");
        sb.append("Tunai     : " + db.formatRupiah(bayar) + "\n");
        sb.append("Kembali   : " + db.formatRupiah(kembalian) + "\n");
        sb.append("=====================================\n");
        sb.append("      Terima Kasih Arigatou! ^^      \n");
        sb.append("=====================================\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.BOLD, 12)); 
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 450));
        
        Object[] options = {"Tutup", "Simpan Struk (Save As...)"};
        
        int choice = JOptionPane.showOptionDialog(this, 
                scrollPane, 
                "Struk Belanja", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                options, 
                options[0]);

        if (choice == 1) { 
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Struk Belanja");
            String defaultFileName = "Struk_" + currentTransaction.getTransactionID() + ".txt";
            fileChooser.setSelectedFile(new File(defaultFileName));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                }
                try (FileWriter fw = new FileWriter(fileToSave)) {
                    fw.write(sb.toString());
                    JOptionPane.showMessageDialog(this, "Struk berhasil disimpan di:\n" + fileToSave.getAbsolutePath());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + e.getMessage());
                }
            }
        }
    }
}