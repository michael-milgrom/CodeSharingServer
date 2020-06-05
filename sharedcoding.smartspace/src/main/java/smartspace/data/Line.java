package smartspace.data;

import javax.persistence.Embeddable;

@Embeddable
public class Line {
	
	private int number;
	private String code;
	private boolean locked;
	
	public Line(int number, String code) {
		super();
		this.number = number;
		this.code = code;
		this.locked = false;
	}


	public Line() {
		super();
	}


	public int getNumber() {
		return this.number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getCode() {
		return this.code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public boolean isLocked() {
		return locked;
	}


	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	
}