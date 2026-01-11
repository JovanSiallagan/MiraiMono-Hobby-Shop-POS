package module;

public abstract class Product {
	
	private String productID;
	private String productName;
	private String productType;
	private double price;
	private int stock;


	public Product(String productID, String productName, String productType, double price, int stock) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.productType = productType;
		this.price = price;
		this.stock = stock;
	}

	

	public int getStock() {
		return stock;
	}


	public void setStock(int stock) {
		this.stock = stock;
	}



	public String getProductID() {
		return productID;
	}



	public void setProductID(String productID) {
		this.productID = productID;
	}



	public String getProductName() {
		return productName;
	}



	public void setProductName(String productName) {
		this.productName = productName;
	}



	public String getProductType() {
		return productType;
	}



	public void setProductType(String productType) {
		this.productType = productType;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public abstract double hitungDiskon();
	
}
