package Agents;

import Markets.Order;
import Products.*;
import Testing.*;

import java.util.*;

public class BasicAgent extends AbstractAgent{
	
	Output test = new Output();
	int orderID;
	Map<String, Integer> productInventory;
	
	public BasicAgent(double balance, String[] products)
	{
		setBalance(balance);
		productInventory = new HashMap<String, Integer>();
		
		for(String product : products) productInventory.put(product, 100);
		
		orderID = 0;
	}
	
	public void closingActions()
	{
		test.println("Apples: " + productInventory.get("Apple"));
		test.println("balance: " + getBalance());
	}
	
	public int getItemInventory(String productName)
	{
		return productInventory.get(productName);
	}
	
	//Called during agent interaction stage
	public void interact() {} 
	
	//Called if order has been filled
	public void receiveInventoryChange() {} 
	
	int x = 0;
	//Called for bid/offer stage
	public Order[] sendOrderDetails()
	{
		int orderType;
		int orderNum;
		
		if(getBalance() > 1000) 
		{
			orderID++;
			orderType = 1;
			orderNum = (int) Math.rint(.25 * (getBalance()));
			
		} else if(productInventory.get("Apple") > 0)
		{
			orderNum = productInventory.get("Apple");
			orderType = 0;
			
		} else return null;
		
		Order order = new Order(new Apple(), orderNum, 5.0, orderType, orderID);
		Order[] orders = new Order[1];
		orders[0] = order;

		return orders;
	} 
	
	public boolean receiveInventoryChange(String productName, int amountTraded, double balanceChange)
	{
		System.out.println("recieveInventoryChange: " + productName + "  " + amountTraded + "  " + balanceChange);
		int currentQuantity = productInventory.get(productName);
		if(currentQuantity + amountTraded < 0) return false;
		
		double currentBalance = getBalance();
		if(currentBalance + balanceChange < 0) return false;
		
		productInventory.put(productName, currentQuantity + amountTraded);
		setBalance(currentBalance + balanceChange);
		
		return true;
	}

	public void setItemInventory(String productName, int amount)
	{
		productInventory.put(productName, amount);
	}
	
	//called for update accounting stage
	public void updateAccounting() {} 
	
	public void otherActions()
	{
		
	}
}
