package main.java.project;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
				int ele = v[a];
				if(a == v.length / 10)
				{
					st.append(".");
				}
				st.append(ele);
			}
			//			st.append(".0");
			//for(int ele : v) st.append(ele);
			Double vInt = Double.parseDouble(st.toString());

			st = new StringBuilder();
			for(int a = 0 ; a < vPrime.length; a ++)
			{
				int ele = vPrime[a];
				if(a == vPrime.length / 10)
				{
					st.append(".");
				}
				st.append(ele);
			}
			//			st.append(".0");
			Double vPrimeInt = Double.parseDouble(st.toString());

			subsetSumItems.add(vInt);
			subsetSumItems.add(vPrimeInt);
		}
		System.out.println(subsetSumItems);

		StringBuilder st = new StringBuilder();
		for(int i = 0 ; i < nPlusM; i++) 
		{
			if(i == nPlusM / 10)
			{
				st.append(".");
			}
			st.append("1");

		}
		//		st.append(".0");
		Double T = Double.parseDouble(st.toString());

		//System.out.println(T);
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

		ArrayList<Item> itemListKS = new ArrayList<Item>();

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
	 * dynamic programming for knapsack algorithm
	 * @param knapsack
	 * @return
	 */
	public static int dynamicProgrammingKnapsack(Knapsack knapsack)
	{
		int numItem = knapsack.getNumItem();
		int budget = (int) knapsack.getBudget();

		int[][] table = new int[numItem+1][budget+1];

		for(int i = 0; i < numItem; i ++)
		{
			table[i][0] = 0;
		}
		for(int b = 1; b <= budget; b++)
		{
			table[numItem-1][budget] = 0;
		}
		for(int i = numItem-1; i > 0; i--)
		{
			for(int b = 1 ; b <= budget; b++)
			{
				Item item = knapsack.getItem(i);
				if(item.getCost() > b)
				{
					table[i][b] = table[i+1][b];
				}
				else
				{
					table[i][b] = Math.max(table[i+1][b- (int) item.getCost()] + (int) item.getValue(), table[i+1][b]);
				}
			}
		}
		return table[1][budget];
	}

	public static int dynamicProgrammingKnapsackMinCost(Knapsack knapsack)
	{
		int numItem = knapsack.getNumItem();
		int budget = (int) knapsack.getBudget();
		int target = (int) knapsack.getTarget();

		// calculate aMax
		int aMax = calculateAMax(knapsack);

		int[][] minCost = new int[numItem][numItem * aMax + 1];

		for(int i = 0 ; i < minCost.length; i++)
		{
			for(int j = 0 ; j < minCost[i].length; j++)
			{
				minCost[i][j] = Integer.MAX_VALUE;
			}
		}
		boolean[][] take = new boolean[numItem][numItem * aMax + 1];

		solveMaximumKnapsack(knapsack, minCost, take, aMax, numItem, budget);

		int optimalValue = constructMaxKnapsackSolution(knapsack, minCost, take, aMax, numItem, budget);
		return optimalValue;
	}

	private static void solveMaximumKnapsack(Knapsack knapsack,
			int[][] minCost, boolean[][] take, int aMax, int numItem, int budget) {

		// when target is 0, there's no cost
		for(int i = 0; i < numItem; i++)
		{
			minCost[i][0] = 0;
		}

		// when t <= v(1), target t can be achieved by taking object 1
		for(int t = 0; t <= knapsack.getItem(0).getValue(); t++)
		{
			minCost[1][t] = (int) knapsack.getItem(0).getCost();
			take[1][t] = true;
		}

		// when t > v(1), target cannot be reached with only object 1 available
		for(int t = (int) knapsack.getItem(0).getValue() + 1; t <= numItem*aMax; t++)
		{
			minCost[1][t] = Integer.MAX_VALUE;
			take[1][t] = false;
		}

		for(int i = 2; i < numItem; i++)
		{
			for(int t = 1; t < numItem*aMax; t++)
			{
				int nextT = (int) Math.max(0, t - knapsack.getItem(i).getValue());
				if(minCost[i-1][t] <= knapsack.getItem(i).getCost() + minCost[i-1][nextT]) // don't include i
				{
					minCost[i][t] = minCost[i-1][t];
					take[i][t] = false;
				}
				else // include i
				{
					minCost[i][t] = (int) (knapsack.getItem(i).getCost() + minCost[i-1][nextT]);
					take[i][t] = true;
				}
			}
		}
	}

	private static int constructMaxKnapsackSolution(Knapsack knapsack, int[][] minCost, boolean[][] take, int aMax, int numItem, int budget) {

		int optimalValue = numItem * aMax;

		//		System.out.println(minCost[numItem-1][optimalValue]);
		while(optimalValue > 0 && minCost[numItem-1][optimalValue] > budget)
		{
			optimalValue--;
		}

		// intialize solution to be empty
		ArrayList<Item> solution = new ArrayList<Item>();

		int i = numItem-1;
		int t = optimalValue;
		//		System.out.println(knapsack.getItemList().size());
		while(i > 0 && t > 0)
		{
			//			System.out.println("i: " + i + " - t: " + t);
			if(take[i][t])
			{
				solution.add(knapsack.getItem(i));
				t = t - (int) knapsack.getItem(i).getValue();
			}
			i = i - 1;
		}

		int res = 0;

		for(Item item : solution)
		{
			res += item.getValue();
		}
		return res;
	}

	/**
	 * Greedy algorithm for knapsack
	 * @param knapsack
	 * @return
	 */
	public static int greedyKnapsack(Knapsack knapsack)
	{
		ArrayList<Item> G = new ArrayList<Item>();

		// sort based on custom comparator that does based on value/cost
		Collections.sort(knapsack.getItemList(), new Implementation(). new CustomComparator());
		//		for(Item item : knapsack.getItemList())
		//		{
		//			System.out.println(item.getValue()/item.getCost());
		//		}

		int L = (int) knapsack.getBudget();

		for(Item item : knapsack.getItemList())
		{
			//			System.out.println(item.getValue() / item.getCost());
			if(L > 0)
			{
				if(item.getCost() <= L)
				{
					G.add(item);
					L -= item.getCost();
				}
			}
			else
			{
				break;
			}
		}

		int aMax = 0;
		for(Item item : knapsack.getItemList())
		{
			// if cost > budget, we cannot take it as well
			if(item.getValue() > aMax && item.getCost() <= knapsack.getBudget()) aMax = (int) item.getValue();
		}

		int currSum = 0;
		for(Item item : G)
		{
			currSum += (int) item.getValue();
		}

		return aMax > currSum? aMax : currSum;
	}

	public static int knapsackApproxScheme(Knapsack knapsack, double scaleFactor)
	{
		ArrayList<Item> itemList = knapsack.getItemList();
		ArrayList<Item> scaled = new ArrayList<Item>();
		int numItem = knapsack.getNumItem();
		int budget = (int) knapsack.getBudget();

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
		int aMax = calculateAMax(newInstance);

		int[][] minCost = new int[numItem][numItem * aMax + 1];

		for(int i = 0 ; i < minCost.length; i++)
		{
			for(int j = 0 ; j < minCost[i].length; j++)
			{
				minCost[i][j] = Integer.MAX_VALUE;
			}
		}
		boolean[][] take = new boolean[numItem][numItem * aMax + 1];

		solveMaximumKnapsack(newInstance, minCost, take, aMax, numItem, budget);

		//		aMax = calculateAMax(knapsack);
		int optimalValue = constructMaxKnapsackSolution(newInstance, minCost, take, aMax, numItem, budget);
		return (int) (optimalValue * scaleFactor);
	}

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

	public static void main(String[] args)
	{
		// NEED THIS TO GENERATE LOG AS RAW DATA
		Logger LOGGER = Logger.getLogger(Implementation.class);

		boolean testing3SAT = false;
		if(testing3SAT)
		{
			//for(int i = 0 ; i < 100; i++)
			//{
			ClauseCollection ThreeSAT = InstanceGenerator.get3SAT();
			System.out.println(ThreeSAT);
			System.out.println();
			System.out.println(reduce3SATTo1In3SAT(ThreeSAT));
			System.out.println();
			System.out.println(reduce1In3SATToSubsetSum(reduce3SATTo1In3SAT(ThreeSAT))); // TODO: too big, tweaking with clauses size right now
			SubsetSum ss = reduce1In3SATToSubsetSum(reduce3SATTo1In3SAT(ThreeSAT));
			Knapsack ks = reduceSubsetSumToKnapsack(ss);
			System.out.println();
			System.out.println(ks);
			System.out.println();
			System.out.println("DP1: " + dynamicProgrammingKnapsack(ks));
			System.out.println("DP2: " + dynamicProgrammingKnapsackMinCost(ks));
			System.out.println("Greedy: " + greedyKnapsack(ks));
			System.out.println("FPTAS: " + knapsackApproxScheme(ks, 2));
			//}
		}

		boolean testingKnapsack = false;
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
					int res = dynamicProgrammingKnapsack(knapsack);
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
					int res = dynamicProgrammingKnapsackMinCost(knapsack);
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
					int res = greedyKnapsack(knapsack);
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
					int res = knapsackApproxScheme(knapsack, scaleFactor);
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
		boolean runningSimulation = false;
		if(runningSimulation)
		{
			// automate running Python script to parse log into csv.
			String cmd = "python scripts/extractDataFromLog.py";
			try {
				Runtime.getRuntime().exec(cmd);
				System.out.println("Parsed log into csv.");

				boolean vuMachine = false;
				if(vuMachine)
				{
					String openRStudio = "open -a RStudio Report/DataAnalysis.R";
					Runtime.getRuntime().exec(openRStudio);
				}
			} catch (IOException e) {
				System.out.println("Cannot run python script or open RStudio.");
			}
		}
	}
}
