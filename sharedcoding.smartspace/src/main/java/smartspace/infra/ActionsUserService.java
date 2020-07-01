package smartspace.infra;

import java.util.Map;

import smartspace.data.ActionEntity;

public interface ActionsUserService {
	
	public Map<String, Object> invokeAction(String email, ActionEntity action);
}
