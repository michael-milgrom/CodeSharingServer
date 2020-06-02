package smartspace.data.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Line;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;

@Component
public class EntityFactoryImpl implements EntityFactory {

	public EntityFactoryImpl() {
		super();
	}

	@Override
	public UserEntity createNewUser(String email, String password, List<String> projects) {
		return new UserEntity(email, password, projects);
	}

	@Override
	public ElementEntity createNewElement(String name, String creator, int numberOfLines, 
			Date lastEditTimestamp, List<String> users, List<String> activeUsers, List<Line> linesOfCode) {
		return new ElementEntity(name, creator, numberOfLines, lastEditTimestamp, users, activeUsers, linesOfCode);
	}

	@Override
	public ActionEntity createNewAction(String actionId, String elementKey, String user, 
			ActionType actionType, Date creationTimestamp, Map<String, Object> properties) {
		return new ActionEntity(actionId, elementKey, user, actionType, creationTimestamp, properties);
	}

	@Override
	public String toString() {
		return "EntityFactoryImpl []";
	}
	
}
