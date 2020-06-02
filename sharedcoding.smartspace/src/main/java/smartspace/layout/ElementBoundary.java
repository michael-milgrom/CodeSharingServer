package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ElementEntity;
import smartspace.data.Line;

public class ElementBoundary {

	private Key key;
	private String elementType;
	private String name;
	private boolean expired;
	private Date created;
	private UserForBoundary creator;
	private LocationForBoundary latlng;
	private Map<String, Object> elementProperties;

	public ElementBoundary() {

	}
	
	
	public ElementBoundary(String elementType, String name, boolean expired, UserForBoundary creator,
			LocationForBoundary latlng, Map<String, Object> elementProperties) {
		super();
		this.elementType = elementType;
		this.name = name;
		this.expired = expired;
		this.creator = creator;
		this.latlng = latlng;
		this.elementProperties = elementProperties;
	}



	public ElementBoundary(ElementEntity entity) {
		if (entity != null) {
			if (entity.getKey() != null) {
				String[] args = entity.getKey().split("=");
				if (args.length == 2) {
					this.key = new Key();
					this.key.setSmartspace(args[0]);
					this.key.setId(args[1]);
				}
			} else
				this.key = null;
			this.elementType = entity.getType();
			this.name = entity.getName();
			this.created = entity.getCreationTimestamp();
			this.expired = entity.isExpired();
			this.creator = new UserForBoundary();
			this.creator.setEmail(entity.getCreatorEmail());
			this.creator.setSmartspace(entity.getCreatorSmartspace());
			if (entity.getLocation() != null) {
				this.latlng = new LocationForBoundary();
				this.latlng.setLat(entity.getLocation().getX());
				this.latlng.setLng(entity.getLocation().getY());
			} else
				this.latlng = null;
			this.elementProperties = entity.getMoreAttributes();
		}
	}

	public ElementEntity convertToEntity() {
		ElementEntity entity = new ElementEntity();
		if (this.key != null && this.key.getId() != null && this.key.getSmartspace() != null) {
			entity.setKey(this.key.getSmartspace() + "=" + this.key.getId());
		}
		entity.setType(this.elementType);
		entity.setName(this.name);
		entity.setExpired(this.expired);
		entity.setCreationTimestamp(this.created);
		if (this.creator != null) {
			entity.setCreatorEmail(this.creator.getEmail());
			entity.setCreatorSmartspace(this.creator.getSmartspace());
		}
		if (this.latlng != null) {
			entity.setLocation(new Line(this.latlng.getLat(), this.latlng.getLng()));
		}

		entity.setMoreAttributes(this.elementProperties);
		return entity;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public UserForBoundary getCreator() {
		return creator;
	}

	public void setCreator(UserForBoundary creator) {
		this.creator = creator;
	}

	public LocationForBoundary getLatlng() {
		return latlng;
	}

	public void setLatlng(LocationForBoundary latlng) {
		this.latlng = latlng;
	}

	public Map<String, Object> getElementProperties() {
		return elementProperties;
	}

	public void setElementProperties(Map<String, Object> elementProperties) {
		this.elementProperties = elementProperties;
	}

	@Override
	public String toString() {
		return "ElementBoundary [key=" + key + ", elementType=" + elementType + ", name=" + name + ", expired="
				+ expired + ", created=" + created + ", creator=" + creator + ", latlng=" + latlng
				+ ", elementProperties=" + elementProperties + "]";
	}

}
