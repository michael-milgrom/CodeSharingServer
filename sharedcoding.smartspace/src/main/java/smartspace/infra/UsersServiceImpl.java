package smartspace.infra;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;

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
		//check local admin
//		if (!(valiadete_admin(adminSmartspace, adminEmail))) {
//			throw new RuntimeException("user are not allowed to create actions");
//		}
		
		for(UserEntity user : users) {
			if(!valiadate(user))
				throw new RuntimeException("invalid user");
//			else {
//				if(this.smartspace.equals(user.getUserSmartspace()))
//					throw new RuntimeException("user smartspace must be different then the local project");
//			}
			this.userDao.createfromImport(user);
			users_entities.add(user);			
		}
		
		return users_entities;
	}
	
//	private boolean valiadete_admin(String adminSmartspace, String adminEmail) {
//		Optional<UserEntity> user = this.userDao.readById(adminSmartspace + "=" + adminEmail);
//		if(user.isPresent() && user.get().getRole().equals(ActionType.ADMIN))
//			return true;
//		return false;
//	}
	
	
	private boolean valiadate(UserEntity entity) {
				return entity != null &&
						entity.getEmail() != null &&
						!entity.getEmail().trim().isEmpty() &&
						entity.getPassword() != null &&
						!entity.getPassword().trim().isEmpty() &&
						entity.getProjects() != null;
			}

	@Override
	public List<UserEntity> getUsersUsingPagination(int size,
			int page) {
//		if(!(valiadete_admin(adminSmartspace, adminEmail)))
//			throw new RuntimeException("user are not allowed to get users");
//		else
			return this.userDao
					.readAll("email", size, page);
	}
}
