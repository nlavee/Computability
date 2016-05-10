package main.java.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import main.java.project.beans.Clause;
import main.java.project.beans.ClauseCollection;
import main.java.project.beans.Item;
import main.java.project.beans.Knapsack;
import main.java.project.beans.Literal;
import main.java.project.beans.SubsetSum;

public class Implementation {

	public Implementation() {}

	/**
	 * Method that reduce 3SAT to 1in3SAT.
	 * @param ThreeSATInstance
	 * @return
	 */
	public static ClauseCollection reduce3SATTo1In3SAT(ClauseCollection ThreeSATInstance)
	{
		ArrayList<Clause> res = new ArrayList<Clause>();
		ArrayList<Clause> ThreeSATInstanceArray = ThreeSATInstance.getClauses();
		int literalCount = ThreeSATInstance.getLiteralCount();

		for(Clause clause : ThreeSATInstanceArray)
		{
			Literal firstLiteral = clause.getLiteral(0);
			Literal secondLiteral = clause.getLiteral(1);
			Literal thirdLiteral = clause.getLiteral(2);

			Clause newClause = new Clause();
			Literal newFirstLiteral = new Literal();
			newFirstLiteral.setId(firstLiteral.getId());
			if(!firstLiteral.isNegate())newFirstLiteral.negate();
			Literal a = new Literal();
			a.setId(literalCount++);
			Literal b = new Literal();
			b.setId(literalCount++);
			newClause.addLiteral(newFirstLiteral);
			newClause.addLiteral(a);
			newClause.addLiteral(b);
			res.add(newClause);

			newClause = new Clause();
			Literal c = new Literal();
			c.setId(literalCount++);
			newClause.addLiteral(b);
			newClause.addLiteral(secondLiteral);
			newClause.addLiteral(c);
			res.add(newClause);

			newClause = new Clause();
			Literal d = new Literal();
			d.setId(literalCount++);
			Literal newThirdLiteral = new Literal();
			newThirdLiteral.setId(thirdLiteral.getId());
			if(!thirdLiteral.isNegate()) newThirdLiteral.negate();
			newClause.addLiteral(c);
			newClause.addLiteral(newThirdLiteral);
			newClause.addLiteral(d);
			res.add(newClause);
		}
		return new ClauseCollection(res, literalCount);
	}

	/**
	 * Method that reduce 1 in 3 sat into an instance of SubsetSum.
	 * @param OneInThreeSAT
	 * @return
	 */
	public static SubsetSum reduce1In3SATToSubsetSum(ClauseCollection OneInThreeSAT)
	{
		int literalCount = OneInThreeSAT.getLiteralCount();
		ArrayList<Clause> clauses = OneInThreeSAT.getClauses();
		ArrayList<Double> subsetSumItems = new ArrayList<>();

		int nPlusM = literalCount + clauses.size();

		for(int i = 0 ; i < literalCount; i++)
		{
			// create 2 elements, set to be 0
			int[] v = new int[nPlusM];
			int[] vPrime = new int[nPlusM];

			// set ith digit of both to be one
			v[i] = 1;
			vPrime[i] = 1;

			for(int j = 0 ; j < clauses.size(); j ++)
			{
				Clause clause = clauses.get(j);
				Literal toCheck = new Literal(i);
				if(clause.containsLiteral(toCheck))
				{
					v[literalCount + j] = 1;
				}
				else
				{
					toCheck.negate();
					if(clause.containsLiteral(toCheck))
					{
						vPrime[literalCount + j] = 1;
					}
				}	
			}
			StringBuilder st = new StringBuilder();
			for(int a = 0 ; a < v.length; a ++)
			{
				if(a >= literalCount) st.append(v[a]);
			}
			//System.out.println(st.toString());
			Double vDouble = Double.parseDouble(st.toString());

			st = new StringBuilder();
			for(int a = 0 ; a < vPrime.length; a ++)
			{
				if(a >= literalCount) st.append(vPrime[a]);
			}
			//System.out.println(st.toString());
			Double vPrimeDouble = Double.parseDouble(st.toString());

			subsetSumItems.add(vDouble);
			subsetSumItems.add(vPrimeDouble);

		}

		StringBuilder st = new StringBuilder();
		for(int i = 0 ; i < clauses.size(); i++) 
		{
			st.append("1");
		}
		//		System.out.println(st.toString()+ "\nTarget ^ ");
		//		System.out.println();
		Double T = Double.parseDouble(st.toString());
		return new SubsetSum(subsetSumItems, T);
	}

