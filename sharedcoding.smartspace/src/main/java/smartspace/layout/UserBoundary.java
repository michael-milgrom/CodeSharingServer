package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.ActionType;

public class UserBoundary {
	private UserForBoundary key;
	private String username;
	private String avatar;
	private String role;
	private long points;
	
	public UserBoundary() {
		
	}
	
	public UserBoundary(String username, String avatar, String role, long points) {
		super();
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}



	public UserBoundary(UserEntity entity) {
		if(entity!=null) {
			if(entity.getUserEmail() != null && entity.getUserSmartspace() != null) {
				this.key = new UserForBoundary(entity.getUserEmail(),  entity.getUserSmartspace());
			}
			else
				this.key = null;
			
			this.username = entity.getUsername();
			this.avatar = entity.getAvatar();
			
			if(entity.getRole() != null)
				this.role = entity.getRole().name();
			else
				this.role = null;
			
			this.points = entity.getPoints();
		}
	}
	
	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();
		
		entity.setUsername(this.username);
		entity.setAvatar(this.avatar);
		
		if(this.key!=null) {
			entity.setUserSmartspace(this.key.getSmartspace());
			entity.setUserEmail(this.key.getEmail());
		}
		
		if(this.role != null)
			entity.setRole(ActionType.valueOf(this.role));
		else
			entity.setRole(null);
		
		entity.setPoints(this.points);
		
		return entity;
	}

	public UserForBoundary getKey() {
		return key;
	}

	public void setKey(UserForBoundary key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "UserBoundary [key=" + key + ", username=" + username + ", avatar=" + avatar + ", role=" + role
				+ ", points=" + points + "]";
	}
	
	
}
