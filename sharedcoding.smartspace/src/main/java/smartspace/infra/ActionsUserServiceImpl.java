package smartspace.infra;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;

@Service
public class ActionsUserServiceImpl implements ActionsUserService {

	private EnhancedActionDao actionDao;
	ObjectMapper jackson;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
	private SequenceDao sequenceDao;

	@Autowired
	public ActionsUserServiceImpl(EnhancedActionDao actionDao, EnhancedUserDao<String> userDao,
			EnhancedElementDao<String> elementDao, SequenceDao sequenceDao) {
		super();
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.sequenceDao = sequenceDao;
	}

	@Override
	@Transactional
	// @CheckRoleOfUser
	public Map<String, Object> invokeAction(String email, ActionEntity action) {
		Optional<UserEntity> user = this.userDao.readById(email);
		if (user.isPresent()) {
			// TODO MAYBE SHOW THAT HE IS EDITING OR SOMETHING
		} else
			throw new RuntimeException("The user doesn't exist");

		String type = action.getActionType();
//		String[] name = action.getUser().split("@");
		switch (type) {
//		case "echo":
//			action.setCreationTimestamp(new Date());
//			try {
//				return convertToMap(actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME)));
//			} catch (Exception e) {
//				new RuntimeException(e);
//			}
//			break;
//		case "to-afeka":
//			action.setCreationTimestamp(new Date());
//			try {
//				return convertToMap(actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME)));
//			} catch (Exception e) {
//				new RuntimeException(e);
//			}
//			break;
//
//		case "from-afeka":
//			action.setCreationTimestamp(new Date());
//			try {
//				return convertToMap(actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME)));
//			} catch (Exception e) {
//				new RuntimeException(e);
//			}
//			break;
//
		case "add-new-user":
			action.setCreationTimestamp(new Date());
			try {
				Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
				if (element.isPresent()) {
//					if (element.get().getUsers() == null)
//						users = new ArrayList<>();
					if (user.get().getEmail().equals(element.get().getCreator())) {
						element.get().getUsers().add("" + action.getProperties().get("newUser")); // adding the new user
																									// to the users list
						user.get().getProjects().add(action.getElementKey()); // add the project to the user's list
						this.userDao.update(user.get());
						this.elementDao.update(element.get());
						this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
						return convertToMap(element.get());
					}
					else
						throw new RuntimeException("the user who've done the action is not the creator");
				}
				else
					throw new RuntimeException("the element isn't exist");
			} catch (Exception e) {
				new RuntimeException(e);
			}
			break;

//		case "check-out":
//			action.setCreationTimestamp(new Date());
//			try {
//				Optional<ElementEntity> element = this.elementDao
//						.readById(action.getElementSmartspace() + "=" + action.getElementId());
//				if (element.isPresent()) {
//					Map<String, Object> drivers = (Map<String, Object>) element.get().getMoreAttributes()
//							.get("drivers");
//					if (drivers != null) {
//						drivers.remove(name[0]);
//						element.get().getMoreAttributes().put("drivers", drivers);
//						this.elementDao.update(element.get());
//					}
//					this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
//					return convertToMap(element.get());
//				}
//				throw new RuntimeException("the element isn't exist");
//			} catch (Exception e) {
//				new RuntimeException(e);
//			}
//			break;
//
//		case "transfer":
//			action.setCreationTimestamp(new Date());
//			try {
//				Optional<UserEntity> from = this.userDao
//						.readById(action.getPlayerSmartspace() + "=" + action.getPlayerEmail());
//				String smart = (String) action.getMoreAttributes().get("smartspace");
//				String em = (String) action.getMoreAttributes().get("email");
//				long points = 100;
//				Optional<UserEntity> to = this.userDao.readById(smart + "=" + em);
//				if (to.isPresent()) {
//					if (from.get().getPoints() >= points) {
//						from.get().setPoints(from.get().getPoints() - points);
//						to.get().setPoints(to.get().getPoints() + points);
//						userDao.update(from.get());
//						userDao.update(to.get());
//						this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
//						return convertToMap(to.get());
//					}
//					throw new RuntimeException("the user doesn't have enough points");
//				}
//				throw new RuntimeException("the user doesn't exist");
//			} catch (Exception e) {
//				new RuntimeException(e);
//			}
//			break;
//
//		case "in-station":
//			action.setCreationTimestamp(new Date());
//			try {
//				Optional<ElementEntity> element = this.elementDao
//						.readById(action.getElementSmartspace() + "=" + action.getElementId());
//				if (element.isPresent()) {
//					Map<String, Object> drivers = (Map<String, Object>) element.get().getMoreAttributes()
//							.get("drivers");
//					this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
//					return drivers;
//				}
//				throw new RuntimeException("the element doesn't exist");
//			} catch (Exception e) {
//				new RuntimeException(e);
//			}
//			break;
//
		default:
			throw new RuntimeException("Action type does not exist!");
		}
		return null;
	}

	public Map<String, Object> convertToMap(ActionEntity action) {
		Map<String, Object> actionMap = new HashMap<String, Object>();
		// Map<String, String> keyMap = new HashMap<String, String>();
		// Map<String, String> elementMap = new HashMap<String, String>();
		// Map<String, String> playerMap = new HashMap<String, String>();

		// keyMap.put("id", action.getActionId());

		// elementMap.put("id", action.getElementKey());

		// playerMap.put("email", action.getUser());

		actionMap.put("user", action.getUser());
		actionMap.put("actionKey", action.getActionId());
		actionMap.put("type", action.getActionType());
		actionMap.put("created", action.getCreationTimestamp());
		actionMap.put("element", action.getElementKey());

		actionMap.put("properties", action.getProperties());

		return actionMap;
	}

	public Map<String, Object> convertToMap(ElementEntity element) {
		Map<String, Object> elementMap = new HashMap<String, Object>();
		// Map<String, String> keyMap = new HashMap<String, String>();

		// keyMap.put("id", element.getElementId());

		elementMap.put("creator", element.getCreator());

		// elementMap.put("key", keyMap);
		elementMap.put("name", element.getName());
		elementMap.put("users", element.getUsers());
		elementMap.put("active_users", element.getActiveUsers());
		elementMap.put("numberOfLines", element.getNumberOfLines());
		elementMap.put("code", element.getLinesOfCode());
		elementMap.put("last_edited", element.getLastEditTimestamp());

		return elementMap;
	}

	public Map<String, Object> convertToMap(UserEntity user) {
		Map<String, Object> userMap = new HashMap<String, Object>();

		userMap.put("email", user.getEmail());
		userMap.put("password", user.getPassword());
		userMap.put("projects", user.getProjects());

		return userMap;
	}
}
