package smartspace.infra;

import smartspace.data.UserEntity;
import smartspace.data.ActionType;

public interface UserUsersService {
	
	public UserEntity newUser(UserEntity user);
	
	public UserEntity getUser(String userEmail, String password);
	
	public void updateUser(UserEntity user);
}
