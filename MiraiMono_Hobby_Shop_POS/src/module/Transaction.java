package module;

import java.util.ArrayList;
import java.util.Date;

public class Transaction {
	private String transactionID;
	private Cashier kasir;
	private ArrayList<Product> cart;
	private Date date;
	
	public Transaction(String transactionID, Cashier kasir, ArrayList<Product> cart, Date date) {
		super();
		this.transactionID = transactionID;
		this.kasir = kasir;
		this.cart = cart;
		this.date = date;
	}
	
	public Transaction(String transactionID, Cashier kasir) {
	    this.transactionID = transactionID;
	    this.kasir = kasir;
	    this.cart = new ArrayList<>(); 
	    this.date = new Date();
	}

	public double totalHarga() {
		double totalHarga = 0;
		for(Product p : cart) {
			totalHarga += p.getPrice() - p.hitungDiskon();
		}
		return totalHarga;
	}
	
	
	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public Cashier getKasir() {
		return kasir;
	}

	public void setKasir(Cashier kasir) {
		this.kasir = kasir;
	}

	public ArrayList<Product> getCart() {
		return cart;
	}

	public void setCart(ArrayList<Product> cart) {
		this.cart = cart;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
