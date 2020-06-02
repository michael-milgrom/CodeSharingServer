package smartspace.data.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Line;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;

public interface EntityFactory {
	public UserEntity createNewUser(String email, String password, List<String> projects);
	
	public ElementEntity createNewElement(String name, String creator, int numberOfLines, 
			Date lastEditTimestamp, List<String> users, List<String> activeUsers, List<Line> linesOfCode);
	
	public ActionEntity createNewAction(String actionId, String elementKey, String user, 
			ActionType actionType, Date creationTimestamp, Map<String, Object> properties);

}
