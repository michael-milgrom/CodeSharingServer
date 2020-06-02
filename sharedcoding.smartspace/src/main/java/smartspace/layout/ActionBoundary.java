package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;

public class ActionBoundary {
	private Key actionKey;
	private String type;
	private Date created;
	private Key element;
	private UserForBoundary player;
	private Map<String, Object> properties;
	
	public ActionBoundary() {
		this.actionKey = new Key();
		this.element = new Key();
		this.player=new UserForBoundary();
	}


	public ActionBoundary(String type, Key element, UserForBoundary player, Map<String, Object> properties) {
		super();
		this.type = type;
		this.element = element;
		this.player = player;
		this.properties = properties;
	}

	public ActionBoundary(ActionEntity entity){
		if(entity!=null) {
			if(entity.getActionId() != null && entity.getActionSmartspace() != null)
				this.actionKey = new Key(entity.getActionId(), entity.getActionSmartspace());
			else
				this.actionKey = null;
		
		this.type = entity.getActionType();
		this.created = entity.getCreationTimestamp();
			
		this.element = new Key();//??
		this.element.setId(entity.getElementId());
		this.element.setSmartspace(entity.getElementSmartspace());
		
		this.player= new UserForBoundary();//??
		this.player.setEmail(entity.getPlayerEmail());
		this.player.setSmartspace(entity.getPlayerSmartspace());
		
		this.properties = entity.getMoreAttributes();
		}
	}
	
	public ActionEntity convertToEntity() {
		ActionEntity entity = new ActionEntity();
		
		if(this.actionKey!= null)
			entity.setKey(actionKey.getSmartspace() + "=" + actionKey.getId());
		
		entity.setActionType(this.type);
		entity.setCreationTimestamp(this.created);
		
		if(this.element!=null) {
		entity.setElementSmartspace(this.element.getSmartspace());
		entity.setElementId(this.element.getId());
		}
		
		if(this.player!=null) {
			entity.setPlayerEmail(this.player.getEmail());
			entity.setPlayerSmartspace(this.player.getSmartspace());
		}
		
		entity.setMoreAttributes(this.properties);
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

	public UserForBoundary getPlayer() {
		return player;
	}

	public void setPlayer(UserForBoundary player) {
		this.player = player;
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
				+ element + ", player=" + player + ", properties=" + properties + "]";
	}


	public void convertToMap() {
		// TODO Auto-generated method stub
		
	}
	

}
