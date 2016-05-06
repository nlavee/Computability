package main.java.project.beans;

import java.util.ArrayList;

public class SubsetSum {

	ArrayList<Double> set = null;
	Double target = null;
	
	public SubsetSum() {
	}

	public SubsetSum(ArrayList<Double> subsetSumItems, Double target) {
		this.set = subsetSumItems;
		this.target = target;
	}

	public ArrayList<Double> getSet() {
		return set;
	}

	public void setSet(ArrayList<Double> set) {
		this.set = set;
	}

	public Double getTarget() {
		return target;
	}

	public void setTarget(Double target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "SubsetSum [set=" + set + ", target=" + target + "]";
	}
	
	
}
