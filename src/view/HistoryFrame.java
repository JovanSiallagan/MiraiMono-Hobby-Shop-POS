package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import module.DatabaseManager;

public class HistoryFrame extends JFrame {
    private JTable table;
    private DatabaseManager db;
    private JButton btnClose, btnRefresh;

    public HistoryFrame(DatabaseManager db) {
        this.db = db;

        // 1. Setup Window
        setTitle("Riwayat Transaksi Lengkap - MiraiMono Hobby Shop");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. HEADER
        JLabel lblTitle = new JLabel("Laporan Riwayat Transaksi", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 150));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        // 3. TABEL
        table = new JTable();
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(230, 230, 250));
        
        refreshData(); 

        JScrollPane scrollPane = new JScrollPane(table);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // 4. TOMBOL BAWAH
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        
        btnRefresh = new JButton("Refresh Data");
        styleButton(btnRefresh, new Color(50, 100, 200));

        btnClose = new JButton("Tutup");
        styleButton(btnClose, new Color(200, 50, 50));

        panelButton.add(btnRefresh);
        panelButton.add(btnClose);
        add(panelButton, BorderLayout.SOUTH);

        // --- EVENT LISTENER ---
        btnRefresh.addActionListener(e -> refreshData());
        btnClose.addActionListener(e -> dispose());
    }

    private void refreshData() {
        DefaultTableModel model = db.getTransactionHistory();
        table.setModel(model);
        
        // SETUP LEBAR KOLOM
        // Kolom: ID, Kasir, Items, Subtotal, Diskon, PPN, Total, Waktu
        if (table.getColumnCount() >= 8) {
            table.getColumnModel().getColumn(0).setPreferredWidth(120); // ID Trx
            table.getColumnModel().getColumn(1).setPreferredWidth(100); // Kasir
            table.getColumnModel().getColumn(2).setPreferredWidth(300); // Item Barang (LEBAR)
            table.getColumnModel().getColumn(3).setPreferredWidth(100); // Subtotal
            table.getColumnModel().getColumn(4).setPreferredWidth(100); // Diskon
            table.getColumnModel().getColumn(5).setPreferredWidth(100); // PPN
            table.getColumnModel().getColumn(6).setPreferredWidth(120); // Grand Total
            table.getColumnModel().getColumn(7).setPreferredWidth(150); // Waktu
        }
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
    }
}