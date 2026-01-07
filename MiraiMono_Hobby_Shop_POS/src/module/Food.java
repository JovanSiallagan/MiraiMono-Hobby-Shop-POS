package module;

public class Food extends Product {

	public Food(String productID, String productName, String productType, double price, int stock) {
		super(productID, productName, productType, price, stock);
	}

	@Override
	public double hitungDiskon() {
		return getPrice() * 0.05 ;
	}
}
