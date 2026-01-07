package module;

public class Household extends Product{

	public Household(String productID, String productName, String productType, double price, int stock) {
		super(productID, productName, productType, price, stock);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double hitungDiskon() {
		return getPrice() * 0.1;
	}

}
