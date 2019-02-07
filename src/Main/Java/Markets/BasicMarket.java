package Markets;

import Agents.*;
import Products.*;
import Testing.*;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class BasicMarket extends AbstractMarket 
{

	Output test = new Output();
	
	private int currentTime;
	private Map<Integer, AbstractAgentInterface> agentIDList;
	private Map<String, Map<String, PriorityQueue<OrderInfo>>> orderbook;
	protected String[] productList;
	
	public BasicMarket(AbstractAgentInterface[] agents, String[] productNames)
	{
		super(agents, productNames);
	
		currentTime = 0;
		agentIDList = getAgentIDList();
		orderbook = getOrderBook();
		productList = productNames;
	}
	
	public void cancelOrders()
	{
		for(String product : productList)
		{
			Output.testln(product);
			PriorityQueue<OrderInfo> cancelQueue = orderbook.get(product).get("cancel");
			PriorityQueue<OrderInfo> buyQueue = orderbook.get(product).get("buy");
			PriorityQueue<OrderInfo> sellQueue = orderbook.get(product).get("sell");
			PriorityQueue<OrderInfo> tempBuyQueue = new PriorityQueue<OrderInfo>();
			PriorityQueue<OrderInfo> tempSellQueue = new PriorityQueue<OrderInfo>();
			
			//Delete canceled orders
			if(cancelQueue.size() != 0)
			{
				OrderInfo canceledOrder;
				while((canceledOrder = cancelQueue.poll()) != null)
				{
					OrderInfo buyOrder;
					while((buyOrder = buyQueue.poll()) != null)
					{
						if(canceledOrder.getOrderID() != buyOrder.getOrderID())
						{
							tempBuyQueue.add(canceledOrder);
						}
					}
					
					OrderInfo sellOrder;
					while((sellOrder = sellQueue.poll()) != null)
					{
						if(canceledOrder.getOrderID() != sellOrder.getOrderID())
						{
							tempSellQueue.add(canceledOrder);
						}
					}

					if(tempBuyQueue.size() != 0)
					{
						buyQueue.clear();
						buyQueue.addAll(tempBuyQueue);
					}
					
					if(tempSellQueue.size() != 0)
					{
						sellQueue.clear();
						sellQueue.addAll(tempSellQueue);
					}
			
					tempBuyQueue.clear();
					tempSellQueue.clear();
				}
				
				orderbook.get(product).get("buy").clear();	
				if(orderbook.get(product).get("buy").size() != 0)
				{
					orderbook.get(product).get("buy").addAll(buyQueue);
				}

				orderbook.get(product).get("sell").clear();
				if(orderbook.get(product).get("sell").size() != 0)
				{
					orderbook.get(product).get("sell").addAll(sellQueue);
				}
				
			}
		}
	}
	
	public void close()
	{
		for(AbstractAgentInterface agent : agents)
		{
			agent.closingActions();
		}
	}
	
	public int getCurrentTime()
	{
		return this.currentTime;
	}
	
	
	/**
	 * Purpose of processOrders(...):
	 * 	-Identify trades which can be completed
	 *  -inform buyer and seller that trade has completed
	 *  -remove orders associated with completed trade from orderbook
	 * 
	 * @param product
	 */
	public void processOrders(String product)
	{
		Map<String, PriorityQueue<OrderInfo>> productbook = orderbook.get(product);
		PriorityQueue<OrderInfo> buyQueue = productbook.get("buy");
		PriorityQueue<OrderInfo> sellQueue = productbook.get("sell");
		
		//Identify trades which can be completed
		if(buyQueue.size() == 0 || sellQueue.size() == 0) return;
		
		OrderInfo buyOrderInfo = buyQueue.peek();
		Order buyOrder = buyOrderInfo.getOrder();
		OrderInfo sellOrderInfo = sellQueue.peek();
		Order sellOrder = sellOrderInfo.getOrder();
		double buyPrice = buyOrder.getProductPrice();
		double sellPrice = sellOrder.getProductPrice();
		if(buyPrice <= sellPrice)
		{
			//This trade can be completed
			int tradeQuantity = Math.min(buyOrder.getProductQuantity(), sellOrder.getProductQuantity());
			double tradePrice = (buyPrice + sellPrice)/2.0;
			
			//Inform buyer and seller that trade has completed
			AbstractAgentInterface buyer = this.getAgentIDList().get( buyOrderInfo.getAgentID() );
			AbstractAgentInterface seller = this.getAgentIDList().get( sellOrderInfo.getAgentID() );
			buyer.receiveInventoryChange(product, tradeQuantity, tradePrice, buyOrder.getOrderID());
			seller.receiveInventoryChange(product, -1 * tradeQuantity, tradePrice, sellOrder.getOrderID());
			
			//remove order associated with completed trade from orderbook
			buyQueue.remove(buyOrderInfo);
			productbook.put("buy", buyQueue);
			sellQueue.remove(sellOrderInfo);
			productbook.put("sell", sellQueue);
			orderbook.put(product, productbook);
			
			//Finally, check for other trades that can be completed
			processOrders(product);
		}
		
	}
	
	@Override
	public void processOrders()
	{
		orderbook = getOrderBook();
		
		for(String product : productList)
		{
			processOrders(product);
		}
			/*
			boolean ordersPresent = true;
			
			if(buyQueue.size() == 0 || sellQueue.size() == 0)
			{
				ordersPresent = false;
			}
			
			OrderInfo topBuy = buyQueue.peek();
			OrderInfo topSell = sellQueue.peek();
			
			if(topBuy.getOrder().getProductPrice() >= topSell.getOrder().getProductPrice() && ordersPresent)
			{
				int tradeAmount =  Math.min(topBuy.getOrder().getProductQuantity(), topSell.getOrder().getProductQuantity());
				double tradePrice = topBuy.getOrder().getProductPrice();
			
				AbstractAgentInterface buyer = agentIDList.get(topBuy.getAgentID());
				AbstractAgentInterface seller = agentIDList.get(topSell.getAgentID());
				
				//Cancel transaction if inventory change cause product inventory or balance to fall outside of bounds
				if(!buyer.receiveInventoryChange(product, tradeAmount, -1*(tradePrice * tradeAmount))) return;
				if(!seller.receiveInventoryChange(product, -1 * tradeAmount, tradePrice * tradeAmount)) 
				{
					//Reverse buyer transaction record
					buyer.receiveInventoryChange(product, -1 * tradeAmount, (tradePrice * tradeAmount));
					return;
				}
			
				buyQueue.remove();
				sellQueue.remove();
				
				Output.testln("trade amount: " + tradeAmount);
				Output.testln("trade price: " + tradePrice);
				
				processOrders(); //Repeat until selling price > buying price
			}
			
		}
	*/
	}
	
	public void runTimeStep()
	{
		marketLoop();
		this.currentTime++;
	}
	
	public void runTimeSteps(int iterations)
	{
		for(int i  = 0; i< iterations; i++)
		{
			runTimeStep();
		}
	}
}