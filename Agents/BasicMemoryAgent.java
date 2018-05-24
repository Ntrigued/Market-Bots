package MarketBots.Agents;

import java.util.*;

import MarketBots.Markets.*;
import MarketBots.Products.ProductList;
import MarketBots.Testing.*;

/* Adds memory of open orders to BasicAgent */
public class BasicMemoryAgent extends BasicAgent implements MemoryAgentInterface
{
	Output test;
	
	private int currentTime;
	private Map<String, Double> currentPrices;
	private HashMap<String, Integer> currentVolumes;
	private Hashtable<Integer, Order> openOrders;
	private Map<String, Integer> productInventory;
	private String[] productList;
	
	public BasicMemoryAgent(double balance, String[] products) {
		super(balance, products);
		
		test = new Output();
		this.currentTime      = 0;
		this.currentPrices    = new HashMap<String, Double>();
		this.currentVolumes   = new HashMap<String, Integer>();
		this.openOrders       = new Hashtable<Integer, Order>();
		this.productInventory = new HashMap<String, Integer>();
		this.productList      = products;
		
		for(String product : this.productList)
		{
			this.currentPrices.put(product, 0.0);
			this.currentVolumes.put(product, 0);
			this.productInventory.put(product, 0);
		}
	}

	public void addOpenOrder(Order order)
	{
		openOrders.put(order.getOrderID(), order);
	}
	
	public boolean deleteOpenOrder(int orderID)
	{
		Order canceledOrder = openOrders.remove(orderID);
	
		if(canceledOrder == null) return false;
		return true;
	}

	public Map<String, Double> getCurrentPriceMap()
	{
		return currentPrices;
	}
	
	public int getCurrentTime()
	{
		return this.currentTime;
	}

	public Map<String, Integer> getCurrentVolumeMap()
	{
		return currentVolumes;
	}
	
	
	public Map<String, Integer> getProductInventory()
	{
		return this.productInventory;
	}
	
	@Override
	public boolean receiveInventoryChange(String productName, int amountTraded, double balanceChange, int orderID)
	{
		boolean retval = super.receiveInventoryChange(productName, amountTraded, balanceChange, orderID);
		this.deleteOpenOrder(orderID);
		return retval;
	}
	
	@Override
	public Order[] sendOrderDetails()
	{
		Order[] orders = super.sendOrderDetails();
		for(Order order : orders)
			this.addOpenOrder(order);
		
		return orders;
	}
	
	public void setCurrentTrade(String productName, double price,
								int tradeVolume)
	{
		currentPrices.put(productName, price);
		currentVolumes.put(productName, tradeVolume);
	}
	
	public void setProductInventory(Map<String, Integer> productInventory)
	{
		this.productInventory = productInventory;
	}
	
	public void updateAccounting()
	{
		super.updateAccounting();
		this.currentTime++;
	}
}