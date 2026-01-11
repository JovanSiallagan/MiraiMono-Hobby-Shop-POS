package module;

public class Merch extends Product {
    public Merch(String productID, String productName, String productType, double price, int stock) {
        super(productID, productName, productType, price, stock);
    }

    @Override
    public double hitungDiskon() {
        return 0; 
    }
}