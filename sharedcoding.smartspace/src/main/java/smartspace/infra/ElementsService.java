package smartspace.infra;

import java.util.List;

import smartspace.data.ElementEntity;


public interface ElementsService {
	
	public List<ElementEntity> newElements(List<ElementEntity> elements);
	
	public List<ElementEntity> getElementsUsingPagination (int size, int page);
	
}
