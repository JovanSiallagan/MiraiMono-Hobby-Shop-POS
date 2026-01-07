package module;

public abstract class User {
	private String UserID;
	private String Username;
	private String password;
	private String nama;
	private String role;
	
	public User(String userID, String username, String password, String nama, String role) {
		super();
		UserID = userID;
		Username = username;
		this.password = password;
		this.nama = nama;
		this.role = role;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public abstract void displayMenu();
	
}
