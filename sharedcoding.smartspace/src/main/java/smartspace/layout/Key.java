package smartspace.layout;

public class Key {
	private String id;
	private String smartspace;
	
	public Key() {
		
	}

	public Key(String id, String smartspace) {
		super();
		this.id = id;
		this.smartspace = smartspace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}	
}