	/**
	 * Method to reduce an instance of subset sum into knapsack
	 * @param ss
	 * @return
	 */
	public static Knapsack reduceSubsetSumToKnapsack(SubsetSum ss)
	{
		Knapsack knapsack = new Knapsack();

		knapsack.setBudget(ss.getTarget());
		knapsack.setTarget(ss.getTarget());

		ArrayList<Double> itemSet = ss.getSet();
		for(Double l : itemSet)
		{
			Item i = new Item();
			i.setCost(l);
			i.setValue(l);
			knapsack.addItem(i);
		}

		return knapsack;
	}

	/**
	 * Dynamic Programming for knapsack algorithm, only return the values
	 * @param knapsack
	 * @return
	 */
	public static double dynamicProgrammingKnapsack(Knapsack knapsack, boolean restriction)
	{
		ArrayList<Item> itemTaken = new ArrayList<>();
		return dynamicProgrammingKnapsack(knapsack, itemTaken, restriction);
	}

	/**
	 * dynamic programming for knapsack algorithm, pass in arraylist of item
	 * to get out item taken
	 * @param knapsack
	 * @return
	 */
	public static double dynamicProgrammingKnapsack(Knapsack knapsack, ArrayList<Item> itemTaken, boolean restriction)
	{
		int numItem = knapsack.getNumItem();
		int budget = (int) knapsack.getBudget();

		double[][] table = new double[numItem+1][budget+1];

		boolean chosen = false;
		for(int i = numItem-1; i >= 0; i--)
		{
			for(int b = 1 ; b <= budget; b++)
			{
				// if we just chosen and this is v', we don't choose anything
				if(chosen && i%2 == 1)
				{
					table[i][b] = table[i+1][b];
				}
				else
				{
					Item item = knapsack.getItem(i);

					if(item.getCost() > b)
					{
						table[i][b] = table[i+1][b];
					}
					else
					{
						table[i][b] = Math.max(
								table[i+1][b-(int) item.getCost()] + item.getValue(), 
								table[i+1][b]);

						// if we doing 3SAT and we have just chosen an item
						if(table[i][b] == table[i+1][b-(int) item.getCost()] + item.getValue() && restriction)
						{
							if(item.getCost() != 0.0 && item.getValue() != 0.0)
							{
								// mark that have just chosen this
								chosen = true;
							}
						}
					}
				}
			}
			// finish the whole row, meaning that we're done with v', we can choose the next item
			chosen = false;
		}

		// print the table for double check
		//		for(int i = 0 ; i < table.length; i++)
		//		{
		//			for(int j = 0 ; j < table[i].length; j++)
		//			{
		//				System.out.print( table[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
		getItemList(table, itemTaken, knapsack);

		return table[0][budget];
	}

	/**
	 * Method to get back item chosen for dynamic programming knapsack O(nW)
	 * @param table
	 * @param itemTaken
	 * @param ks
	 */
	private static void getItemList(double[][] table, ArrayList<Item> itemTaken, Knapsack ks) {
		int i = 0;
		int k = table[0].length-1;

		while(i < table.length-1 && k >=0)
		{
			if(table[i][k] != table[i+1][k])
			{
				i++;
				k -= ks.getItem(i-1).getCost();
				//System.out.println(ks.getItem(i-1));
				itemTaken.add(ks.getItem(i-1));
				if((i-1) % 2 == 1) i++;
			}
			else
			{
				i++;
			}
		}
	}

	/**
	 * Method for calculating Knapsack only return the value
	 * @param knapsack
	 * @return
	 */
	public static double dynamicProgrammingKnapsackMinCost(Knapsack knapsack, boolean restriction)
	{
		ArrayList<Item> itemTaken = new ArrayList<>();
		return dynamicProgrammingKnapsackMinCost(knapsack, itemTaken, restriction);
	}

