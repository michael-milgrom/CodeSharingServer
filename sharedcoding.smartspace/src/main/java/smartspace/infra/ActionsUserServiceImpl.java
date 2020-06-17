package smartspace.infra;

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
import smartspace.data.Line;
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
		Date now = new Date();
		now.setHours((new Date()).getHours()+3);
		switch (type) {
		case "lock":
			action.setCreationTimestamp(now);
			try {
				Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
				if (element.isPresent()) {
					List<Line> code = element.get().getLinesOfCode();
					int start = (int) action.getProperties().get("start");
					int count = (int) action.getProperties().get("count");
					for (int i = start; i < start + count; i++) {
						Line line = code.get(i);
						if(!line.isLocked()) // if the line is not locked
							line.setLocked(true);
						else
							throw new RuntimeException("the desired lines are already locked");
					}
					actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
					return convertToMap(element.get());
				}
			} catch (Exception e) {
				new RuntimeException(e);
			}
			break;
			
		case "unlock":
			action.setCreationTimestamp(now);
			try {
				Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
				if (element.isPresent()) {
					List<Line> code = element.get().getLinesOfCode();
					int start = (int) action.getProperties().get("start");
					int count = (int) action.getProperties().get("count");
					for (int i = start; i < start + count; i++) {
						Line line = code.get(i);
						line.setLocked(false);
					}
					actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
					return convertToMap(element.get());
				}
			} catch (Exception e) {
				new RuntimeException(e);
			}
			break;

		case "add-new-user":
			action.setCreationTimestamp(now);
			System.out.println(convertToMap(action));
			try {
				Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
				if (element.isPresent()) {
					String newUserEmail = (String) action.getProperties().get("newUser");
					Optional<UserEntity> userToAdd = this.userDao.readById(newUserEmail);
					if (userToAdd.isPresent()) {
						if (user.get().getEmail().equals(element.get().getCreator())) {
							element.get().getUsers().add(newUserEmail); // adding the new user
																		// to the users list
							userToAdd.get().getProjects().add(action.getElementKey()); // add the project to the user's list
							this.userDao.update(userToAdd.get());
							this.elementDao.update(element.get());
							this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
							return convertToMap(element.get());
						} else
							throw new RuntimeException("the user who've done the action is not the creator");
					} else {
						throw new RuntimeException("the user doesn't exists");
					}

				} else
					throw new RuntimeException("the element doesn't exists");
			} catch (Exception e) {
				new RuntimeException(e);
			}
			break;

		case "login":
			action.setCreationTimestamp(now);
			try {
				Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
				if (element.isPresent()) {
					element.get().getActiveUsers().add(user.get().getEmail());
					this.elementDao.update(element.get());
					actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
					return convertToMap(element.get());
				}
			} catch (Exception e) {
				new RuntimeException(e);
			}
			break;

		case "logout":
			action.setCreationTimestamp(now);
			try {
				Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
				if (element.isPresent()) {
					element.get().getActiveUsers().remove(user.get().getEmail());
					this.elementDao.update(element.get());
					actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
					return convertToMap(element.get());
				}
			} catch (Exception e) {
				new RuntimeException(e);
			}
			break;

		case "edit-code":
			action.setCreationTimestamp(now);
			try {
				Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
				if (element.isPresent()) {
					List<Line> code = (List<Line>) action.getProperties().get("code");
					element.get().setLinesOfCode(code);
					element.get().setLastEditTimestamp(now); // edited now
					element.get().setNumberOfLines(code.size());
					this.elementDao.updateLinesOfCode(element.get());
					this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
					return convertToMap(element.get());
				}
				throw new RuntimeException("the element doesn't exist");
			} catch (Exception e) {
				new RuntimeException(e);
			}
			break;

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
