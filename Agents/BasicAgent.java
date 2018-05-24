package MarketBots.Agents;

import MarketBots.Markets.Order;
import MarketBots.Products.*;
import MarketBots.Testing.*;

import java.util.*;

public class BasicAgent implements AbstractAgentInterface
{
	private double balance;
	int orderID;
	Map<String, Integer> productInventory;
	Output test = new Output();
	
	public BasicAgent(double balance, String[] products)
	{
		setBalance(balance);
		productInventory = new HashMap<String, Integer>();
		
		for(String product : products) productInventory.put(product, 100);
		
		orderID = 0;
	}
	
	public void closingActions()
	{
	}
	
	public int getItemInventory(String productName)
	{
		return productInventory.get(productName);
	}
	
	public void decreaseBalance(double decreaseAmount)
	{
		this.balance -= decreaseAmount;
	}
	
	public double getBalance() 
	{
		return this.balance;
	}
	
	public void increaseBalance(double increaseAmount)
	{
		this.balance += increaseAmount;
	}
	
	//Called during agent interaction stage
	public void interact() {}  

	public void otherActions() {}

	public void receive(String msg) 
	{
		
	}
	
	public boolean receiveInventoryChange(String productName, int amountTraded, double balanceChange, int orderID)
	{
		Output.testln("Trade: " + productName + " " + amountTraded);
		
		int currentQuantity = productInventory.get(productName);
		if(currentQuantity + amountTraded < 0) return false;
		
		double currentBalance = getBalance();
		if(currentBalance + balanceChange < 0) return false;
		
		productInventory.put(productName, currentQuantity + amountTraded);
		setBalance(currentBalance + balanceChange);
		
		return true;
	}
	
	public void setBalance(double balance) 
	{
		this.balance = balance;
	}
	
	public void setCurrentTrade(String productName, double price,
			int tradeVolume) {
		
	}

	public void setItemInventory(String productName, int amount)
	{
		productInventory.put(productName, amount);
	}

	//Called for bid/offer stage
	public Order[] sendOrderDetails()
	{
		return new Order[0];
	}
	
	//called for update accounting stage
	public void updateAccounting() {} 
}
