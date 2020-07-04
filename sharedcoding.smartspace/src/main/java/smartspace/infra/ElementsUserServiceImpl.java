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
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

@Service
public class ElementsUserServiceImpl implements ElementsUserService {

	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;

	@Autowired
	public ElementsUserServiceImpl(EnhancedElementDao<String> elementDao, EnhancedUserDao<String> userDao) {
		super();
		this.elementDao = elementDao;
		this.userDao = userDao;
	}

	@Override
	@Transactional
	public ElementEntity newElement(ElementEntity element, String creator) {
		if (valiadate(element)) {
	
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
	public void setElement(String email, String elementId,ElementEntity element) {
		if(!element.getUsers().contains(email))
			throw new RuntimeException("The user doesn't has access to this project");
		element.setElementId(elementId);
		this.elementDao.update(element);
	}
	
	@Override
	@Transactional
	public void setElementCode(String email, String elementId,ElementEntity element) {
		if(!element.getUsers().contains(email))
			throw new RuntimeException("The user doesn't has access to this project");
		element.setElementId(elementId);
		element.setLastEditTimestamp(new Date());
		element.setNumberOfLines(element.getNumberOfLines());
		this.elementDao.updateLinesOfCode(element);
	}

	@Override
	public ElementEntity getSpecificElement(String email, String elementKey) {
		ElementEntity element = this.elementDao.readById(elementKey)
				.orElseThrow(() -> new RuntimeException("There is no element with the given key"));
		if(element.getUsers().contains(email))
			return element;
		else
			throw new RuntimeException("The user doesn't has access to this project");
	}

	@Override
	public List<ElementEntity> getElementsUsingPagination(String userEmail, int size, int page) { 
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
	}

	@Override
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String name, int size, int page) {
		
		return this.elementDao.readAllUsingName(name, size, page);
		
	}

	private boolean valiadate(ElementEntity entity) {
		return entity != null 
				&& entity.getName() != null && !entity.getName().trim().isEmpty() 
				&& entity.getCreator() != null && !entity.getCreator().trim().isEmpty() 
				&& entity.getNumberOfLines() >=0 
				&& entity.getUsers() != null 
				&& entity.getActiveUsers() != null
				&& entity.getLinesOfCode() != null;
	}

}
