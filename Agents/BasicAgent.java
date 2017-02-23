package Agents;

import Markets.Order;
import Products.*;

import java.util.*;

public class BasicAgent extends AbstractAgent{
	
	int orderID;
	Map<String, Integer> productInventory;
	
	public BasicAgent(double balance, String[] products)
	{
		setBalance(balance);
		productInventory = new HashMap<String, Integer>();
		
		for(String product : products) productInventory.put(product, 0);
		
		orderID = 0;
	}
	
	//Called during agent interaction stage
	public void interact() {} 
	
	//Called if order has been filled
	public void receiveInventoryChange() {} 
	
	//Called for bid/offer stage
	public Order[] sendOrderDetails()
	{
		orderID++;
		int orderNum = (int) Math.ceil(.25 * (getBalance()/5));
		Order order = new Order(new Apple(), orderNum, 5.0, 1, orderID);
		Order[] orders = new Order[1];
		orders[0] = order;
		
		return orders;
	} 
	
	public void receiveInventoryChange(String productName, int amountTraded, double balanceChange)
	{
		int currentQuantity = productInventory.get(productName);
		productInventory.put(productName, currentQuantity + amountTraded);
		
		double currentBalance = getBalance();
		setBalance(currentBalance + balanceChange);
	}
	
	//called for update accounting stage
	public void updateAccounting() {} 
	
	public void otherActions()
	{
		
	}
	
}
