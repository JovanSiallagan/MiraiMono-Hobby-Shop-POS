package module;

public class Admin extends User {

	public Admin(String userID, String username, String password, String nama, String role) {
		super(userID, username, password, nama, "Admin");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayMenu() {
		System.out.println("========== Menu Admin ==========");
		System.out.println("1. Add Product");
		System.out.println("2. Update Stock");
		System.out.println("3. Delete Product");
		System.out.println("4. Exit");
		System.out.print(">> ");
	}

}
