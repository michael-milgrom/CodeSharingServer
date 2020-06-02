package smartspace.infra;

import smartspace.data.UserEntity;
import smartspace.data.ActionType;

public interface UserUsersService {
	
	public UserEntity newUser(UserEntity user);
	
	public UserEntity getUser(String userEmail);
	
	//public void updateUser(String userSmartspace, String userEmail,ActionType role,UserEntity entity); // TODO WHY DO WE NEED ALL THIS
	
	public void updateUser(UserEntity user);
}
