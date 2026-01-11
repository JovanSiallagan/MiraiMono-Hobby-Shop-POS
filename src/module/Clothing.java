package module; // Bisa juga dipakai untuk Gacha

public class Clothing extends Product {
    public Clothing(String productID, String productName, String productType, double price, int stock) {
        super(productID, productName, productType, price, stock);
    }

    @Override
    public double hitungDiskon() {
        return 0; 
    }
}