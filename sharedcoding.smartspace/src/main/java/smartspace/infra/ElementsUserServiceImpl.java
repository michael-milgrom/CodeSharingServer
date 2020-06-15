package smartspace.infra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

@Service
public class ElementsUserServiceImpl implements ElementsUserService {

	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	private SequenceDao sequenceDao;

	@Autowired
	public ElementsUserServiceImpl(EnhancedElementDao<String> elementDao, EnhancedUserDao<String> userDao, SequenceDao sequenceDao) {
		super();
		this.elementDao = elementDao;
		this.userDao = userDao;
		this.sequenceDao = sequenceDao;
	}

	@Override
	@Transactional
	//@CheckRoleOfUser
	public ElementEntity newElement(ElementEntity element, String creator) {
		if (valiadate(element)) {
			//TODO do delete//
			Optional<UserEntity> user = this.userDao.readById(creator);
			if (user.isPresent()) {
				if(!user.get().getProjects().contains(element.getKey()))
					user.get().getProjects().add(element.getKey()); // add the project to the creator's list
				else
					throw new RuntimeException("The project already exists");
				this.userDao.update(user.get());
			} else
				throw new RuntimeException("The creator doesn't exist");
			element.setLastEditTimestamp((new Date()));
			this.elementDao.createWithId(element);
			} 
		else
			throw new RuntimeException("invalid element");
		return element;
	}

	@Override
	@Transactional
	//@CheckRoleOfUser
	public void setElement(String elementId,ElementEntity element) {
		//TODO CHECK THAT THE USER HAS PERMISSION TO EDIT
		element.setElementId(elementId);
		this.elementDao.update(element);
	}

	@Override
	//@CheckRoleOfUser
	public ElementEntity getSpecificElement(String email, String elementKey) {
		ElementEntity element = this.elementDao.readById(elementKey)
				.orElseThrow(() -> new RuntimeException("There is no element with the given key"));
		if(element.getUsers().contains(email))
			return element;
		else
			throw new RuntimeException("The user doesn't has access to this project");
	}

	@Override
	//@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPagination(String userEmail, int size, int page) { // TODO PAGINATION
		UserEntity user = this.userDao.readById(userEmail)
				.orElseThrow(() -> new RuntimeException("There is no user with the given key"));
		List<String> projects = user.getProjects();
		List<ElementEntity> elements_entities = new ArrayList<>();
		Optional<ElementEntity> element;
		for(String elementKey: projects) {
			element = this.elementDao.readById(elementKey);
			if(element.isPresent())
				elements_entities.add(this.elementDao.readById(elementKey).get());
		}
		return elements_entities;
		//return this.elementDao.readAll(size, page);
	}

	@Override
	//@CheckRoleOfUser
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String name, int size, int page) {
		
		return this.elementDao.readAllUsingName(name, size, page);
		
	}

//	@Override
//	//@CheckRoleOfUser
//	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(String userSmartspace, String userEmail, ActionType role,
//			String type, int size, int page) {
//
//		if (role == ActionType.MANAGER) {
//			return this.elementDao.readAllUsingType(type, size, page);
//		} else if (role == ActionType.PLAYER) {
//			return this.elementDao.readAllUsingTypeNotExpired(type, size, page);
//		} else {
//			throw new RuntimeException(
//					"The URl isn't match for manager or player. use another user or URL that match admin user");
//		}
//	}

	private boolean valiadate(ElementEntity entity) {
		return entity != null 
				&& entity.getName() != null && !entity.getName().trim().isEmpty() 
				&& entity.getCreator() != null && !entity.getCreator().trim().isEmpty() 
				&& entity.getNumberOfLines() >=0 
				&& entity.getUsers() != null 
				//&& entity.getElementId() != null && !entity.getElementId().trim().isEmpty()
				&& entity.getActiveUsers() != null
				&& entity.getLinesOfCode() != null;
				//&& entity.getLastEditTimestamp() != null;
	}

}
