package main.java.project.beans;

public class Item {
	double value;
	double cost;
	
	public Item() {
	}

	public Item(double value, double cost) {
		this.value = value;
		this.cost = cost;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Item [value=" + value + ", cost=" + cost + "]";
	}
	
	
}
