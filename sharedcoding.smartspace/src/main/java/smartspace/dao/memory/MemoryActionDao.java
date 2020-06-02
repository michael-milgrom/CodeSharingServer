package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;

public class MemoryActionDao implements ActionDao {
	
	private List<ActionEntity> actions;
	private AtomicLong nextId;
	
	public MemoryActionDao() {
		this.actions = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}
	
	protected List<ActionEntity> getActions (){
		return this.actions;
	}
	
	@Override
	public ActionEntity create(ActionEntity actionEntity) {
		if(actionEntity!=null) {
			actionEntity.setKey(""+nextId.getAndIncrement());
			this.actions.add(actionEntity);
		}
		return actionEntity;
	}

	@Override
	public List<ActionEntity> readAll() {
		return this.actions;
	}

	@Override
	public void deleteAll() {
		this.actions.clear();
	}
	

}
