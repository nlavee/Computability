package main.java.project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import main.java.project.beans.Clause;
import main.java.project.beans.ClauseCollection;
import main.java.project.beans.Item;
import main.java.project.beans.Knapsack;
import main.java.project.beans.Literal;

public class InstanceGenerator {

	public InstanceGenerator() {

	}

	public static Knapsack getKnapsack()
	{
		// get new instance of knapsack
		Knapsack knapsack = new Knapsack();
		Random rand = new Random();
		
		// set arbitrary limit of 200 items
		int sizeItemList = rand.nextInt(200);
		// set arbitrary bound of 1000 for value and cost
		int maxItemValue = rand.nextInt(1000);
		int maxItemCost = rand.nextInt(1000);
		
		for(int i = 0 ; i < sizeItemList; i++)
		{
			Item item = new Item();
			// cost and value can be any integer number between 0 and 1000
			double cost = Math.ceil(maxItemCost * rand.nextDouble());
			double value = Math.ceil(maxItemValue * rand.nextDouble());
			item.setCost(cost);
			item.setValue(value);
			
			knapsack.addItem(item);
		}
		
		// target can be any number between 0 and total value
		knapsack.setTarget(Math.ceil(rand.nextDouble() * knapsack.getTotalValue()));
		// budget can be any number between 0 and total cost
		knapsack.setBudget(Math.ceil(rand.nextDouble() * knapsack.getTotalCost()));
		
		return knapsack;
	}
	
	public static ClauseCollection get3SAT()
	{
		ArrayList<Literal> literalList = new ArrayList<Literal>();
		Random rand = new Random();
		//int size = rand.nextInt();
		int size = 10; // number of clauses
		
		// arraylist to hold all clauses
		ArrayList<Clause> clauses = new ArrayList<Clause>();
		
		// go through each clause
		for(int i = 0 ; i < size ; i++)
		{
			// create a new clause
			Clause clause = new Clause();
			
			// have a hashset of literal to ensure all three literals are 
			HashSet<Integer> appearedLiteral = new HashSet<Integer>();
			
			// go through 3 literal
			for(int j = 0 ; j < 3 ; j ++)
			{
				// a (pseudo) random call to randomly decide whether we should reuse literal from before
				boolean reuseLiteral = Math.round(rand.nextDouble()) == 1 ? true : false;

				// check if we're supposed to resuse literal & whether we have any literal to reuse
				if(reuseLiteral && literalList.size() > 0)
				{
					// randomly call for an index from list of literal that we have
					int index = (int) (rand.nextDouble() * literalList.size());
					Literal l = new Literal();
					l.setId(index);
					
					// if we haven't used this literal, we randomly decide whether to 
					// negate it and then add into clause
					if(appearedLiteral.add(index))
					{
						boolean negateLiteral = Math.round(rand.nextDouble()) == 1 ? true : false;
						if(negateLiteral) l.negate();
						clause.addLiteral(l);
					}
					// if we have used this literal, we can just 
					// create a new literal
					else
					{
						// id is the index of this literal in the list of literal used
						l.setId(literalList.size());
						literalList.add(l);
						
						// randomly decide whether we should negate
						boolean negateLiteral = Math.round(rand.nextDouble()) == 1 ? true : false;
						if(negateLiteral) l.negate();
						
						// add new literal into list of literal & into clause
						appearedLiteral.add(l.getId());
						clause.addLiteral(l);
					}
				}
				else
				{
					// we just create new literal
					Literal l = new Literal();
					l.setId(literalList.size());
					literalList.add(l);
					boolean negateLiteral = Math.round(rand.nextDouble()) == 1 ? true : false;
					if(negateLiteral) l.negate();
					appearedLiteral.add(l.getId());
					clause.addLiteral(l);
				}
			}
			clauses.add(clause);
		}
//		System.out.println(literalList.size());
		return new ClauseCollection(clauses, literalList.size());
	}
	
	public static void main(String[] args)
	{
//		System.out.println(get3SAT());
		System.out.println(getKnapsack());
	}

}
