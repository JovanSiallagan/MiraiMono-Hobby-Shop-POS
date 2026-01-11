package module;

public class Clothing extends Product {
    public Clothing(String productID, String productName, String productType, double price, int stock) {
        super(productID, productName, productType, price, stock);
    }

    @Override
    public double hitungDiskon() {
        return 0; 
    }
}