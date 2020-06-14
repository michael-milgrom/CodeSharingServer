package smartspace.layout;

import smartspace.data.UserEntity;

import java.util.ArrayList;
import java.util.List;

import smartspace.data.ActionType;

public class UserBoundary {
	private String email;
	private String password;
	private List<String> projects;
	
	public UserBoundary() {
		
	}

	public UserBoundary(String email, String password, List<String> projects) {
		super();
		this.email = email;
		this.password = password;
		this.projects = new ArrayList<>(projects);
	}


	public UserBoundary(UserEntity entity) {
		if(entity!=null) {
	
			this.email = entity.getEmail();
			this.password = entity.getPassword();
			this.projects = new ArrayList<>(entity.getProjects());
		}
	}
	
	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();
		
		entity.setPassword(this.password);
		entity.setProjects(this.projects);
		entity.setEmail(this.email);
		
		return entity;
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
		this.projects = projects;
	}

	@Override
	public String toString() {
		return "UserBoundary [email=" + email + ", password=" + password + ", projects=" + projects + "]";
	}

	
	
}
