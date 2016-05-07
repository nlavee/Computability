package main.java.project.beans;

import java.util.ArrayList;

public class Knapsack {
	ArrayList<Item> itemList;
	double budget;
	double target;
	double sum = 0;
	double cost = 0;
	
	public Knapsack() {
		itemList = new ArrayList<Item>();
		sum = 0;
	}

	public Knapsack(ArrayList<Item> itemList, double budget, double target) {
		this.itemList = itemList;
		this.budget = budget;
		this.target = target;
		for(Item item : itemList)
		{
			sum += item.value;
			cost += item.cost;
		}
	}

	public ArrayList<Item> getItemList() {
		sum = 0;
		return itemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
		for(Item i : itemList)
		{
			sum += i.getValue();
			cost += i.getCost();
		}
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public double getTarget() {
		return target;
	}

	public void setTarget(double target) {
		this.target = target;
	}
	
	public void addItem(Item item)
	{
		itemList.add(item);
		sum += item.getValue();
		cost += item.getCost();
	}
	
	public Item getItem(int index)
	{
		Item item = new Item();
		if(index < itemList.size())
		{
			item = itemList.get(index);
		}
		return item;
	}
	
	public double getTotalValue()
	{
		return sum;
	}
	
	public double getTotalCost()
	{
		return cost;
	}
	
	public int getNumItem()
	{
		return itemList.size();
	}

	@Override
	public String toString() {
		return "Knapsack [ budget=" + budget
				+ ", target=" + target + ", sum=" + sum + "\nItemList=" + itemList.toString() + "]";
	}

	
	
	
}
