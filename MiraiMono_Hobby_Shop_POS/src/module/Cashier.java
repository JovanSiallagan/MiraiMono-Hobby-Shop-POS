package module;

public class Cashier extends User {

	public Cashier(String userID, String username, String password, String nama, String role) {
		super(userID, username, password, nama, "Cashier");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayMenu() {
		System.out.println("========== Menu Cashier ==========");
		System.out.println("1. Start new transaction");
		System.out.println("2. View Stock");
		System.out.println("3. Exit");
		System.out.print(">> ");		
	}

}
