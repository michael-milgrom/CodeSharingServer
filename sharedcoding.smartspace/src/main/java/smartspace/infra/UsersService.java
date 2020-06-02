package smartspace.infra;

import java.util.List;

import smartspace.data.UserEntity;


public interface UsersService {

	public List<UserEntity> newUsers(List<UserEntity> users);
	
	public List<UserEntity> getUsersUsingPagination (int size, int page);
}
