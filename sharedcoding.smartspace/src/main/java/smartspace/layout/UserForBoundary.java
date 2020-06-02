package smartspace.layout;

public class UserForBoundary {

	private String email;
	private String smartspace;
	
	public UserForBoundary() {
		super();
	}

	public UserForBoundary(String email, String smartspace) {
		super();
		this.email = email;
		this.smartspace = smartspace;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
}
