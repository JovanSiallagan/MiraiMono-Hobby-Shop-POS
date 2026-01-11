package module;

public class Figure extends Product {

    public Figure(String productID, String productName, String productType, double price, int stock) {
        super(productID, productName, productType, price, stock);
    }

    @Override
    public double hitungDiskon() {
        return 0; //
    }
}