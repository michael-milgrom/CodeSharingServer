package smartspace.infra;

import java.util.Collection;
import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementsUserService {
	
	public ElementEntity newElement(ElementEntity element, String creator);
	
	public void setElement(String email, String elementId,ElementEntity element);
	
	public ElementEntity getSpecificElement(String email, String elementKey);
	
	public List<ElementEntity> getElementsUsingPagination(String userEmail,int size, int page);
	
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String name, int size, int page);
	
	public void setElementCode(String email, String elementId,ElementEntity element);
}
