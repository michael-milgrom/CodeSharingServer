package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;

public interface EnhancedElementDao<Key> extends ElementDao<Key> {
	public List<ElementEntity> readAll(int size, int page);
	public List<ElementEntity> readAll(String sortBy, int size, int page);
	public List<ElementEntity> readMessageWithCreatorContaining (String creator, int size, int page);
	public ElementEntity createImportAction(ElementEntity entity);
	//public List<ElementEntity> readAllNotExpierd(int size, int page);
	//public List<ElementEntity> readAllUsingLocation(int x, int y, int distance, int size, int page);
	//public List<ElementEntity> readAllUsingLocationNotExpired(int x, int y, int distance, int size, int page);
	public List<ElementEntity> readAllUsingName(String name, int size, int page);
	//public List<ElementEntity> readAllUsingNameNotExpired(String name, int size, int page);
	//public List<ElementEntity> readAllUsingType(String type, int size, int page);
	//public List<ElementEntity> readAllUsingTypeNotExpired(String type, int size, int page);
	//public Optional<ElementEntity> readByIdNotExpired(String elementKey);
	public ElementEntity createWithId(ElementEntity elementEntity);
}