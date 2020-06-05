package smartspace.layout;

public class Key {
	private String user;
	private String id;
	
	public Key() {
		
	}

	public Key(String user, String id) {
		super();
		this.user = user;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	
}
