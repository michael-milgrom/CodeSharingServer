package smartspace.infra;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ElementEntity;

@Service
public class ElementServiceImpl implements ElementsService {
	private EnhancedElementDao<String> elementDao;
	private String smartspace;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<String> elementDao) {
		super();
		this.elementDao = elementDao;
	}

	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public List<ElementEntity> newElements(List<ElementEntity> elements) {
		List<ElementEntity> elements_entities = new ArrayList<ElementEntity>();
		
		for (ElementEntity element : elements) {
			if (valiadate(element)) {
					this.elementDao.createWithId(element);
					elements_entities.add(element);
			} else {
				throw new RuntimeException("invalid element");
			}
		}

		return elements_entities;
	}


	private boolean valiadate(ElementEntity entity) {
		return entity != null 
				&& entity.getName() != null && !entity.getName().trim().isEmpty() 
				&& entity.getCreator() != null && !entity.getCreator().trim().isEmpty() 
				&& entity.getNumberOfLines() >=0 
				&& entity.getUsers() != null 
				&& entity.getElementId() != null && !entity.getElementId().trim().isEmpty()
				&& entity.getActiveUsers() != null
				&& entity.getLinesOfCode() != null
				&& entity.getLastEditTimestamp() != null;
	}

	@Override
	public List<ElementEntity> getElementsUsingPagination(int size, int page) {
		return this.elementDao.readAll("creationTimestamp", size, page);
	}
}
