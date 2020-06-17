package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.ActionType;

public class ActionBoundary {
	private Key actionKey;
	private String type;
	private Date created;
	private Key element;
	private String user;
	private Map<String, Object> properties;
	
	public ActionBoundary() {
		this.actionKey = new Key();
		this.element = new Key();
	}


	public ActionBoundary(String type, Key element, String user, Map<String, Object> properties) {
		super();
		this.type = type;
		this.element = element;
		this.user = user;
		this.properties = properties;
	}

	public ActionBoundary(ActionEntity entity){
		if(entity!=null) {
			if(entity.getActionId() != null && entity.getUser() != null)
				this.actionKey = new Key(entity.getUser(), entity.getActionId());
			else
				this.actionKey = null;
		
		this.type = entity.getActionType();
		this.created = entity.getCreationTimestamp();
			
		this.element = new Key();//??
		this.element.setId(entity.getActionId());
		this.element.setUser(entity.getUser());
		
		this.user= entity.getUser();
		
		this.properties = entity.getProperties();
		}
	}
	
	public ActionEntity convertToEntity() {
		ActionEntity entity = new ActionEntity();
		
		if(this.actionKey!= null)
			entity.setKey(actionKey.getUser() + "-" + actionKey.getId());
		
		entity.setActionType(this.type);
		entity.setCreationTimestamp(this.created);
		
		if(this.element!=null) {
			entity.setElementKey(this.element.getUser() + "-" + this.element.getId());
		}
		
		if(this.user!=null) {
			entity.setUser(this.user);
		}
		
		entity.setProperties(this.properties);
		return entity;
	}

	public Key getActionKey() {
		return actionKey;
	}

	public void setActionKey(Key actionKey) {
		this.actionKey = actionKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Key getElement() {
		return element;
	}

	public void setElement(Key element) {
		this.element = element;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "ActionBoundary [actionKey=" + actionKey + ", type=" + type + ", created=" + created + ", element="
				+ element + ", user=" + user + ", properties=" + properties + "]";
	}

}
