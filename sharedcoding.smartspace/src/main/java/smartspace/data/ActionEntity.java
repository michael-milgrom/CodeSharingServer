package smartspace.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Document(collection="ACTIONS")
public class ActionEntity implements SmartspaceEntity<String> {

	private String actionId;
	private String elementKey;
	private String user; // email
	private ActionType actionType;
	private Date creationTimestamp;
	private Map<String ,Object> properties;
	private String key;
	public static final String SEQUENCE_NAME = "actions_sequence";
	
	
	public ActionEntity() {
		this.creationTimestamp = new Date();
		this.properties = new HashMap<>();
	}
	
	public ActionEntity(ActionType type) {
		this();
		this.actionType = type;
		//this.properties = new HashMap<>();
	}
	
	public ActionEntity(String actionId, String elementKey, String user, ActionType actionType, Date creationTimestamp,
			Map<String, Object> properties) {
		super();
		this.actionId = actionId;
		this.elementKey = elementKey;
		this.user = user;
		this.actionType = actionType;
		this.creationTimestamp = creationTimestamp;
		this.properties = new HashMap<>(properties);
	}

	@JsonIgnore
	public String getActionId() {
		return this.actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getElementKey() {
		return elementKey;
	}

	public void setElementKey(String elementKey) {
		this.elementKey = elementKey;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Enumerated(EnumType.STRING)
	public ActionType getActionType() {
		return this.actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return this.creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	@Id
	public String getKey() {
		return this.user + "-" + this.actionId;
	}

	@Override
	public void setKey(String key) {	
		if(key != null || key != "") {
			String[] split = key.split("-");
			if(split!=null & split.length==2) {
				this.user = split[0];
				this.actionId = split[1];	
			}
		}
		
	}

	@Override
	public String toString() {
		return "ActionEntity [actionId=" + actionId + ", elementKey=" + elementKey + ", user=" + user + ", actionType="
				+ actionType + ", creationTimestamp=" + creationTimestamp + ", properties=" + properties + "]";
	}

	@JsonIgnore
	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}
}
