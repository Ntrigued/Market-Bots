package Agents;

import java.util.*;

import Markets.*;
import Products.ProductList;
import Testing.*;

/* Adds memory of open orders to BasicAgent */
public class BasicMemoryAgent extends BasicAgent {
	Output test;
	private Hashtable<Integer, Order> openOrders;
	private Map<String, Double> currentPrices;
	private Map<String, Integer> productInventory;
	
	public BasicMemoryAgent(double balance, String[] products) {
		super(balance, products);
		
		test = new Output();
		this.openOrders = new Hashtable<Integer, Order>();
		this.currentPrices = new HashMap<String, Double>();
		this.productInventory = new HashMap<String, Integer>();
		
		for(String product : ProductList.getList())
		{
			productInventory.put(product, 0);
		}
	}

	public void addOpenOrder(Order order)
	{
		openOrders.put(order.getOrderID(), order);
	}
	
	public boolean deleteOpenOrder(Order order)
	{
		Order canceledOrder = openOrders.remove(order);
	
		if(canceledOrder == null) return false;
		return true;
	}
	
	public Map<String, Double> getCurrentPriceMap()
	{
		return currentPrices;
	}
	
	public Map<String, Integer> getProductInventory()
	{
		return this.productInventory;
	}
	
	public void setCurrentPrice(String productName, double price)
	{
		test.println("ABC: " + productName);
		test.println("XYZ: " + price);
		currentPrices.put(productName, price);
	}
	
	public void setProductInventory(Map<String, Integer> productInventory)
	{
		this.productInventory = productInventory;
	}
}
