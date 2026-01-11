package module;

import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    private String transactionID;
    private Cashier cashier;
    private ArrayList<Product> cart;
    private Date date;
    
    // Variabel Baru
    private double discountPercent = 0; // Diskon dalam persen (0 - 100)
    private final double TAX_PERCENT = 0.11; // PPN 11% (Final)

    public Transaction(String transactionID, Cashier cashier) {
        this.transactionID = transactionID;
        this.cashier = cashier;
        this.cart = new ArrayList<>();
        this.date = new Date();
    }

    public String getTransactionID() { return transactionID; }
    public Cashier getKasir() { return cashier; }
    public ArrayList<Product> getCart() { return cart; }
    public Date getDate() { return date; }

    // Setter Diskon (Dipanggil saat Kasir input angka)
    public void setDiscountPercent(double percent) {
        this.discountPercent = percent;
    }
    
    public double getDiscountPercent() {
        return discountPercent;
    }

    // --- RUMUS PERHITUNGAN LENGKAP ---
    
    // 1. Hitung Subtotal (Total Harga Barang Murni)
    public double getSubtotal() {
        double sub = 0;
        for (Product p : cart) {
            // Karena diskon per barang sudah dimatikan (0), ini aman
            sub += (p.getPrice() - p.hitungDiskon()); 
        }
        return sub;
    }

    // 2. Hitung Nominal Diskon (Rupiah)
    public double getDiscountAmount() {
        return getSubtotal() * (discountPercent / 100.0);
    }

    // 3. Hitung Dasar Pengenaan Pajak (Subtotal - Diskon)
    public double getTaxBase() {
        return getSubtotal() - getDiscountAmount();
    }

    // 4. Hitung PPN 11%
    public double getTaxAmount() {
        return getTaxBase() * TAX_PERCENT;
    }

    // 5. GRAND TOTAL (Yang harus dibayar)
    public double totalHarga() {
        return getTaxBase() + getTaxAmount();
    }
}