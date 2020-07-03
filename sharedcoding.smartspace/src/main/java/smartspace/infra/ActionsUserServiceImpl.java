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

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Line;
import smartspace.data.UserEntity;
import smartspace.data.ActiveUser;

@Service
public class ActionsUserServiceImpl implements ActionsUserService {

	private EnhancedActionDao actionDao;
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
	public Map<String, Object> invokeAction(String email, ActionEntity action) {
		String type = action.getActionType();
		if (type.equals("connect"))
			return null;
		else {
			Optional<UserEntity> user = this.userDao.readById(email);
			if (user.isPresent()) {
				// TODO MAYBE SHOW THAT HE IS EDITING OR SOMETHING
			} else
				throw new RuntimeException("The user doesn't exist");

			Date now = new Date();
			now.setHours((new Date()).getHours() + 3);
			switch (type) {
			case "lock":
				action.setCreationTimestamp(now);
				try {
					System.out.println(convertToMap(action));
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						List<Line> code = element.get().getLinesOfCode();
						int start = (int) action.getProperties().get("start");
						int count = (int) action.getProperties().get("count");
						for (ActiveUser au : element.get().getActiveUsers()) // change the start
							if (au.getEmail().equals(user.get().getEmail())) {
								au.setEditing(true);
								au.setStart(start);
								au.setBeforeEditLength(count);
							}

						for (int i = start; i < start + count; i++) {
							Line line = code.get(i);
							if (!line.isLocked()) // if the line is not locked
								line.setLocked(true);
							else
								throw new RuntimeException("the desired lines are already locked");
						}
						elementDao.updateLinesOfCode(element.get());
						actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
						return convertToMap(element.get());
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			case "unlock":
				action.setCreationTimestamp(now);
				try {
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						List<Line> code = element.get().getLinesOfCode();
						int start = 0;
						int length = (int) action.getProperties().get("length");
						for (ActiveUser au : element.get().getActiveUsers())
							if (au.getEmail().equals(user.get().getEmail())) {
								start = au.getStart();
								au.setEditing(false);
								au.setStart(-1);
								au.setBeforeEditLength(0);
							}

						for (int i = start; i < start + length; i++) {
							Line line = code.get(i);
							line.setLocked(false);
						}
						elementDao.updateLinesOfCode(element.get());
						actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
						return convertToMap(element.get());
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

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
								userToAdd.get().getProjects().add(action.getElementKey()); // add the project to the
																							// user's
																							// list
								this.userDao.update(userToAdd.get());
								this.elementDao.update(element.get());
								this.actionDao.createWithId(action,
										sequenceDao.newEntity(ActionEntity.getSequenceName()));
								return convertToMap(element.get());
							} else
								throw new RuntimeException("the user who've done the action is not the creator");
						} else {
							throw new RuntimeException("the user doesn't exists");
						}

					} else
						throw new RuntimeException("the element doesn't exists");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			case "login":
				action.setCreationTimestamp(now);
				try {
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						element.get().getActiveUsers().add(new ActiveUser(user.get().getEmail(), false, -1, 1));
						this.elementDao.update(element.get());
						actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
						return convertToMap(element.get());
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			case "logout":
				action.setCreationTimestamp(now);
				try {
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						element.get().getActiveUsers().removeIf(au -> au.getEmail().equals(user.get().getEmail()));
						this.elementDao.update(element.get());
						actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
						return convertToMap(element.get());
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			case "delete":
				action.setCreationTimestamp(now);
				try {
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						element.get().getUsers().remove(user.get().getEmail());
						element.get().getActiveUsers().removeIf(au -> au.getEmail().equals(user.get().getEmail()));
						user.get().getProjects().remove(element.get().getKey());
						if (element.get().getUsers().isEmpty())
							this.elementDao.deleteByKey(element.get().getKey());
						else
							this.elementDao.update(element.get());
						this.userDao.update(user.get());
						this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
						return convertToMap(element.get());
					} else
						throw new RuntimeException("the element isn't exist");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			case "edit-code-save":
				action.setCreationTimestamp(now);
				try {
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						List<Line> linesOfCode = element.get().getLinesOfCode();
						element.get().setLastEditTimestamp(now); // edited now
						int start = 0;
						int beforeLength = 1;
						for (ActiveUser au : element.get().getActiveUsers())
							if (au.getEmail().equals(user.get().getEmail())) {
								start = au.getStart();
								//beforeLength = au.getBeforeEditLength();
							}

						String text = (String) action.getProperties().get("code");
						String[] stringArr = { "" };
						int length = 0;
						if (text != null && !text.equals("")) {
							stringArr = text.split("\n");
							length = stringArr.length;
							System.out.println("length = " + length);
						}

						/*
						 * remove the out dated lines
						 */
						for (int i = 0; i < beforeLength; i++) {
							System.out.println("*** " + linesOfCode.get(start));
							linesOfCode.remove(start);
						}

						/*
						 * add all new lines (and the locked lines) to the list
						 */
						for (int i = start, j = 0; i < length + start; i++, j++) {
							Line tempLine = new Line(i, stringArr[j]);
							linesOfCode.add(i, tempLine);
						}
						
						linesOfCode.get(start+beforeLength-1).setLocked(true);;

						element.get().setLinesOfCode(linesOfCode);
						element.get().setNumberOfLines(linesOfCode.size());
						for (ActiveUser activeUser : element.get().getActiveUsers()) {
							if (!activeUser.getEmail().equals(user.get().getEmail()) && activeUser.isEditing())
								activeUser.setStart(activeUser.getStart() + length - (beforeLength));
						}
						this.elementDao.updateLinesOfCode(element.get());
						this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));
						return convertToMap(element.get());
					}
					throw new RuntimeException("the element doesn't exist");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			case "edit-code-event":
				action.setCreationTimestamp(now);
				try {
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						List<Line> linesOfCode = element.get().getLinesOfCode();
						element.get().setLastEditTimestamp(now); // edited now
						String event = (String) action.getProperties().get("event");
						String before = (String) action.getProperties().get("before-change");
						int start = 0;
						int beforeLength = 1;
						boolean locked = false;
						for (ActiveUser au : element.get().getActiveUsers())
							if (au.getEmail().equals(user.get().getEmail())) {
								start = au.getStart();
								//beforeLength = au.getBeforeEditLength();
							}
						int destination = start;
						int originalStart = start;
						switch (event) {
						case "BACK_SPACE":
							if (element.get().getLinesOfCode().get(start - 1).isLocked())
								locked = true;
							else {
								start--; // go to the line above
								beforeLength++;
							}
							destination = start; // if line above locked, stay in the same line. if not, go to line
													// above
							break;

						case "DELETE":
							if (element.get().getLinesOfCode().get(start + 1).isLocked())
								locked = true;
							else
								beforeLength++;
							destination = start; // stay in the same line
							break;

						case "LEFT":
						case "UP":
							if (element.get().getLinesOfCode().get(start - 1).isLocked())
								locked = true;
							else
								destination = start - 1; // if line above locked, stay in the same line. if not, go to
															// line above
							break;

						case "RIGHT":
						case "DOWN":
							if (element.get().getLinesOfCode().get(start + 1).isLocked())
								locked = true;
							else
								destination = start + 1; // if line below locked, stay in the same line. if not, go to
															// line below
							break;

						case "ENTER":
							destination = start + 1; 
							break;

						}

						System.out.println("start = " + start);
						System.out.println("destination = " + destination);

						String text = (String) action.getProperties().get("code");
						String[] stringArr = { "" };
						int length = 0;
						if (text != null && !text.equals("")) {
							stringArr = text.split("\n");
							length = stringArr.length;
							System.out.println("length = " + length);
						}

						/*
						 * remove the out dated lines
						 */
						for (int i = 0; i < beforeLength; i++) {
							System.out.println("*** " + linesOfCode.get(start));
							linesOfCode.remove(start);
						}

						/*
						 * add all new lines (and the locked lines) to the list
						 */
						for (int i = start, j = 0; i < length + start; i++, j++) {
							Line tempLine;
							if(locked)
								tempLine = new Line(i, before);
							else
								tempLine = new Line(i, stringArr[j]);
							linesOfCode.add(i, tempLine);
						}

						if (!locked) // if he did move the cursor
							linesOfCode.get(originalStart).setLocked(false); // Unlock previous locked lines by me
						linesOfCode.get(destination).setLocked(true); // No matter where the destination is, lock it,
																		// even if the line is occupied (it's already
																		// locked by another user so it doesn't matter)

						element.get().setLinesOfCode(linesOfCode);
						element.get().setNumberOfLines(linesOfCode.size());
						for (ActiveUser activeUser : element.get().getActiveUsers()) {
							if (!activeUser.getEmail().equals(user.get().getEmail())) {
								if (activeUser.isEditing())
									if(activeUser.getStart() >= originalStart)
										activeUser.setStart(activeUser.getStart() + length - (beforeLength));
							} else { // THE EDITING USER
								if (!locked) {
									activeUser.setEditing(true);
									activeUser.setStart(destination);
									activeUser.setBeforeEditLength(beforeLength);
								} else { // pop up message of locked line and return the cursor to the original position
									activeUser.setEditing(true);
									activeUser.setStart(originalStart);
									activeUser.setBeforeEditLength(1);
								}
							}
						}

						this.elementDao.updateLinesOfCode(element.get());
						this.actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.getSequenceName()));

						Map<String, Object> result = convertToMap(element.get());
						if (locked) {
							result.put("error", "The line that you're trying to reach is locked by another user.");
						} else
							result.put("error", "");

						return result;
					}
					throw new RuntimeException("the element doesn't exist");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			case "logs":
				try {
					Optional<ElementEntity> element = this.elementDao.readById(action.getElementKey());
					if (element.isPresent()) {
						List<ActionEntity> actions = this.actionDao.readActionsWithElementKey(action.getElementKey(),
								10, 0);
						Map<String, Object> result = new HashMap<>();
						for (ActionEntity ae : actions)
							result.put(ae.getActionId(), ae);
						return result;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			default:
				throw new RuntimeException("Action type does not exist!");
			}
		}
	}

	public Map<String, Object> convertToMap(ActionEntity action) {
		Map<String, Object> actionMap = new HashMap<String, Object>();

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

		elementMap.put("name", element.getName());
		elementMap.put("creator", element.getCreator());
		elementMap.put("numberOfLines", element.getNumberOfLines());
		elementMap.put("users", element.getUsers());
		elementMap.put("activeUsers", element.getActiveUsers());
		elementMap.put("linesOfCode", element.getLinesOfCode());

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
