package smartspace.infra;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;

@Service
public class UsersServiceImpl implements UsersService {

	private EnhancedUserDao<String> userDao;
	private String smartspace;
	
	@Autowired	
	public UsersServiceImpl(EnhancedUserDao<String> userDao) {
		super();
		this.userDao = userDao;
	}
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public List<UserEntity> newUsers(List<UserEntity> users) {
		List<UserEntity> users_entities = new ArrayList<UserEntity>();
		
		for(UserEntity user : users) {
			if(!valiadate(user))
				throw new RuntimeException("invalid user");
			this.userDao.createfromImport(user);
			users_entities.add(user);			
		}
		
		return users_entities;
	}
	
	
	private boolean valiadate(UserEntity entity) {
		return entity != null &&
				entity.getEmail() != null && !entity.getEmail().trim().isEmpty() &&
				entity.getPassword() != null && !entity.getPassword().trim().isEmpty() &&
				entity.getProjects() != null;
	}

	@Override
	public List<UserEntity> getUsersUsingPagination(int size, int page) {
		return this.userDao.readAll("email", size, page);
	}
}
