package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;

public class MemoryElementDao implements ElementDao<String> {

	private List<ElementEntity> elements;
	private AtomicLong nextId;
	
	public MemoryElementDao() {
		this.elements = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}
	
	protected List<ElementEntity> getElements (){
		return this.elements;
	}
	
	@Override
	public ElementEntity create(ElementEntity elementEntity) {
		if(elementEntity!=null) {
			elementEntity.setKey(elementEntity.getCreator()+"-"+nextId.getAndIncrement());
			this.elements.add(elementEntity);
		}
		return elementEntity;				
	}

	@Override
	public Optional<ElementEntity> readById(String elementKey) {
		ElementEntity target = null;
		for (ElementEntity current : this.elements) {
			if (current.getKey().equals(elementKey)) {
				target = current;
			}
		}
		if (target != null) {
			return Optional.of(target);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public List<ElementEntity> readAll() {
		return this.elements;
	}

	@Override
	public void update(ElementEntity element) {
		synchronized (this.elements) {
			ElementEntity existing = this.readById(element.getKey())
					.orElseThrow(() -> new RuntimeException("not element to update"));
			if (element.getCreator() != null)
				existing.setCreator(element.getCreator());

			if (element.getName() != null)
				existing.setName(element.getName());

			if (element.getNumberOfLines() >= 0)
				existing.setNumberOfLines(element.getNumberOfLines());

			if (element.getName() != null)
				existing.setName(element.getName());

			if (element.getLastEditTimestamp() != null)
				existing.setLastEditTimestamp(element.getLastEditTimestamp());

			if (element.getUsers() != null)
				existing.setUsers(element.getUsers());
			
			if (element.getActiveUsers() != null)
				existing.setActiveUsers(element.getActiveUsers());
			
			if (element.getLinesOfCode() != null)
				existing.setLinesOfCode(element.getLinesOfCode());
		}
		
	}

	@Override
	public void deleteByKey(String elementKey) {
		if(elements!=null && elementKey!="") {
		Iterator<ElementEntity> itr = this.elements.listIterator();
		ElementEntity element;
		while (itr.hasNext()) {
			element = (ElementEntity) itr.next();
			if(element!=null & element.getKey().equals(elementKey)) {
				itr.remove();
				return;
			}
		}
		}
		
	}

	@Override
	public void delete(ElementEntity elementEntity) {
		deleteByKey(elementEntity.getKey());
		
	}

	@Override
	public void deleteAll() {
		this.elements.clear();	
		
	}

}
