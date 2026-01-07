package module;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/smartcashier";
    private static final String USER = "root";
    private static final String PASS = ""; // Sesuaikan password MySQL kamu
    private Connection conn;

    public void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi Database Berhasil!");
        } catch (SQLException e) {
            // Validasi: Menangani error koneksi database
            System.err.println("Koneksi Gagal: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }
    
    public void loadProducts() {
        String query = "SELECT * FROM product";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
               
               System.out.println("\n==================== DAFTAR STOK BARANG ====================");
               System.out.printf("%-10s | %-20s | %-15s | %-10s | %-5s\n", 
                                 "ID", "Nama", "Tipe", "Harga", "Stok");
               System.out.println("------------------------------------------------------------");
               
               while (rs.next()) {
                   System.out.printf("%-10s | %-20s | %-15s | %-10.0f | %-5d\n", 
                       rs.getString("ProductID"), 
                       rs.getString("ProductName"), 
                       rs.getString("type"), 
                       rs.getDouble("Price"), 
                       rs.getInt("stock"));
               }
               System.out.println("============================================================\n");
               
           } catch (SQLException e) {
               System.err.println("Gagal menampilkan stok: " + e.getMessage());
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
    
    public void addProductToDB(String id, String name, String type, double price, int stock) {
        String sql = "INSERT INTO product (ProductID, ProductName, type, Price, stock) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, stock);
            
            pstmt.executeUpdate();
            System.out.println("Produk berhasil ditambahkan ke database!");
        } catch (SQLException e) {
            System.err.println("Gagal tambah produk: " + e.getMessage());
        }
    }
    
    
 // Method untuk Admin (Restock)
    public void restockProduct(String productId, int jumlahTambah) {
        // Gunakan tanda + untuk menambah stok di database
        String query = "UPDATE product SET stock = stock + ? WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, jumlahTambah);
            pstmt.setString(2, productId);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Stok berhasil ditambah!");
            } else {
                System.out.println("ID Produk tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal update stok: " + e.getMessage());
        }
    }
    
    
    public void deleteProduct(String productId) {
        String sql = "DELETE FROM product WHERE ProductID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Produk dengan ID " + productId + " berhasil dihapus!");
            } else {
                System.out.println("ID Produk tidak ditemukan. Tidak ada yang dihapus.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghapus produk: " + e.getMessage());
        }
    }
    
    public void saveTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (transactionID, UserID, totalPrice, transactionDate) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getTransactionID());
            pstmt.setString(2, t.getKasir().getUserID());
            pstmt.setDouble(3, t.totalHarga());
            
            pstmt.setTimestamp(4, new java.sql.Timestamp(t.getDate().getTime()));
            
            pstmt.executeUpdate();
            System.out.println("Data transaksi berhasil disimpan ke database!");
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan transaksi: " + e.getMessage());
        }
    }
    
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
                int stock = rs.getInt("Stock");

                // Logika Polimorfisme: Mengembalikan objek sesuai tipenya
	                if (type.equalsIgnoreCase("Food")) {
	                    return new Food(pID, name, "Food", price, stock);
	                } else if (type.equalsIgnoreCase("Household")) {
	                    return new Household(pID, name ,"Household", price, stock);
	                } else if (type.equalsIgnoreCase("PersonalCare")) {
	                    return new PersonalCare(pID, name,"PersonalCare", price, stock);
	                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari produk: " + e.getMessage());
        }
        return null; // Mengembalikan null jika ID tidak ditemukan
    }
    
}