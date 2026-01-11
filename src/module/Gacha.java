package module;

public class Gacha extends Product {

    public Gacha(String productID, String productName, double price, int stock) {
        super(productID, productName, "Gacha", price, stock);
    }

    @Override
    public double hitungDiskon() {
        return 0;
    }
}