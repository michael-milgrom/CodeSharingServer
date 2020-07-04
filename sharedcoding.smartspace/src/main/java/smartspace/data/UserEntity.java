package smartspace.data;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="USERS")
public class UserEntity implements SmartspaceEntity<String> {
	private String email;
	private String password;
	private List<String> projects;
	private String key;
	public static final String SEQUENCE_NAME = "users_sequence";
	
	
	public UserEntity(String email, String password, List<String> projects) {
		super();
		this.email = email;
		this.password = password;
		this.projects = new ArrayList<String>(projects);
	}
	
	public UserEntity() {
		super();
		projects = new ArrayList<String>();
	}
	
	public UserEntity(String email) {
		this.email = email;
	}



	@JsonIgnore
	public String getEmail() {
		return email;
	}
	

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return this.password;
	}
	

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public List<String> getProjects() {
		return this.projects;
	}
	

	public void setProjects(List<String> projects) {
		this.projects = new ArrayList<String>(projects);
	}


	@Override
	@Id
	public String getKey() {
		return this.email;
	}
	
	@Override
	public void setKey(String key) {	
		if(key != null || key != "") {
			this.key = key;
			this.email = key;
		}
		
	}

	
	@Override
	public String toString() {
		return "UserEntity [email=" + email + ", password=" + password
				+ ", projects=" + projects + "]";
	}

	@JsonIgnore
	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}
}
