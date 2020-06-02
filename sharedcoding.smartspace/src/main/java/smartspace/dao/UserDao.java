package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.UserEntity;

public interface UserDao<UserKey> {

	public UserEntity create(UserEntity userEntity);
	public Optional<UserEntity> readById(UserKey userKey);
	public List<UserEntity> readAll();
	public void update(UserEntity user);
	public void deleteAll();
}
