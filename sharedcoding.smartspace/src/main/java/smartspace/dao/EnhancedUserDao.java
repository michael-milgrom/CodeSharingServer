package smartspace.dao;

import java.util.List;

import smartspace.data.UserEntity;


public interface EnhancedUserDao<Key> extends UserDao<Key> {
	public List<UserEntity> readAll(int size, int page);
	public List<UserEntity> readAll(String sortBy, int size, int page);
	public List<UserEntity> readMessageWithEmailContaining (String email, int size, int page);
	public UserEntity createfromImport(UserEntity entity);
}
