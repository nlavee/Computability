package main.java.project.beans;

import java.util.ArrayList;
import java.util.Iterator;

public class ClauseCollection {
	ArrayList<Clause> clauses = null;
	Integer literalCount = null;
	
	public ClauseCollection()
	{
		
	}
	
	public ClauseCollection(ArrayList<Clause> clauseCollection, int countLiteral)
	{
		clauses = clauseCollection;
		literalCount = countLiteral;
	}

	public ArrayList<Clause> getClauses() {
		return clauses;
	}

	public void setClauses(ArrayList<Clause> clauses) {
		this.clauses = clauses;
	}

	public Integer getLiteralCount() {
		return literalCount;
	}

	public void setLiteralCount(Integer literalCount) {
		this.literalCount = literalCount;
	}

	@Override
	public String toString() {
		return "ClauseCollection \n[clauses=\n" + clauses + "\nliteralCount="
				+ literalCount + "]";
	}
	
}

