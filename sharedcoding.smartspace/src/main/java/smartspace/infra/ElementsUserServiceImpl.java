package smartspace.infra;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ElementEntity;
import smartspace.data.ActionType;

@Service
public class ElementsUserServiceImpl implements ElementsUserService {

	private EnhancedElementDao<String> elementDao;
	private SequenceDao sequenceDao;

	@Autowired
	public ElementsUserServiceImpl(EnhancedElementDao<String> elementDao, SequenceDao sequenceDao) {
		super();
		this.elementDao = elementDao;
		this.sequenceDao = sequenceDao;
	}

	@Override
	@Transactional
	@CheckRoleOfUser
	public ElementEntity newElement(ElementEntity element) {
		if (valiadate(element)) {
			//TODO do delete//
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
		element.setElementId(elementId);
		this.elementDao.update(element);
	}

	@Override
	//@CheckRoleOfUser
	public ElementEntity getSpecificElement(String elementCreator, String elementName) {
		
		return this.elementDao.readById(elementCreator+"-"+elementName)
				.orElseThrow(() -> new RuntimeException("There is no element with the given key"));
	}

	@Override
	//@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPagination(int size, int page) {
		return this.elementDao.readAll(size, page);
	}

//	@Override
//	//@CheckRoleOfUser
//	public List<ElementEntity> getElementsUsingPaginationOfLocation(String userSmartspace, String userEmail, ActionType role,
//			int x, int y, int distance, int size, int page) {
//
//		if (role == ActionType.MANAGER) {
//			return this.elementDao.readAllUsingLocation(x, y, distance, size, page);
//		} else if (role == ActionType.PLAYER) {
//			return this.elementDao.readAllUsingLocationNotExpired(x, y, distance, size, page);
//		} else {
//			throw new RuntimeException(
//					"The URl isn't match for manager or player. use another user or URL that match admin user");
//		}
//	}

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
				&& entity.getElementId() != null && !entity.getElementId().trim().isEmpty()
				&& entity.getActiveUsers() != null
				&& entity.getLinesOfCode() != null
				&& entity.getLastEditTimestamp() != null;
	}

}
