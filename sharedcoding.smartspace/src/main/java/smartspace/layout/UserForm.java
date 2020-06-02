package smartspace.layout;

import java.util.ArrayList;
import java.util.List;

public class UserForm {
	private String email;
	private String password;
	private List<String> projects;
	
	public UserForm() {
	}
	
	public UserForm(String email,String password,List<String> projects) {
		this.email = email;
		this.password = password;
		this.projects = new ArrayList<>(projects);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = new ArrayList<>(projects);
	}
	
	
}

