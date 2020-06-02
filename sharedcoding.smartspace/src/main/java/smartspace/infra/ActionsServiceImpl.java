package smartspace.infra;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;

@Service
public class ActionsServiceImpl implements ActionsService {
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
	private String smartspace;
	private SequenceDao sequenceDao;
	
	@Autowired	
	public ActionsServiceImpl(EnhancedActionDao actionDao, EnhancedUserDao<String> userDao, EnhancedElementDao<String> elementDao, SequenceDao sequenceDao) {
		super();
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.sequenceDao = sequenceDao;
	}
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public List<ActionEntity> newActions(List<ActionEntity> actions) {
		List<ActionEntity> actions_entities = new ArrayList<ActionEntity>();
		//check local admin
//		if (!(valiadete_admin(adminSmartspace, adminEmail))) {
//			throw new RuntimeException("user are not allowed to create actions");
//		}
		
		
		for(ActionEntity action : actions) {
			if(!valiadate(action))
				throw new RuntimeException("invalid action");
			else {
//				if(this.smartspace.equals(action.getActionSmartspace()))
//					throw new RuntimeException("action smartspace must be different then the local project");
//				else {
					if(!validateElement(action))
						throw new RuntimeException("action element must be imported in advance");	
				//}
			}
			this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
			actions_entities.add(action);
		}
		
		return actions_entities;
	}

	private boolean validateElement(ActionEntity action) {
		return this.elementDao.readById(action.getElementKey()).isPresent();		
	}

//	private boolean valiadete_admin(String adminSmartspace, String adminEmail) {
//		Optional<UserEntity> user = this.userDao.readById(adminSmartspace + "=" + adminEmail);
//		if(user.isPresent() && user.get().getRole().equals(ActionType.ADMIN))
//			return true;
//		return false;
//	}
	
		private boolean valiadate(ActionEntity entity) {
			return entity != null &&
					entity.getActionId()!= null &&
					!entity.getActionId().trim().isEmpty() &&
					entity.getActionType() != null &&
					entity.getElementKey() != null &&
					!entity.getElementKey().trim().isEmpty() &&
					entity.getCreationTimestamp() != null &&
					entity.getUser() != null &&
					!entity.getUser().trim().isEmpty() &&
					entity.getProperties() != null;
		}

	
	
	@Override
	public List<ActionEntity> getActionsUsingPagination(int size,
			int page) {
//		if(!(valiadete_admin(adminSmartspace, adminEmail)))
//			throw new RuntimeException("user are not allowed to get actions");
//		else
			return this.actionDao
					.readAll("creationTimestamp", size, page);
	}


	
}
