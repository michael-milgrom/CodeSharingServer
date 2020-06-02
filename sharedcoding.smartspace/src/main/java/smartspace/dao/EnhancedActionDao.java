package smartspace.dao;

import java.util.List;

import smartspace.data.ActionEntity;

public interface EnhancedActionDao extends ActionDao{
	public List<ActionEntity> readAll(int size, int page);
	public List<ActionEntity> readAll(String sortBy, int size, int page);
	public List<ActionEntity> readMessageWithElementKeyContaining (String smartspace, int size, int page);
	public ActionEntity createImportAction(ActionEntity entity);
	public ActionEntity createWithId(ActionEntity actionEntity, Long id);
}


