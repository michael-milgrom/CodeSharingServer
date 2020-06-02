package smartspace.infra;

import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.ActionType;

public interface ActionsUserService {
	
	public Map<String, Object> invokeAction(String email, ActionEntity action);
}
