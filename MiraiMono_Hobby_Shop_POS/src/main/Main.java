package main;

import module.*;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
	private static Cashier currentKasir;
	
    private static DatabaseManager db = new DatabaseManager();
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        db.connect(); 
        showMainMenu();
    }

    public static void showMainMenu() {
        boolean isRunning = true;

        while (isRunning) {
            try {
                System.out.println("\n===============================");
                System.out.println("   SMART CASHIER SYSTEM   ");
                System.out.println("===============================");
                System.out.println("1. Login Admin");
                System.out.println("2. Login Kasir");
                System.out.println("3. Keluar");
                System.out.print("Pilih Menu: ");

                int choice = scan.nextInt();
                scan.nextLine(); // Konsumsi newline

                switch (choice) {
                    case 1:
                        loginProcess("Admin");
                        break;
                    case 2:
                        loginProcess("Kasir");
                        break;
                    case 3:
                        System.out.println("Terima kasih telah menggunakan Smart Cashier.");
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Masukkan angka (1-3)!");
                scan.nextLine(); 
            }
        }
    }

    private static void loginProcess(String role) {
        System.out.println("\n--- Login " + role + " ---");
        System.out.print("Username: ");
        String user = scan.nextLine();
        System.out.print("Password: ");
        String pass = scan.nextLine();

        if (user.equals("admin") && role.equals("Admin")) {
            Admin adm = new Admin("A01", user, pass,"joni",role);
            while(true) {
            	adm.displayMenu();
                String choices = scan.nextLine();
                if(choices.equals("1")) {
                	try {
                        System.out.println("\n--- Tambah Produk Baru ---");
                        System.out.print("ID Produk: ");
                        String id = scan.nextLine();
                        System.out.print("Nama Produk: ");
                        String name = scan.nextLine();
                        System.out.print("Tipe (Food/Household/PersonalCare): ");
                        String type = scan.nextLine();
                        System.out.print("Harga: ");
                        double price = scan.nextDouble();
                        System.out.print("Stok: ");
                        int stock = scan.nextInt();
                        scan.nextLine(); 

                        db.addProductToDB(id, name, type, price, stock);
                        
                    } catch (InputMismatchException e) {
                        System.err.println("Input Salah! Harga dan Stok harus berupa angka.");
                        scan.nextLine(); 
                    }
                }else if(choices.equals("2")) {
                	try {
                        System.out.println("\n--- Update Stok Produk ---");
                        System.out.print("Masukkan ID Produk: ");
                        String id = scan.nextLine();
                        System.out.print("Jumlah Stok Tambahan: ");
                        int add = scan.nextInt();
                        scan.nextLine(); 

                        db.restockProduct(id, add);
                        
                    } catch (InputMismatchException e) {
                        System.err.println("Input Salah! Jumlah stok harus angka.");
                        scan.nextLine(); 
                    }
                }else if(choices.equals("3")) {
                	System.out.println("\n--- Hapus Produk ---");
                    System.out.print("Masukkan ID Produk yang akan dihapus: ");
                    String id = scan.nextLine();

                    System.out.print("Apakah Anda yakin ingin menghapus " + id + "? (y/n): ");
                    String confirm = scan.nextLine();

                    if (confirm.equalsIgnoreCase("y")) {
                        db.deleteProduct(id);
                    } else {
                        System.out.println("Penghapusan dibatalkan.");
                    }
                }else if(choices.equals("4")) {
                	break;
                }
            }
        } else if (user.equals("kasir") && role.equals("Kasir")) {
            Cashier ksr = new Cashier("K01", user, pass, "joni",role);
            currentKasir = ksr;
            while(true) {
            	 ksr.displayMenu();
                 String choices = scan.nextLine();
                 if(choices.equals("1")) {
                	 String trxID = "TRX-" + (System.currentTimeMillis() / 1000);
                	    
                	    // Inisialisasi Objek Transaksi (CurrentKasir adalah objek Kasir yang sedang login)
                	    Transaction trans = new Transaction(trxID, currentKasir);
                	    
                	    boolean adding = true;
                	    while (adding) {
                	        System.out.println("\n--- Input Barang ---");
                	        System.out.print("Masukkan ID Produk: ");
                	        String pID = scan.nextLine();
                	        
                	        // 1. Cari produk di database (Method searchProductByID di DatabaseManager)
                	        Product p = db.searchProductByID(pID); 
                	        
                	        if (p != null) {
                	            System.out.print("Jumlah beli untuk " + p.getProductName() + ": ");
                	            int qty = scan.nextInt();
                	            scan.nextLine(); // Clear buffer

                	            // Validasi Stok
                	            if (p.getStock() >= qty) {
                	                // 2. Tambahkan ke keranjang (ArrayList di class Transaction)
                	                for(int i=0; i < qty; i++) {
                	                    trans.getCart().add(p); 
                	                }
                	                
                	                // 3. Update stok di database (Kurangi stok)
                	                db.updateStock(pID, qty); 
                	                System.out.println("Berhasil ditambahkan.");
                	            } else {
                	                System.out.println("Gagal! Stok hanya sisa: " + p.getStock());
                	            }
                	        } else {
                	            System.out.println("Produk tidak ditemukan di database!");
                	        }

                	        System.out.print("Tambah barang lain? (y/n): ");
                	        if (scan.nextLine().equalsIgnoreCase("n")) {
                	            adding = false;
                	        }
                	    }

                	    // --- PROSES PEMBAYARAN ---
                	    double total = trans.totalHarga(); // Menggunakan Polymorphism hitungDiskon()
                	    System.out.println("\n============================");
                	    System.out.println("TOTAL BELANJA : Rp" + total);
                	    System.out.println("============================");
                	    
                	    boolean paymentValid = false;
                	    while (!paymentValid) {
                	        try {
                	            System.out.print("Bayar (Tunai) : Rp");
                	            double bayar = scan.nextDouble();
                	            scan.nextLine();

                	            if (bayar >= total) {
                	                double kembalian = bayar - total;
                	                System.out.println("KEMBALIAN     : Rp" + kembalian);
                	                
                	                // 4. Simpan Header Transaksi ke Database
                	                db.saveTransaction(trans); 
                	                
                	                System.out.println("\nTransaksi Berhasil Disimpan!");
                	                paymentValid = true;
                	            } else {
                	                System.out.println("Uang tidak cukup!");
                	            }
                	        } catch (InputMismatchException e) {
                	            System.out.println("Error: Masukkan angka untuk nominal uang!");
                	            scan.nextLine();
                	        }
                	    }
                 }else if(choices.equals("2")) {
                	 db.loadProducts();
                	    System.out.println("Tekan Enter untuk kembali ke menu...");
                	    scan.nextLine();
                 }else if(choices.equals("3")) {
                 	break;
                 }
            }
        } else {
            System.err.println("Login Gagal! Username/Role tidak cocok.");
        }
    }
}