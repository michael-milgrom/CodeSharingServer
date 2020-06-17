package smartspace.infra;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.layout.UserForm;

@Service
public class UserUsersServiceImpl implements UserUsersService{

	private EnhancedUserDao<String> userDao;
	private String smartspace;
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Autowired	
	public UserUsersServiceImpl(EnhancedUserDao<String> userDao) {
		super();
		this.userDao = userDao;
	}
	
	@Override
	@Transactional
	public UserEntity newUser(UserEntity user) {
		return this.userDao.create(user);
	}
	
	@Override
	public UserEntity getUser(String email, String password) {
		UserEntity user = this.userDao.readById(email).get();
		if(user.getPassword().equals(password))
			return user;
		else
			throw new RuntimeException("password does not match");
	}
//	@Override
//	public UserEntity getUser(String email) {
//		return this.userDao.readById(email).get();
//		
//	}

	@Override
	@Transactional
	public void updateUser(UserEntity user) {
		
		this.userDao.update(user);
	}

	public UserEntity convertToUserEntity(UserForm newUser) {
		return new UserEntity(newUser.getEmail(), newUser.getPassword(), newUser.getProjects());
	}
}
