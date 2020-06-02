package smartspace.infra;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;

@Service
public class ElementServiceImpl implements ElementsService {
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	private String smartspace;
	private SequenceDao sequenceDao;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<String> elementDao, EnhancedUserDao<String> userDao, SequenceDao sequenceDao) {
		super();
		this.elementDao = elementDao;
		this.userDao = userDao;
		this.sequenceDao = sequenceDao;
	}

	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public List<ElementEntity> newElements(List<ElementEntity> elements, String adminSmartspace, String adminEmail) {
		List<ElementEntity> elements_entities = new ArrayList<ElementEntity>();
		if (!valiadete_admin(adminSmartspace, adminEmail)) {
			throw new RuntimeException("user are not allowed to create elements");
		}

		for (ElementEntity element : elements) {
			if (valiadate(element)) {
				if (!(this.smartspace.equals(element.getElementSmartspace()))) {

					this.elementDao.createWithId(element, sequenceDao.newEntity(ElementEntity.getSequenceName()));
					elements_entities.add(element);
				} else
					throw new RuntimeException("element smartspace must be different then the local project");
			} else {
				throw new RuntimeException("invalid element");
			}
		}

		return elements_entities;
	}

	private boolean valiadete_admin(String adminSmartspace, String adminEmail) {
		Optional<UserEntity> user = this.userDao.readById(adminSmartspace + "=" + adminEmail);
		if (user.isPresent() && user.get().getRole().equals(ActionType.ADMIN))
			return true;
		return false;
	}

	private boolean valiadate(ElementEntity entity) {
		return entity != null 
				&& entity.getCreatorSmartspace() != null && !entity.getCreatorSmartspace().trim().isEmpty() 
				&& entity.getCreatorEmail() != null && !entity.getCreatorEmail().trim().isEmpty() 
				&& entity.getName() != null && !entity.getName().trim().isEmpty()
				&& entity.getType() != null && !entity.getType().trim().isEmpty()
				&& entity.getElementId() != null && !entity.getElementId().trim().isEmpty()
				&& entity.getElementSmartspace() != null && !entity.getElementSmartspace().trim().isEmpty()
				&& entity.getLocation() != null
				&& entity.getMoreAttributes() != null;
	}

	@Override
	public List<ElementEntity> getElementsUsingPagination(String adminSmartspace, String adminEmail, int size,
			int page) {
		if (!(valiadete_admin(adminSmartspace, adminEmail)))
			throw new RuntimeException("user are not allowed to get elements");
		else
			return this.elementDao.readAll("creationTimestamp", size, page);// key?????
	}
}