	/**
	 * Method for calculating Knapsack, does keep track of item that are chosen that can retrieved
	 * @param knapsack
	 * @param itemTaken
	 * @return
	 */
	public static double dynamicProgrammingKnapsackMinCost(Knapsack knapsack, ArrayList<Item> itemTaken, boolean restriction)
	{
		int numItem = knapsack.getNumItem();
		double budget = knapsack.getBudget();

		ArrayList<Item> list = knapsack.getItemList();
		Collections.sort(list, new Implementation(). new ItemComparatorCost());
		//System.out.println(list);
		knapsack.setItemList(list);

		// calculate aMax
		double aMax = calculateAMax(knapsack);

		// making sure that we get to an value where even all item combined, we cannot get that value
		double[][] minCost = new double[numItem][(int) (numItem * aMax + 1)];

		for(int i = 0 ; i < minCost.length; i++)
		{
			for(int j = 1 ; j < minCost[i].length; j++)
			{
				minCost[i][j] = Integer.MAX_VALUE;
			}
		}

		// making sure that we get to an value where even all item combined, we cannot get that value
		boolean[][] take = new boolean[numItem][(int) (numItem * aMax + 1)];

		solveMaximumKnapsack(knapsack, minCost, take, aMax, numItem, budget, restriction);

		int optimalValue = constructMaxKnapsackSolution(knapsack, minCost, take, aMax, numItem, budget, itemTaken, restriction);

		//		for(int i = 0 ; i < minCost.length; i++)
		//		{
		//			for(int j = 0 ; j < minCost[i].length; j++)
		//			{
		//				System.out.print(minCost[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
		return optimalValue;
	}

	/**
	 * Method that actually solve Knapsack for the MinCost approach
	 * @param knapsack
	 * @param minCost
	 * @param take
	 * @param aMax
	 * @param numItem
	 * @param budget
	 */
	private static void solveMaximumKnapsack(Knapsack knapsack,
			double[][] minCost, boolean[][] take, double aMax, int numItem, double budget, boolean restriction) {

		// when target is 0, there's no cost
		//		for(int i = 0; i < numItem; i++)
		//		{
		//			minCost[i][0] = 0;
		//		}

		// when t <= v(1), target t can be achieved by taking object 1
		for(int t = 1; t <= knapsack.getItem(0).getValue(); t++)
		{
			// changed from 1 to 0, due to us being in zero-based
			minCost[0][t] = knapsack.getItem(0).getCost();
			take[0][t] = true;
		}

		// when t > v(1), target cannot be reached with only object 1 available
		for(int t = (int) knapsack.getItem(0).getValue() + 1; t <= numItem*aMax; t++)
		{
			minCost[0][t] = Integer.MAX_VALUE;
			take[0][t] = false;
		}

		int i = 1; // if normal, go from the next item
		if(restriction) i++; // if going from 3SAT, skip the 2nd item since it's the same literal

		// start from wherever i is
		for(;i < numItem; i++)
		{
			for(int t = 1; t <= numItem*aMax; t++)
			{
				// if we're going from 3SAT and this is an odd number (v), we'll have to skip the next item, which is v'
				boolean skipThisItem = false;

				// if we the previous item is odd, and we have taken it
				if((i-1) % 2 == 1 && restriction && take[i-1][t])
				{
					skipThisItem = true;
				}


				if(skipThisItem)
				{
					minCost[i][t] = minCost[i-1][t];
					take[i][t] = false;
				}
				else
				{
					int nextT = (int) Math.max(0, t - knapsack.getItem(i).getValue());
					if(minCost[i-1][t] <= knapsack.getItem(i).getCost() + minCost[i-1][nextT]) // don't include i
					{
						minCost[i][t] = minCost[i-1][t];
						take[i][t] = false;
					}
					else // include i
					{
						minCost[i][t] = (knapsack.getItem(i).getCost() + minCost[i-1][nextT]);
						take[i][t] = true;
					}
				}
			}
		}
		//		for(int i = 0 ; i < take.length; i ++)
		//		{
		//			for(int j = 0 ; j < take[i].length; j++)
		//			{
		//				System.out.print(take[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
	}

	/**
	 * Method that construct Knapsack solution for the MinCost approach, also keep track of 
	 * items that are chosen.
	 * @param knapsack
	 * @param minCost
	 * @param take
	 * @param aMax
	 * @param numItem
	 * @param budget
	 * @param itemTaken
	 * @return
	 */
	private static int constructMaxKnapsackSolution(Knapsack knapsack, double[][] minCost, boolean[][] take, 
			double aMax, int numItem, double budget, ArrayList<Item> itemTaken, boolean restriction) {
		int optimalValue = (int) (numItem * aMax);

		//		System.out.println(minCost[numItem-1][optimalValue]);
		while(optimalValue > 0 && minCost[numItem-1][optimalValue] > budget)
		{
			optimalValue--;
		}

		// intialize solution to be empty

		int i = numItem-1;
		int t = optimalValue;
		//		System.out.println(knapsack.getItemList().size());
		while(i > 0 && t > 0)
		{
			//			System.out.println("i: " + i + " - t: " + t);
			if(take[i][t])
			{
				itemTaken.add(knapsack.getItem(i));
				t = t - (int) knapsack.getItem(i).getValue();
			}
			i = i - 1;
		}

		int res = 0;

		for(Item item : itemTaken)
		{
			res += item.getValue();
		}
		return res;
	}

