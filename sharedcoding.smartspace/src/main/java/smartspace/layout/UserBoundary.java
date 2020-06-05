package smartspace.layout;

import smartspace.data.UserEntity;

import java.util.ArrayList;
import java.util.List;

import smartspace.data.ActionType;

public class UserBoundary {
	private UserForBoundary key;
	private String password;
	private List<String> projects;
	
	public UserBoundary() {
		
	}

	public UserBoundary(UserForBoundary key, String password, List<String> projects) {
		super();
		this.key = key;
		this.password = password;
		this.projects = new ArrayList<>(projects);
	}


	public UserBoundary(UserEntity entity) {
		if(entity!=null) {
			if(entity.getEmail() != null) {
				this.key = new UserForBoundary(entity.getEmail());
			}
			else
				this.key = null;
			
			this.password = entity.getPassword();
			this.projects = new ArrayList<>(entity.getProjects());
		}
	}
	
	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();
		
		entity.setPassword(this.password);
		entity.setProjects(this.projects);
		
		if(this.key!=null) {
			entity.setEmail(this.key.getEmail());
		}
		
		return entity;
	}

	public UserForBoundary getKey() {
		return key;
	}

	public void setKey(UserForBoundary key) {
		this.key = key;
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
		return "UserBoundary [key=" + key + ", password=" + password + ", projects=" + projects + "]";
	}

	
	
}
