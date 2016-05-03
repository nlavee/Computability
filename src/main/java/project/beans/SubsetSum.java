package main.java.project.beans;

import java.util.ArrayList;

public class SubsetSum {

	ArrayList<Long> set = null;
	Long target = null;
	
	public SubsetSum() {
	}

	public SubsetSum(ArrayList<Long> subsetSumItems, Long target) {
		this.set = subsetSumItems;
		this.target = target;
	}

	public ArrayList<Long> getSet() {
		return set;
	}

	public void setSet(ArrayList<Long> set) {
		this.set = set;
	}

	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "SubsetSum [set=" + set + ", target=" + target + "]";
	}
	
	
}
