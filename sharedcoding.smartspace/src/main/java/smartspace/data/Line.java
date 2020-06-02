package smartspace.data;

import javax.persistence.Embeddable;

@Embeddable
public class Line {
	
	private int number;
	private String code;
	
	
	public Line(int number, String code) {
		super();
		this.number = number;
		this.code = code;
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
	

}