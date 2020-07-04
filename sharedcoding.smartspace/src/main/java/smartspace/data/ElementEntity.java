package smartspace.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Document(collection="ELEMENTS")
public class ElementEntity implements SmartspaceEntity<String> {

	private String elementId;
	private String name;
	private String creator;
	private int numberOfLines;
	private Date lastEditTimestamp;
	private List<String> users;
	private List<ActiveUser> activeUsers;
	private List<Line> linesOfCode;
	private String key;
	private static final String SEQUENCE_NAME = "elements_sequence";

	public ElementEntity() {
		this.numberOfLines = 0;
		Date now = new Date();
		now.setHours((new Date()).getHours()+3);
		this.lastEditTimestamp = now;
		this.users = new ArrayList<String>();
		this.activeUsers = new ArrayList<ActiveUser>();
		this.linesOfCode = new LinkedList<Line>();
	}
	
	public ElementEntity(String name) {
		this();
		this.name = name;	
	}
	
	public ElementEntity(String name, String creator, int numberOfLines, Date lastEditTimestamp, 
			List<String> users, List<ActiveUser> activeUsers, List<Line> linesOfCode) {
		super();
		this.name = name;
		this.creator = creator;
		this.numberOfLines = numberOfLines;
		this.lastEditTimestamp = lastEditTimestamp;
		this.users = new ArrayList<String>(users);
		this.activeUsers = new ArrayList<ActiveUser>(activeUsers);
		this.linesOfCode = new LinkedList<Line>(linesOfCode);
	}

	@JsonIgnore
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	@JsonIgnore
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public int getNumberOfLines() {
		return this.numberOfLines;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastEditTimestamp() {
		return this.lastEditTimestamp;
	}

	public void setLastEditTimestamp(Date lastEditTimestamp) {
		this.lastEditTimestamp = lastEditTimestamp;
	}
	

	public List<String> getUsers() {
		return this.users;
	}

	public void setUsers(List<String> users) {
		this.users = new ArrayList<String>(users);
	}

	public List<ActiveUser> getActiveUsers() {
		return this.activeUsers;
	}

	public void setActiveUsers(List<ActiveUser> activeUsers) {
		this.activeUsers = new ArrayList<ActiveUser>(activeUsers);
	}

	public List<Line> getLinesOfCode() {
		return this.linesOfCode;
	}

	public void setLinesOfCode(List<Line> linesOfCode) {
		this.linesOfCode = new LinkedList<Line>(linesOfCode);
	}

	@Override
	@Id
	public String getKey() {
		return this.creator + "-" +  this.name;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
		String[] split = key.split("-");
		if(split!=null & split.length==2) {
			this.creator = split[0];
			this.name = split[1];	
		}
	}
	
	@Override
	public String toString() {
		return "ElementEntity [elementId=" + elementId + ", name=" + name + ", creator=" + creator + ", numberOfLines="
				+ numberOfLines + ", lastEditTimestamp=" + lastEditTimestamp + ", users=" + users + ", activeUsers="
				+ activeUsers + ", linesOfCode=" + linesOfCode + "]";
	}

	@JsonIgnore
	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}

}