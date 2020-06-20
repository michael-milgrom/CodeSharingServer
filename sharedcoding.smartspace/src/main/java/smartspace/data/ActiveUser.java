package smartspace.data;

public class ActiveUser {
	private String email;
	private boolean isEditing;
	private int start;
	private int beforeEditLength;
	
	
	public ActiveUser() {
		super();
	}



	public ActiveUser(String email, boolean isEditing, int start, int beforeEditLength) {
		super();
		this.email = email;
		this.isEditing = isEditing;
		this.start = start;
		this.beforeEditLength = beforeEditLength;
	}
	
	public ActiveUser(ActiveUser user) {
		super();
		this.email = user.email;
		this.isEditing = user.isEditing;
		this.start = user.start;
		this.beforeEditLength = user.beforeEditLength;
	}


	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public boolean isEditing() {
		return isEditing;
	}



	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}



	public int getStart() {
		return start;
	}



	public void setStart(int start) {
		this.start = start;
	}


	public int getBeforeEditLength() {
		return beforeEditLength;
	}



	public void setBeforeEditLength(int beforeEditLength) {
		this.beforeEditLength = beforeEditLength;
	}



	@Override
	public String toString() {
		return "ActiveUser [email=" + email + ", isEditing=" + isEditing + ", start=" + start + "]";
	}
	
}
