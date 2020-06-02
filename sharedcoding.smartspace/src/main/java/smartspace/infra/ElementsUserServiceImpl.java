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
	public ElementEntity newElement(String userSmartspace, String userEmail, ActionType role,ElementEntity element) {
		if (role == ActionType.MANAGER) {
			if (valiadate(element)) {
				//do delete//
				element.setCreationTimestamp(new Date());
				this.elementDao.createWithId(element, this.sequenceDao.newEntity(ElementEntity.getSequenceName()));
			} else
				throw new RuntimeException("invalid element");
		}

		else if(role == ActionType.PLAYER){
			throw new RuntimeException(
					"Player can't post an element");
		}
		else {
			throw new RuntimeException(
					"The URl isn't match for manager. use manager user or URL that match the admin user");
		}

		return element;
	}

	@Override
	@Transactional
	@CheckRoleOfUser
	public void setElement(String userSmartspace, String userEmail, ActionType role, String elementSmartspace, String elementId,
			ElementEntity element) // TODO what to do with the smartspace?
			 {
		if (role == ActionType.MANAGER) {
			element.setElementId(elementId);
			element.setElementSmartspace(elementSmartspace);
			this.elementDao.update(element);
		}

		else if(role == ActionType.PLAYER){
			throw new RuntimeException(
					"Player can't Update an element");
		}
		else {
			throw new RuntimeException(
					"The URl isn't match for manager. use manager user or URL that match the admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public ElementEntity getSpecificElement(String userSmartspace, String userEmail, ActionType role, String elementSmartspace,
			String elementId) {

		if (role == ActionType.MANAGER) {
			return this.elementDao.readById(elementSmartspace+"="+elementId)
					.orElseThrow(() -> new RuntimeException("There is no element with the given key"));
		} else if (role == ActionType.PLAYER) {
			return this.elementDao.readByIdNotExpired(elementSmartspace+"="+elementId)
					.orElseThrow(() -> new RuntimeException("There is no element with the given key or the element is expired"));
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPagination(String userSmartspace, String userEmail,
			ActionType role, int size, int page) {

		if (role == ActionType.MANAGER) {
			return this.elementDao.readAll(size, page);
		} else if (role == ActionType.PLAYER) {
			return this.elementDao.readAllNotExpierd(size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPaginationOfLocation(String userSmartspace, String userEmail, ActionType role,
			int x, int y, int distance, int size, int page) {

		if (role == ActionType.MANAGER) {
			return this.elementDao.readAllUsingLocation(x, y, distance, size, page);
		} else if (role == ActionType.PLAYER) {
			return this.elementDao.readAllUsingLocationNotExpired(x, y, distance, size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String userSmartspace, String userEmail, ActionType role,
			String name, int size, int page) {
		
		if (role == ActionType.MANAGER) {
			return this.elementDao.readAllUsingName(name, size, page);
		} else if (role == ActionType.PLAYER) {
			return this.elementDao.readAllUsingNameNotExpired(name, size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(String userSmartspace, String userEmail, ActionType role,
			String type, int size, int page) {

		if (role == ActionType.MANAGER) {
			return this.elementDao.readAllUsingType(type, size, page);
		} else if (role == ActionType.PLAYER) {
			return this.elementDao.readAllUsingTypeNotExpired(type, size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	private boolean valiadate(ElementEntity entity) {
		return entity != null && entity.getCreatorSmartspace() != null
				&& !entity.getCreatorSmartspace().trim().isEmpty() && entity.getCreatorEmail() != null
				&& !entity.getCreatorEmail().trim().isEmpty() && entity.getName() != null
				&& !entity.getName().trim().isEmpty() && entity.getType() != null && !entity.getType().trim().isEmpty()
				&& entity.getElementId() != null && !entity.getElementId().trim().isEmpty()
				&& entity.getElementSmartspace() != null && !entity.getElementSmartspace().trim().isEmpty()
				&& entity.getLocation() != null && entity.getMoreAttributes() != null;
	}

}
