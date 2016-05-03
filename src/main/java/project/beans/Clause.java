package main.java.project.beans;

import java.util.ArrayList;

public class Clause {
	ArrayList<Literal> construct = null;
	long size = 0;
	
	public Clause()
	{
		construct = new ArrayList<Literal>();
	}
	
	public Clause(ArrayList<Literal> data)
	{	
		if(data.size() == 3)
		{
			construct = data;
			size = 3;
		}
	}
	 
	public void addLiteral(Literal l)
	{
		construct.add(l);
		size++;
	}
	
	public Literal getLiteral(int i)
	{
		if( i >= 0 && i < 3)
		{
			return construct.get(i);
		}
		return null;
	}

	public boolean containsLiteral(Literal l)
	{
		for(Literal lit : construct)
		{
			if(l.getId() == lit.getId() && (l.isNegate() && lit.isNegate()))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Clause [construct=" + construct + ", size=" + size + "]\n";
	}
	
	
	
}
