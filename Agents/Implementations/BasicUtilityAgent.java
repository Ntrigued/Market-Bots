package MarketBots.Agents.Implementations;

import MarketBots.Products.*;
import MarketBots.Agents.BasicMemoryAgent;
import MarketBots.Markets.*;
import MarketBots.Testing.*;

import java.util.*; 


/* 
  Creates a basic, utility-based agent which uses gradient descent
  to find an optimal bundle.
*/
public class BasicUtilityAgent extends BasicMemoryAgent {
	//Utility = Sum(sqrt(productamount*productutility)) + balance
	
	Map<String, Double> productUtility;
	int nextOrderID;
	
	public BasicUtilityAgent(double balance, String[] products) 
	{
		super(balance, products);
		nextOrderID = 0;
		
		productUtility = new HashMap<String, Double>();
		HashMap<String, Double> recentTradePrices = new HashMap<String, Double>();
		
		for(String productName : products)
		{
			productUtility.put(productName, 10*Math.random());
			recentTradePrices.put(productName, 0.0);
		}
		
		productUtility.put("Apple", Math.random() * 10);
	}
	
	public double calculateBundleCost(Map<String, Double> productCostMap, Map<String, Integer> productAmountMap)
	{
		double price, amount, totalCost;
		totalCost = 0;
		
		Set<String> products = productCostMap.keySet();
		if(products.size() == 0) return getBalance();
		
		for(String product : products)
		{
			if(productAmountMap.get(product) == null) return -1;
		}

		for(String product : products)
		{
			price = productCostMap.get(product);
			amount = productAmountMap.get(product);
			totalCost += (price * amount);
		}
		
		return totalCost;
	}
	
	public double calculateUtility(Map<String, Integer> productInventory, double balance)
	{
		if(balance < 0) return 0.0;
		
		double utility = 0;
		Set<String> productNames = productInventory.keySet();
		
		double pUtility;
		int pAmount;
		for(String productName : productNames)
		{
			pUtility = productUtility.get(productName);
			pAmount = productInventory.get(productName);
			utility += Math.sqrt(pUtility*pAmount);
		}
		utility += balance;
		
		return utility;
	}

	// Max: Sum((a*q)^0.5) + balance - Sum(p*q) (with simple gradient ascent algorithm)
	/*
	  Algorithm:
	  1) calculate utility with  q=0 for all q
	  
	   Loop
	   2) temporarily adjust q_1 to (q_1 + 1)
	   3) re-calculate utility
	   4) IF re-calculated utility > original utility, permanently adjust q_1 to (q_1 + 1)
	   5) repeat 2-4 for each q_i
	   
	   6) if an improvement was found in utility during the most recent loop, repeat the loop
	   7) return map of products with new amounts
	  */
	public Map<String, Integer> maximizeUtility(Map<String, Double> inputProductCostMap)
	{
		if(inputProductCostMap == null) 
		{
			Output.println("inputProductMap is null");
			return null;
		}
		
		HashMap<String, Double> productCostMap = new HashMap<String, Double>();
		HashMap<String, Integer> productMap = new HashMap<String, Integer>();
		HashMap<String, Integer> tempProductMap = new HashMap<String, Integer>();
		double utility, tempUtility;
		boolean utilityMaximized = false;
		boolean utilityImprovementFound;
		int tempProductAmount = 0;
		
		Set<String> products = inputProductCostMap.keySet();
		
		for(String product : products)
		{
			if(inputProductCostMap.get(product) == null) return null;
			if(inputProductCostMap.get(product) != 0.0) productCostMap.put(product, inputProductCostMap.get(product));
		}
		products = productCostMap.keySet();
		
		for(String productName : products)
		{
			productMap.put(productName, 0);
			tempProductMap.put(productName, 0);
		}
		utility = calculateUtility(productMap, getBalance());
		
		while(!utilityMaximized)
		{
			utilityImprovementFound = false;
			
			for(String product: products)
			{
				tempProductAmount = productMap.get(product);
				tempProductMap.clear();
				tempProductMap.putAll(productMap);
				tempProductMap.put(product, tempProductAmount + 1);
				if(calculateBundleCost(productCostMap, tempProductMap) == -1) 
				{
					return null;
				}
				tempUtility = calculateUtility(tempProductMap, getBalance() - calculateBundleCost(productCostMap, tempProductMap));
				if(tempUtility > utility)
				{
					productMap.clear();
					productMap.putAll(tempProductMap);
					utilityImprovementFound = true;
					utility = tempUtility;
				} else 
				{
					Output.println("tempUtility < utility");
				}
			}
			utilityMaximized = !utilityImprovementFound;
		//	test.println("maximizing utility-end: " + utility + "  " + tempProductAmount + " " + (getBalance() - calculateBundleCost(productCostMap, productMap)) + "  " + calculateBundleCost(productCostMap, productMap));
		}
		
		Output.println("maximizeUtility: " + productMap);
		return productMap;
	}
	
	public Order[] sendOrderDetails()
	{
		ArrayList<Order> orderList = new ArrayList<Order>();
		Map<String, Double> products = new HashMap<String, Double>();
		Map<String, Integer> targetPortfolio = maximizeUtility(getCurrentPriceMap());
		if(targetPortfolio == null)
		{
			return null;
		}
		Set<String> targetPortfolioKeys = targetPortfolio.keySet();
		
		for(String productName : targetPortfolioKeys)
		{
			nextOrderID++;
			Order order = null;
			int inventoryChange = targetPortfolio.get(productName) - getProductInventory().get(productName);
		
			if(inventoryChange > 0)
			{
				order = new Order(productName, inventoryChange, getCurrentPriceMap().get(productName), 1, nextOrderID);
			} else if(inventoryChange < 0)
			{
				order = new Order(productName, inventoryChange, getCurrentPriceMap().get(productName), 0, nextOrderID);
			}//else inventory is already at desired level
		
			if(order != null)
			{
				orderList.add(order);
				addOpenOrder(order);
			}
		}
		
		int size = orderList.size();
		Order[] orders = new Order[size];
		for(int i = 0; i<size; i++)
		{
			orders[i] = orderList.get(i);
		}
		
		return orders;
	}
}