package smartspace.infra;

import java.util.Collection;
import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.data.ActionType;

public interface ElementsUserService {
	
	public ElementEntity newElement(String userSmartspace, String userEmail, ActionType role, ElementEntity element);
	
	public void setElement(String userSmartspace, String userEmail, ActionType role, String elementSmartspace, String elementId,
			ElementEntity element);
	
	public ElementEntity getSpecificElement(String userSmartspace, String userEmail, ActionType role, String elementSmartspace,
			String elementId);
	
	public List<ElementEntity> getElementsUsingPagination(String userSmartspace, String userEmail,
			ActionType role, int size, int page);
	
	public List<ElementEntity> getElementsUsingPaginationOfLocation(String userSmartspace, String userEmail, ActionType role,
			int x, int y, int distance, int size, int page);
	
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String userSmartspace, String userEmail, ActionType role,
			String name, int size, int page);

	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(String userSmartspace, String userEmail, ActionType role,
			String type, int size, int page);
}