	/**
	 * Method to solve Knapsack based on Greedy 2-Approx Approach. Does not return item taken
	 * @param knapsack
	 * @return
	 */
	public static double greedyKnapsack(Knapsack knapsack, boolean restriction)
	{
		ArrayList<Item> itemTake = new ArrayList<>();
		return greedyKnapsack(knapsack, itemTake, restriction);
	}

	/**
	 * Greedy algorithm for knapsack, keep track of item taken. This implementation will 
	 * have a higher running time for when we do reduction due to several passes we make through hashmap set of item
	 * to find the original index.
	 * @param knapsack
	 * @return
	 */
	public static double greedyKnapsack(Knapsack knapsack, ArrayList<Item> itemTake, boolean restriction)
	{
		HashMap<Item, Integer> itemMapping = new HashMap<Item, Integer>();

		// save the original position of index for each item into a Hashmap for faster query
		if(restriction)
		{
			for(int i = 0 ; i < knapsack.getItemList().size(); i++)
			{
				itemMapping.put(knapsack.getItem(i), i);
			}
		}

		// sort based on custom comparator that does based on value/cost
		Collections.sort(knapsack.getItemList(), new Implementation(). new CustomComparator());

		//		for(Item item : knapsack.getItemList())
		//		{
		//			System.out.println(item.getValue()/item.getCost());
		//		}

		double L = knapsack.getBudget();

		//System.out.println(knapsack.getItemList());
		HashSet<Item> indexToAvoid = new HashSet<>();
		for(int i = 0; i < knapsack.getItemList().size(); i++)
		{
			// if this item is not blacklisted
			if(!indexToAvoid.contains(knapsack.getItem(i)))
			{
				Item item = knapsack.getItem(i);
				//			System.out.println(item.getValue() / item.getCost());
				if(L > 0)
				{
					if(item.getCost() <= L )
					{
						if(item.getCost() == 0.0 && item.getValue() == 0.0)
						{

						}
						else
						{	
							if(restriction)
							{
								// get the original index
								Integer index = itemMapping.get(item);

								// check to see whether we're dealing with v
								if(index %2 == 1)
								{
									// go through the list of item, whoever has the next index should not be accounted for anymore
									for(Item itemInHashMap : itemMapping.keySet())
									{
										if(itemMapping.get(itemInHashMap) == index+1)
										{
											// add into HashSet to avoid v'
											indexToAvoid.add(itemInHashMap);
										}
									}
								}
							}
							itemTake.add(item);
							L -= item.getCost();
						}
					}
				}
			}
		}

		double aMax = 0;
		for(Item item : knapsack.getItemList())
		{
			// if cost > budget, we cannot take it as well
			if(item.getValue() > aMax && item.getCost() <= knapsack.getBudget()) aMax = item.getValue();
		}

		int currSum = 0;
		for(Item item : itemTake)
		{
			currSum += item.getValue();
		}

		return aMax > currSum? aMax : currSum;
	}

	/**
	 * Method for fully polynomial time approximation scheme, does not return item taken 
	 * @param knapsack
	 * @param scaleFactor
	 * @return
	 */
	public static double knapsackApproxScheme(Knapsack knapsack, double scaleFactor, boolean restriction)
	{
		ArrayList<Item> itemTaken = new ArrayList<Item>();
		return knapsackApproxScheme(knapsack, scaleFactor, itemTaken, restriction);
	}

