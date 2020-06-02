package smartspace.infra;

import java.util.List;

import smartspace.data.ElementEntity;


public interface ElementsService {
	
	public List<ElementEntity> newElements(List<ElementEntity> elements, String adminSmartspace, String adminEmail);
	
	public List<ElementEntity> getElementsUsingPagination (String adminSmartspace, String adminEmail, int size, int page);
	
}
