package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import module.DatabaseManager;

public class AdminDashboardFrame extends JFrame {
    private JTable table;
    private DatabaseManager db;
    private JButton btnRefresh, btnAdd, btnEdit, btnDelete, btnHistory, btnLogout;

    public AdminDashboardFrame(DatabaseManager db) {
        this.db = db;

        // 1. Setup Dasar Window
        setTitle("Admin Dashboard - MiraiMono Hobby Shop");
        setSize(1100, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. HEADER
        JLabel lblTitle = new JLabel("Admin Dashboard - Stock Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 150)); 
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); 
        add(lblTitle, BorderLayout.NORTH);

        // 3. TABEL DATA
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 250)); 
        
        refreshTable(); 
        
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // 4. TOMBOL-TOMBOL
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20)); 
        
        btnRefresh = createStyledButton("Refresh", new Color(50, 100, 200));
        btnAdd = createStyledButton("Tambah", new Color(50, 150, 50)); 
        btnEdit = createStyledButton("Edit", new Color(255, 140, 0)); 
        btnDelete = createStyledButton("Hapus", new Color(200, 50, 50)); 
        btnHistory = createStyledButton("Riwayat Trx", new Color(128, 0, 128));
        btnLogout = createStyledButton("Logout", new Color(100, 100, 100));

        panelButton.add(btnRefresh);
        panelButton.add(btnAdd);
        panelButton.add(btnEdit);
        panelButton.add(btnDelete);
        panelButton.add(btnHistory);
        panelButton.add(btnLogout);
        
        add(panelButton, BorderLayout.SOUTH);

        // --- EVENT LISTENER ---
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin Logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); 
                new LoginFrame().setVisible(true); 
            }
        });

        btnHistory.addActionListener(e -> {
            new HistoryFrame(db).setVisible(true);
        });

        btnRefresh.addActionListener(e -> refreshTable());

        // LOGIKA EDIT
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String idLama = (String) table.getValueAt(selectedRow, 0);
                String namaLama = (String) table.getValueAt(selectedRow, 1);
                String tipeLama = (String) table.getValueAt(selectedRow, 2);
                String hargaStr = (String) table.getValueAt(selectedRow, 3); 
                int stokLama = (int) table.getValueAt(selectedRow, 4);

                String hargaBersih = hargaStr.replace("Rp", "").replace(".", "").trim();

                JPanel panelInput = new JPanel(new GridLayout(5, 2, 10, 20)); 
                panelInput.setPreferredSize(new Dimension(400, 250)); 

                JTextField idField = new JTextField(idLama);
                idField.setEditable(false); 
                idField.setBackground(Color.LIGHT_GRAY); 

                JTextField nameField = new JTextField(namaLama);
                
                String[] types = {"Figure", "Merch", "Clothing", "Gacha"}; 
                JComboBox<String> typeBox = new JComboBox<>(types);
                typeBox.setSelectedItem(tipeLama); 

                JTextField priceField = new JTextField(hargaBersih);
                JTextField stockField = new JTextField(String.valueOf(stokLama));

                panelInput.add(new JLabel("ID Produk (Locked):"));
                panelInput.add(idField);
                panelInput.add(new JLabel("Nama Produk:"));
                panelInput.add(nameField);
                panelInput.add(new JLabel("Tipe Kategori:"));
                panelInput.add(typeBox);
                panelInput.add(new JLabel("Harga (Rp):"));
                panelInput.add(priceField);
                panelInput.add(new JLabel("Stok:"));
                panelInput.add(stockField);

                int option = JOptionPane.showConfirmDialog(null, panelInput, "Edit Produk: " + namaLama, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String name = nameField.getText();
                        String type = (String) typeBox.getSelectedItem();
                        double price = Double.parseDouble(priceField.getText());
                        int stock = Integer.parseInt(stockField.getText());

                        if (stock < 0) {
                            JOptionPane.showMessageDialog(null, "Stok tidak boleh negatif!", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else if (price < 0) {
                            JOptionPane.showMessageDialog(null, "Harga tidak boleh negatif!", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {
                            db.updateProductFull(idLama, name, type, price, stock);
                            refreshTable();
                            JOptionPane.showMessageDialog(null, "Data Produk Berhasil Diperbarui!");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih baris produk dulu!");
            }
        });

        // LOGIKA TAMBAH
        btnAdd.addActionListener(e -> {
            JPanel panelInput = new JPanel(new GridLayout(5, 2, 10, 20)); 
            panelInput.setPreferredSize(new Dimension(400, 250)); 

            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            String[] types = {"Figure", "Merch", "Clothing", "Gacha"}; 
            JComboBox<String> typeBox = new JComboBox<>(types);
            JTextField priceField = new JTextField();
            JTextField stockField = new JTextField();

            panelInput.add(new JLabel("ID Produk:"));
            panelInput.add(idField);
            panelInput.add(new JLabel("Nama Produk:"));
            panelInput.add(nameField);
            panelInput.add(new JLabel("Tipe Kategori:"));
            panelInput.add(typeBox);
            panelInput.add(new JLabel("Harga (Rp):"));
            panelInput.add(priceField);
            panelInput.add(new JLabel("Stok Awal:"));
            panelInput.add(stockField);

            int option = JOptionPane.showConfirmDialog(null, panelInput, "Form Tambah Produk", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String id = idField.getText();
                    String name = nameField.getText();
                    String type = (String) typeBox.getSelectedItem();
                    double price = Double.parseDouble(priceField.getText());
                    int stock = Integer.parseInt(stockField.getText());

                    if (stock < 0) {
                        JOptionPane.showMessageDialog(null, "Stok tidak boleh negatif!", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (price < 0) {
                        JOptionPane.showMessageDialog(null, "Harga tidak boleh negatif!", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        db.addProductToDB(id, name, type, price, stock);
                        refreshTable();
                        JOptionPane.showMessageDialog(null, "Produk Berhasil Ditambahkan!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Input Error: " + ex.getMessage());
                }
            }
        });

        // LOGIKA HAPUS
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(null, "Hapus produk " + id + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    db.deleteProduct(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih baris yang mau dihapus dulu!");
            }
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12)); 
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 40)); 
        return btn;
    }

    private void refreshTable() {
        DefaultTableModel model = db.getAllProductsForTable();
        table.setModel(model);
    }
}