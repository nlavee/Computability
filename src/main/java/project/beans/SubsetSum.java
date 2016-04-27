package main.java.project.beans;

import java.util.ArrayList;

public class SubsetSum {

	ArrayList<Integer> set = null;
	Long target = null;
	
	public SubsetSum() {
	}

	public SubsetSum(ArrayList<Integer> set, Long target) {
		this.set = set;
		this.target = target;
	}

	public ArrayList<Integer> getSet() {
		return set;
	}

	public void setSet(ArrayList<Integer> set) {
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
