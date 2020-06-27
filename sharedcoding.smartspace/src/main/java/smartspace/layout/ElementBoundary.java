package smartspace.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import smartspace.data.ActiveUser;
import smartspace.data.ElementEntity;
import smartspace.data.Line;

public class ElementBoundary {

	private Key key;
	private String name;
	private String creator;
	private int numberOfLines;
	private Date lastEditTimestamp;
	private List<String> users;
	private List<ActiveUser> activeUsers;
	private List<LineForBoundary> linesOfCode;
	
	
	public ElementBoundary() {

	}
	
	


	public ElementBoundary(String name, String creator, int numberOfLines, Date lastEditTimestamp,
			List<String> users, List<ActiveUser> activeUsers, List<LineForBoundary> linesOfCode) {
		super();
		this.name = name;
		this.creator = creator;
		this.numberOfLines = numberOfLines;
		this.lastEditTimestamp = lastEditTimestamp;
		this.users = users;
		this.activeUsers = activeUsers;
		this.linesOfCode = new LinkedList<>(linesOfCode);
	}



	public ElementBoundary(ElementEntity entity) {
		if (entity != null) {
			if (entity.getKey() != null) {
				String[] args = entity.getKey().split("-");
				if (args.length == 2) {
					this.key = new Key();
					this.key.setUser(args[0]);
					this.key.setId(args[1]);
				}
			} else
				this.key = null;
			
			this.name = entity.getName();
			this.numberOfLines = entity.getNumberOfLines();
			this.lastEditTimestamp = entity.getLastEditTimestamp();
			this.creator = entity.getCreator();
			this.users = new ArrayList<>(entity.getUsers());
			this.activeUsers = new ArrayList<>(entity.getActiveUsers());
			this.linesOfCode = new LinkedList<>();
			if(entity.getLinesOfCode() != null) {
				for(Line line : entity.getLinesOfCode())
					this.linesOfCode.add(new LineForBoundary(line.getNumber(), line.getCode(), line.isLocked()));
			}
		}
	}

	public ElementEntity convertToEntity() {
		ElementEntity entity = new ElementEntity();
		if (this.key != null && this.key.getId() != null) {
			entity.setKey(this.key.getUser() + "-" + this.key.getId());
		}
		entity.setActiveUsers(this.activeUsers);
		entity.setUsers(this.users);
		entity.setName(this.name);
		entity.setNumberOfLines(this.numberOfLines);
		entity.setLastEditTimestamp(this.lastEditTimestamp);
		
		List<Line> lines = new LinkedList<>();
		
		if(this.linesOfCode != null) {
			for(LineForBoundary line : this.linesOfCode)
				lines.add(new Line(line.getNumber(), line.getCode(), line.isLocked()));
		}
		
		entity.setLinesOfCode(lines);
		entity.setCreator(this.creator);
		
		return entity;
	}




	public Key getKey() {
		return key;
	}




	public void setKey(Key key) {
		this.key = key;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getCreator() {
		return creator;
	}




	public void setCreator(String creator) {
		this.creator = creator;
	}




	public int getNumberOfLines() {
		return numberOfLines;
	}




	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}




	public Date getLastEditTimestamp() {
		return lastEditTimestamp;
	}




	public void setLastEditTimestamp(Date lastEditTimestamp) {
		this.lastEditTimestamp = lastEditTimestamp;
	}




	public List<String> getUsers() {
		return users;
	}




	public void setUsers(List<String> users) {
		this.users = users;
	}




	public List<ActiveUser> getActiveUsers() {
		return activeUsers;
	}




	public void setActiveUsers(List<ActiveUser> activeUsers) {
		this.activeUsers = activeUsers;
	}




	public List<LineForBoundary> getLinesOfCode() {
		return linesOfCode;
	}




	public void setLinesOfCode(List<LineForBoundary> linesOfCode) {
		this.linesOfCode = linesOfCode;
	}




	@Override
	public String toString() {
		return "ElementBoundary [key=" + key + ", name=" + name + ", creator=" + creator + ", numberOfLines="
				+ numberOfLines + ", lastEditTimestamp=" + lastEditTimestamp + ", users=" + users + ", activeUsers="
				+ activeUsers + ", linesOfCode=" + linesOfCode + "]";
	}

	

}
