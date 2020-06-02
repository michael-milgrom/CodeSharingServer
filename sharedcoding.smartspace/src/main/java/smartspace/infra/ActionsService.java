package smartspace.infra;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionsService {
	
	public List<ActionEntity> newActions(List<ActionEntity> actions);
	
	public List<ActionEntity> getActionsUsingPagination (int size, int page);
	
}
