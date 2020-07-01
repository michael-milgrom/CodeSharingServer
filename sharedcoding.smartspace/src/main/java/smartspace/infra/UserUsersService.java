package smartspace.infra;

import smartspace.data.UserEntity;

public interface UserUsersService {
	
	public UserEntity newUser(UserEntity user);
	
	public UserEntity getUser(String userEmail, String password);
	
	public void updateUser(UserEntity user);
}
