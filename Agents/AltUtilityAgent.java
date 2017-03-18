package Agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AltUtilityAgent extends BasicUtilityAgent {
// Utility = Sum(productUtility * productAmount) + sqrt(balance)
	HashMap<String, Double> productUtilities;
	
	public AltUtilityAgent(double balance, String[] products) {
		super(balance, products);
	
		productUtilities = new HashMap<String, Double>();
		for(String product : products) productUtilities.put(product, Math.random() * 10);
	}
	
	public double calculateUtility(Map<String, Integer> productInventory, double balance)
	{
		double utility = 0;
		
		Set<String> products = productInventory.keySet();
		for(String product : products)
		{
			utility += productInventory.get(product) * productUtilities.get(product);
		}
		utility += Math.sqrt(balance);
		
		return utility;
	}
}