	/**
	 * Method for fully polynomial time approximation scheme, keep track of item taken
	 * @param knapsack
	 * @param scaleFactor
	 * @param itemTaken
	 * @return
	 */
	public static double knapsackApproxScheme(Knapsack knapsack, double scaleFactor, ArrayList<Item> itemTaken, boolean restriction)
	{
		ArrayList<Item> itemList = knapsack.getItemList();
		ArrayList<Item> scaled = new ArrayList<Item>();

		int numItem = knapsack.getNumItem();
		double budget = knapsack.getBudget();

		for(Item item : itemList)
		{
			Item newItem = new Item();
			newItem.setValue(Math.floor(item.getValue() / scaleFactor));
			newItem.setCost(item.getCost());
			scaled.add(newItem);
		}

		Knapsack newInstance = new Knapsack();
		newInstance.setItemList(scaled);
		newInstance.setBudget(knapsack.getBudget());
		newInstance.setTarget(knapsack.getTarget());

		// Start solving use scaled item
		double aMax = calculateAMax(newInstance);

		// making sure that we get to an value where even all item combined, we cannot get that value
		double[][] minCost = new double[numItem][(int)(numItem * aMax) + 1];

		for(int i = 0 ; i < minCost.length; i++)
		{
			for(int j = 1 ; j < minCost[i].length; j++)
			{
				minCost[i][j] = Integer.MAX_VALUE;
			}
		}

		// making sure that we get to an value where even all item combined, we cannot get that value
		boolean[][] take = new boolean[numItem][(int)(numItem * aMax) + 1];

		solveMaximumKnapsack(newInstance, minCost, take, aMax, numItem, budget, restriction);

		//		aMax = calculateAMax(knapsack);
		double optimalValue = constructMaxKnapsackSolution(newInstance, minCost, take, aMax, numItem, budget, itemTaken, restriction);
		for(int i = 0 ; i < itemTaken.size(); i++)
		{
			itemTaken.get(i).setValue(itemTaken.get(i).getValue() * scaleFactor);
		}
		return (optimalValue * scaleFactor);
	}

	/**
	 * Method to get the value of item with the maximum value in a knapsack instance
	 * @param knapsack
	 * @return
	 */
	private static int calculateAMax(Knapsack knapsack)
	{
		int aMax = 0;
		for(Item item : knapsack.getItemList())
		{
			if(item.getValue() > aMax) aMax = (int) item.getValue();
		}
		return aMax;
	}

	/**
	 * The method that bridges the gap between decision problem and optimazation problem for Knapsack. This is 
	 * specifically written for the process of reduction experiment
	 * If the result from the Knapsack algorithms is larger or equal to the target in the decision version,
	 * then we have can return true
	 * Also, due to the way that subsetsum reduction to knapsack (in order to work with higher number of clauses in 
	 * 3SAT), we check specifically for two consecutive item in knapsack (which represents v and v') and see
	 * whether they both are in the results. If they are, we cannot have the results due to the fact that 
	 * we would be taking both true and false for a literal in 3SAT.
	 * @param knapsack
	 * @param algorithm
	 * @return
	 */
	private static boolean decide01Knapsack(Knapsack knapsack, int algorithm)
	{
		if(algorithm == 1) System.out.println("\n============DP1===============");
		else if(algorithm == 2) System.out.println("\n============DP2===============");
		else if(algorithm == 3) System.out.println("\n============GRE===============");
		else if(algorithm == 4) System.out.println("\n============FPT===============");

		ArrayList<Item> itemTaken = new ArrayList<Item>();

		// copy the item into a new list to keep since some algorithms changes the order of 
		// item in the knapsack
		ArrayList<Item> knapsackItemList = new ArrayList<>();
		for(int i = 0 ; i < knapsack.getItemList().size(); i++)
		{
			knapsackItemList.add(knapsack.getItemList().get(i));
		}

		double returnedFromMaxProblem = -1;
		if(algorithm == 1) returnedFromMaxProblem = dynamicProgrammingKnapsack(knapsack, itemTaken, true);
		else if(algorithm == 2) returnedFromMaxProblem = dynamicProgrammingKnapsackMinCost(knapsack, itemTaken, true);
		else if(algorithm == 3) returnedFromMaxProblem = greedyKnapsack(knapsack, itemTaken, true);
		else if(algorithm == 4) returnedFromMaxProblem = knapsackApproxScheme(knapsack, 2, itemTaken, true);
		else throw new IllegalStateException("The algorithm doesn't exist. Only have 1 - 4."); // could have use enum for this

		// does this hit the target?
		boolean getTarget = false;
		if(returnedFromMaxProblem >= (int) knapsack.getTarget()) getTarget = true;

		System.out.println("Returned: " + returnedFromMaxProblem);
		System.out.println("Taken size: " + knapsack.getItemList().size());
		System.out.println("Item List:\n" + knapsackItemList);
		System.out.println("Get Target: " + ((Boolean) getTarget).toString().toUpperCase());

		return getTarget;
	}

