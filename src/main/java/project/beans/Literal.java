package main.java.project.beans;

public class Literal {
	Boolean value;
	Boolean isNegate = false;
	Integer id = null;
	
	public Literal(boolean value) {
		super();
		this.value = value;
	}
	
	public Literal() {
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void negate() {
		isNegate = !isNegate;
	}
	
	public boolean logicalAnd( Literal b ) {
		return Boolean.logicalAnd(value, b.getValue());
	}
	
	public boolean isNegate()
	{
		return isNegate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Literal [value=" + value + ", isNegate=" + isNegate + ", id=" + id + "]";
	}
	
	
}
