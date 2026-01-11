package module;

import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/smartcashier";
    private static final String USER = "root";
    private static final String PASS = ""; 
    
    private Connection conn;

    public DatabaseManager() {
        connect();
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi Database Berhasil!");
        } catch (SQLException e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- METHOD LOGIN ---
    public User validateLogin(String username, String password) {
        if (conn == null) return null;

        String query = "SELECT * FROM users WHERE Username = ? AND passwords = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String id = rs.getString("UserID");
                String nama = rs.getString("nama");
                
                if (role.equalsIgnoreCase("Admin")) {
                    return new Admin(id, username, password, nama, role);
                } else {
                    return new Cashier(id, username, password, nama, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- METHOD UNTUK TABEL GUI ---
    public DefaultTableModel getAllProductsForTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Tipe");
        model.addColumn("Harga");
        model.addColumn("Stok");

        String query = "SELECT * FROM product";
        if (conn == null) return model;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ProductID"));
                row.add(rs.getString("ProductName"));
                row.add(rs.getString("type"));
                row.add(formatRupiah(rs.getDouble("Price")));
                row.add(rs.getInt("stock"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    // --- UPDATE: SIMPAN TRANSAKSI ---
    public void saveTransaction(Transaction t) {
        // 1. Simpan Header Transaksi
        String sqlHeader = "INSERT INTO transactions (transactionID, UserID, subtotal, discountAmount, taxAmount, totalPrice, transactionDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlHeader)) {
            pstmt.setString(1, t.getTransactionID());
            pstmt.setString(2, t.getKasir().getUserID());
            
            // Data Keuangan
            pstmt.setDouble(3, t.getSubtotal());
            pstmt.setDouble(4, t.getDiscountAmount());
            pstmt.setDouble(5, t.getTaxAmount());
            pstmt.setDouble(6, t.totalHarga());
            
            pstmt.setTimestamp(7, new java.sql.Timestamp(t.getDate().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal simpan header: " + e.getMessage());
        }

        // 2. Simpan Detail Barang
        Map<String, Integer> qtyMap = new HashMap<>();
        Map<String, Double> priceMap = new HashMap<>(); 

        for (Product p : t.getCart()) {
            qtyMap.put(p.getProductID(), qtyMap.getOrDefault(p.getProductID(), 0) + 1);
            priceMap.put(p.getProductID(), p.getPrice()); 
        }

        String sqlDetail = "INSERT INTO transaction_detail (transactionID, ProductID, qty, subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
            for (String pid : qtyMap.keySet()) {
                int qty = qtyMap.get(pid);
                double hargaSatuan = priceMap.get(pid);
                double subtotalBarang = hargaSatuan * qty; 
                
                pstmt.setString(1, t.getTransactionID());
                pstmt.setString(2, pid);
                pstmt.setInt(3, qty); 
                pstmt.setDouble(4, subtotalBarang); 
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Gagal simpan detail: " + e.getMessage());
        }
    }

    public void updateStock(String productId, int jumlahBeli) {
        String query = "UPDATE product SET stock = stock - ? WHERE productID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, jumlahBeli);
            pstmt.setString(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal update stok: " + e.getMessage());
        }
    }
    
    // --- SEARCH PRODUCT ---
    public Product searchProductByID(String id) {
        String query = "SELECT * FROM product WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String pID = rs.getString("ProductID");
                String name = rs.getString("ProductName");
                String type = rs.getString("type");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("stock");

                if (type.equalsIgnoreCase("Figure")) {
                    return new Figure(pID, name, "Figure", price, stock); 
                } else if (type.equalsIgnoreCase("Merch")) {
                    return new Merch(pID, name, type, price, stock);
                } else if (type.equalsIgnoreCase("Gacha")) { 
                    return new Gacha(pID, name, price, stock);
                } else {
                    return new Clothing(pID, name, type, price, stock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addProductToDB(String id, String name, String type, double price, int stock) {
        String sql = "INSERT INTO product (ProductID, ProductName, type, Price, stock) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, stock);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal tambah produk: " + e.getMessage());
        }
    }

    public void deleteProduct(String productId) {
        String sql = "DELETE FROM product WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal hapus produk: " + e.getMessage());
        }
    }
    
    public String formatRupiah(double number) {
        NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp" + format.format(number);
    }

    public void updateProductFull(String id, String name, String type, double price, int stock) {
        String query = "UPDATE product SET ProductName = ?, type = ?, Price = ?, stock = ? WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, stock);
            pstmt.setString(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal update produk: " + e.getMessage());
        }
    }
    
    // --- UPDATE: AMBIL RIWAYAT TRANSAKSI ---
    public DefaultTableModel getTransactionHistory() {
        DefaultTableModel model = new DefaultTableModel();
        // Kolom Tabel
        model.addColumn("ID Trx");
        model.addColumn("Kasir");
        model.addColumn("Item Barang");
        model.addColumn("Subtotal");
        model.addColumn("Diskon");
        model.addColumn("PPN (11%)");
        model.addColumn("Grand Total");
        model.addColumn("Waktu");

        String query = "SELECT t.transactionID, u.nama, t.transactionDate, " +
                       "t.subtotal, t.discountAmount, t.taxAmount, t.totalPrice, " +
                       "GROUP_CONCAT(CONCAT(p.ProductName, ' (', td.qty, ')') SEPARATOR ', ') as items " +
                       "FROM transactions t " +
                       "JOIN users u ON t.UserID = u.UserID " +
                       "JOIN transaction_detail td ON t.transactionID = td.transactionID " +
                       "JOIN product p ON td.ProductID = p.ProductID " +
                       "GROUP BY t.transactionID " +
                       "ORDER BY t.transactionDate DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("transactionID"));
                row.add(rs.getString("nama"));
                row.add(rs.getString("items"));
                
                // Ambil data keuangan baru
                row.add(formatRupiah(rs.getDouble("subtotal")));
                row.add(formatRupiah(rs.getDouble("discountAmount")));
                row.add(formatRupiah(rs.getDouble("taxAmount")));
                row.add(formatRupiah(rs.getDouble("totalPrice")));
                
                row.add(rs.getTimestamp("transactionDate")); 
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
}