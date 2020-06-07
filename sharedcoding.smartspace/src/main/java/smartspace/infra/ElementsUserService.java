package smartspace.infra;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;
import smartspace.data.ActionType;

public interface ElementsUserService {
	
	public ElementEntity newElement(ElementEntity element);
	
	public void setElement(String elementId,ElementEntity element);
	
	public ElementEntity getSpecificElement(String elementKey);
	
	public List<ElementEntity> getElementsUsingPagination(String userEmail,int size, int page);
	
//	public List<ElementEntity> getElementsUsingPaginationOfLocation(String userSmartspace, String userEmail, ActionType role,
//			int x, int y, int distance, int size, int page);
	
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String name, int size, int page);

//	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(String userSmartspace, String userEmail, ActionType role,
//			String type, int size, int page);
}
