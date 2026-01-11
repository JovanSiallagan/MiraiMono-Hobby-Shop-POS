package module;

public class Gacha extends Product {

    public Gacha(String productID, String productName, double price, int stock) {
        // PERBAIKAN: Menambahkan "Gacha" sebagai parameter ke-3 (Type)
        super(productID, productName, "Gacha", price, stock);
    }

    @Override
    public double hitungDiskon() {
        return 0; // Tidak ada diskon
    }
}