	/**
	 * Custom comparator. This is for descending sort.
	 * @author AnhVuNguyen
	 *
	 */
	public class CustomComparator implements Comparator<Item> {
		@Override
		public int compare(Item o1, Item o2) {
			return -1*(new Double(o1.getValue()/o1.getCost())).compareTo(new Double(o2.getValue()/o2.getCost()));
		}
	}

	/**
	 * Comparator to sort items based on cost descendingly
	 * @author AnhVuNguyen
	 *
	 */
	public class ItemComparatorCost implements Comparator<Item> {
		@Override
		public int compare(Item o1, Item o2) {
			return -1*(new Double(o1.getCost())).compareTo(new Double(o2.getCost()));
		}
	}

	public static void main(String[] args)
	{
		// NEED THIS TO GENERATE LOG AS RAW DATA
		Logger LOGGER = Logger.getLogger(Implementation.class);

		// set mode
		boolean testKnapsackValidity = false; // testing knapsack algorithms with some test case
		boolean testing3SAT = false; // performing experiment on 3SAT
		boolean testingKnapsack = true; // performing experiment on Knapsack 
		boolean runningSimulation = true; // enable python scripts to automaticaly parse log file
		boolean vuMachine = true; // if it's Vu's machine, it'll launch RSturio to do the analysis

		/*
		 * Testing whether Knapsack is returning what I'm expecting using three different test cases
		 */
		if(testKnapsackValidity)
		{
			/**
			 * Test knapsack #1
			 * Value 102
			 */

			Knapsack ks1 = new Knapsack();
			ks1.setBudget(100);
			Item water = new Item();
			water.setValue(75);
			water.setCost(50);
			Item temp = new Item();
			temp.setValue(75);
			temp.setCost(50);
			Item air = new Item();
			air.setValue(102);
			air.setCost(51);
			ArrayList<Item> itemList = new ArrayList<>();
			itemList.add(air);
			itemList.add(temp);
			itemList.add(water);
			ks1.setItemList(itemList);
			System.out.println(dynamicProgrammingKnapsack(ks1, false));
			System.out.println(dynamicProgrammingKnapsackMinCost(ks1, false));
			System.out.println(greedyKnapsack(ks1, false));
			System.out.println(knapsackApproxScheme(ks1, 2, false));
			System.out.println();

			/**
			 * Test knapsack #2
			 * Value 29
			 */

			Knapsack ks2 = new Knapsack();
			ks2.setBudget(8);
			Item one = new Item(15, 1);
			Item two = new Item(10, 5);
			Item three = new Item(9, 3);
			Item four = new Item(5, 4);
			ArrayList<Item> itemList2 = new ArrayList<>();
			itemList2.add(one);
			itemList2.add(two);
			itemList2.add(three);
			itemList2.add(four);
			ks2.setItemList(itemList2);
			System.out.println(dynamicProgrammingKnapsack(ks2, false));
			System.out.println(dynamicProgrammingKnapsackMinCost(ks2, false));
			System.out.println(greedyKnapsack(ks2, false));
			System.out.println(knapsackApproxScheme(ks2, 2, false));
			System.out.println();


			/**
			 * Test knapsack #3
			 * Value 7
			 */

			Knapsack ks3 = new Knapsack();
			ks3.setBudget(5);
			Item s1 = new Item(3, 2);
			Item s2 = new Item(4, 3);
			Item s3 = new Item(5, 4);
			Item s4 = new Item(6, 5);
			ArrayList<Item> itemList3 = new ArrayList<>();
			itemList3.add(s1);
			itemList3.add(s2);
			itemList3.add(s3);
			itemList3.add(s4);
			ks3.setItemList(itemList3);
			System.out.println(dynamicProgrammingKnapsack(ks3, false));
			System.out.println(dynamicProgrammingKnapsackMinCost(ks3, false));
			System.out.println(greedyKnapsack(ks3, false));
			System.out.println(knapsackApproxScheme(ks3, 2, false));
			System.out.println();

			/**
			 * Testing knapsack #4
			 * Item from 3SAT
			 * 
			 */
			Knapsack ks4 = new Knapsack();
			ks4.setBudget(11);
			Item ss1 = new Item(10.0, 10.0);
			Item ss2 = new Item(00.0, 00.0);
			Item ss3 = new Item(00.0, 00.0);
			Item ss4 = new Item(11.0, 11.0);
			Item ss5 = new Item(10.0, 10.0);
			Item ss6 = new Item(01.0, 01.0);
			Item ss7 = new Item(00.0, 00.0);
			Item ss8 = new Item(01.0, 01.0);
			ArrayList<Item> itemList4 = new ArrayList<>();
			itemList4.add(ss1);
			itemList4.add(ss2);
			itemList4.add(ss3);
			itemList4.add(ss4);
			itemList4.add(ss5);
			itemList4.add(ss6);
			itemList4.add(ss7);
			itemList4.add(ss8);
			ks4.setItemList(itemList4);

			//			System.out.println(ks4);

			ArrayList<Item> DP1 = new ArrayList<>();
			System.out.println(dynamicProgrammingKnapsack(ks4, DP1, true));
			System.out.println(DP1);

			ArrayList<Item> DP2 = new ArrayList<>();
			System.out.println(dynamicProgrammingKnapsackMinCost(ks4, DP2, true));
			System.out.println(DP2);

			ArrayList<Item> DP3 = new ArrayList<>();
			System.out.println(greedyKnapsack(ks4, DP3, true));
			System.out.println(DP3);

			ArrayList<Item> DP4 = new ArrayList<>();
			System.out.println(knapsackApproxScheme(ks4, 2, DP4, true));
			System.out.println(DP4);

		}


		if(testing3SAT)
		{
			for(int i = 0 ; i < 200; i++)
			{
				LOGGER.info("Count: " + i);
				ClauseCollection ThreeSAT = InstanceGenerator.get3SAT();
				//System.out.println(ThreeSAT);
				//LOGGER.info(ThreeSAT);
				//System.out.println();
				//System.out.println(reduce3SATTo1In3SAT(ThreeSAT));
				//System.out.println();
				//System.out.println(reduce1In3SATToSubsetSum(reduce3SATTo1In3SAT(ThreeSAT))); // TODO: too big, tweaking with clauses size right now
				SubsetSum ss = reduce1In3SATToSubsetSum(reduce3SATTo1In3SAT(ThreeSAT));
				Knapsack ks = reduceSubsetSumToKnapsack(ss);
				Knapsack ks1 = reduceSubsetSumToKnapsack(ss);
				Knapsack ks2 = reduceSubsetSumToKnapsack(ss);
				Knapsack ks3 = reduceSubsetSumToKnapsack(ss);
				///System.out.println();
				//System.out.println(ks);
				//System.out.println();
				//System.out.println("DP1: " + dynamicProgrammingKnapsack(ks));
				long start = System.nanoTime();
				boolean solveOne = decide01Knapsack(ks, 1);
				long end = System.nanoTime();
				LOGGER.info("1: " + (end - start)/1000000.0 + " ms");
				//			System.out.println("DP1 Decide: " + solveOne);
				//			System.out.println();
				//LOGGER.info("DP1 Decide: " + solveOne);
				//System.out.println("DP2: " + dynamicProgrammingKnapsackMinCost(ks));
				start = System.nanoTime();
				boolean solveTwo = decide01Knapsack(ks1, 2);
				end = System.nanoTime();
				LOGGER.info("2: " + (end - start)/1000000.0 + " ms");
				//			System.out.println("DP2 Decide: " + solveTwo);
				//			System.out.println();
				//LOGGER.info("DP2 Decide: " + solveTwo);
				//System.out.println("Greedy: " + greedyKnapsack(ks));
				start = System.nanoTime();
				boolean solveThree = decide01Knapsack(ks2, 3);
				end = System.nanoTime();
				LOGGER.info("3: " + (end - start)/1000000.0 + " ms");
				//			System.out.println("Greedy Decide: " + solveThree);
				//			System.out.println();
				//LOGGER.info("Greedy Decide: " + solveThree);
				//System.out.println("FPTAS: " + knapsackApproxScheme(ks, 2));
				start = System.nanoTime();
				boolean solveFour = decide01Knapsack(ks3, 4);
				end = System.nanoTime();
				LOGGER.info("4: " + (end - start)/1000000.0 + " ms");
				//			System.out.println("FPTAS Decide: " + solveFour);
				//			System.out.println();
				//LOGGER.info("FPTAS Decide: " + solveFour);

				if(!solveOne && !solveTwo && !solveThree && !solveFour)
				{
					System.err.println("FAIL FOR: \n" + ThreeSAT);
					//LOGGER.info("FAIL FOR: \n" + ThreeSAT);
					// break;
				}
			}
		}

		if(testingKnapsack)
		{
			for(int i = 0; i < 100; i++)
			{
				LOGGER.info("Count: " + (i+1));
				Knapsack knapsack = InstanceGenerator.getKnapsack();
				//			System.out.println(knapsack);
				//LOGGER.info(knapsack);
				try{
					long startTime = System.nanoTime();
					double res = dynamicProgrammingKnapsack(knapsack, false);
					//				System.out.println("O(nW): \t\t\t\t\t" + res);
					LOGGER.info("O(nW): " + res);

					long endTime = System.nanoTime();
					//				System.out.println("Running time: " + (endTime - startTime)/1000000 + " ms");
					LOGGER.info("Running time: " + (endTime - startTime)/1000000.0 + " ms");
					//				System.out.println();
				}
				catch( Throwable t)
				{
					//				System.out.println("Something went wrong with the DP");
					//LOGGER.info("Something went wrong with the DP", t);
					LOGGER.info("O(nW): NA");
					LOGGER.info("Running time: NA");
					t.printStackTrace();
				}

				try{
					long startTime = System.nanoTime();
					double res = dynamicProgrammingKnapsackMinCost(knapsack, false);
					//				System.out.println("O(n^2 * v(aMax)): \t\t\t" + res);
					LOGGER.info("O(n^2 * v(aMax)): " + res);

					long endTime = System.nanoTime();
					//				System.out.println("Running time: " + (endTime - startTime)/1000000 + " ms");
					LOGGER.info("Running time: " + (endTime - startTime)/1000000.0 + " ms");
					//				System.out.println();
				}
				catch( Throwable t)
				{
					//				System.out.println("Something went wrong with the DP Min Cost");
					//LOGGER.info("Something went wrong with the DP Min Cost", t);
					LOGGER.info("O(n^2 * v(aMax)): NA");
					LOGGER.info("Running time: NA");
					t.printStackTrace();
				}

				try{
					long startTime = System.nanoTime();
					double res = greedyKnapsack(knapsack, false);
					//				System.out.println("Greedy: \t\t\t\t" + res);
					LOGGER.info("Greedy: " + res);
					long endTime = System.nanoTime();
					//				System.out.println("Running time: " + (endTime - startTime)/1000000 + " ms");
					LOGGER.info("Running time: " + (endTime - startTime)/1000000.0 + " ms");
					//				System.out.println();
				}
				catch( Throwable t)
				{
					//				System.out.println("Something went wrong with the Greedy Knapsack");
					//LOGGER.info("Something went wrong with the Greedy Knapsack", t);
					LOGGER.info("Greedy: NA");
					LOGGER.info("Running time: NA");
					t.printStackTrace();
				}

				try{
					double aMax = calculateAMax(knapsack);
					int itemNo = knapsack.getNumItem();
					double ep = 0.5;
					double scaleFactor = ep * ( aMax / itemNo);

					long startTime = System.nanoTime();
					double res = knapsackApproxScheme(knapsack, scaleFactor, false);
					//				System.out.println("FPTAS: \t\t\t\t" + res);
					LOGGER.info("FPTAS: " + res);
					long endTime = System.nanoTime();
					//				System.out.println("Running time: " + (endTime - startTime)/1000000 + " ms");
					LOGGER.info("Running time: " + (endTime - startTime)/1000000.0 + " ms");
					//				System.out.println();
				}
				catch( Throwable t)
				{
					//				System.out.println("Something went wrong with the Greedy Knapsack");
					//LOGGER.info("Something went wrong with the FPTAS Knapsack", t);
					LOGGER.info("FPTAS: NA");
					LOGGER.info("Running time: NA");
					t.printStackTrace();
				}
			}
		}

		/* 
		 * NOTE ON AUTOMATION:
		 * 1. If we're actually runnning simulation that generates log to be parsed, 
		 * 		set runningSimulation = true
		 * 2. If it's on Vu's laptop, set vuMachine to true to open Data Analysis R script
		 * 		in Rstudio
		 */
		if(runningSimulation)
		{
			// automate running Python script to parse log into csv.
			String cmd = "";
			if(testingKnapsack)
			{
				cmd = "python scripts/extractDataFromLog.py";
			}
			else
			{
				cmd = "python scripts/extract3SATDataFromLog.py";
			}
			try {
				Runtime.getRuntime().exec(cmd);
				System.out.println("Parsed log into csv.");

				if(vuMachine)
				{
					String openRStudio = "open -a Rstudio Report/DataAnalysis.R";
					Runtime.getRuntime().exec(openRStudio);
				}
			} catch (IOException e) {
				System.out.println("Cannot run python script or open RStudio.");
			}
		}
	}
}
