package MarketBots.Markets;

import java.util.*;

import MarketBots.Agents.*;
import MarketBots.Products.ProductList;
import MarketBots.Testing.Output;

public class BasicCommunicatingMarket extends BasicMarket {

	public BasicCommunicatingMarket(AbstractAgentInterface[] agents, String[] productNames) 
	{
		super(agents, productNames);
	}
	
	public String[] getProductList()
	{
		return this.productList;
	}
	
	/**
	 * Purpose of processOrders(...):
	 * 	-Identify trades which can be completed
	 *  -inform buyer and seller that trade has completed
	 *  -remove orders associated with completed trade from orderbook
	 *  -Inform all agents which implement MemoryAgentInterface that a trade has completed
	 * 
	 * @param product
	 */
	public void processOrders(String product)
	{
		Map<String, PriorityQueue<OrderInfo>> productbook = getOrderBook().get(product);
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
			getOrderBook().put(product, productbook);
			
			//Inform all agents that implement MemoryAgentInterface that a trade has completed
			Set<Integer> agentIDs = getAgentIDList().keySet();
			for(Integer agentID : agentIDs)
			{
				AbstractAgentInterface agent = getAgentIDList().get(agentID);
				if(agent instanceof MemoryAgentInterface)
					agent.setCurrentTrade(product, tradePrice, tradeQuantity);
			}
			
			Output.testln(tradeQuantity);
			//Finally, check for other trades that can be completed
			processOrders(product);
		}
		
	}
	
	@Override
	public void processOrders()
	{		
		for(String product : productList)
		{
			processOrders(product);
		}
	}
	
	/*
	@Override
	public void processOrders()
	{
		Map<String, Map<String, PriorityQueue<OrderInfo>>> orderbook = getOrderBook();
		
		for(String product : getProductList())
		{
			Map<String, PriorityQueue<OrderInfo>> productbook = orderbook.get(product);
			PriorityQueue<OrderInfo> buyQueue = productbook.get("buy");
			PriorityQueue<OrderInfo> sellQueue = productbook.get("sell");
			boolean ordersPresent = true;
			
			if(buyQueue.size() == 0 && sellQueue.size() == 0) 
			{
			//	test.testln("processOrders: no orders for " + product + " were submitted");
			}
			
			OrderInfo topBuy = buyQueue.peek();
			OrderInfo topSell = sellQueue.peek();
			
			if(topBuy != null && topSell != null && ordersPresent)
			{
				if(topBuy.getOrder().getProductPrice() >= topSell.getOrder().getProductPrice())
				{
					int tradeAmount =  Math.min(topBuy.getOrder().getProductQuantity(), topSell.getOrder().getProductQuantity());
					double tradePrice = topSell.getOrder().getProductPrice();
				
					AbstractAgentInterface buyer = getAgentIDList().get(topBuy.getAgentID());
					AbstractAgentInterface seller = getAgentIDList().get(topSell.getAgentID());
					
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
					
					Set<Integer> agentIDs = getAgentIDList().keySet();
					for(Integer agentID : agentIDs)
					{
						getAgentIDList().get(agentID).setCurrentTrade(product, tradePrice, tradeAmount);
					}
					
					productbook.put("buy", buyQueue);
					productbook.put("sell", sellQueue);
					setProductBook(product, productbook);
					
					processOrders(); //Repeat until selling price > buying price
				}
				else
				{
					Set<Integer> agentIDs = getAgentIDList().keySet();
					for(Integer agentID : agentIDs)
					{
						getAgentIDList().get(agentID).setCurrentTrade(product, (topBuy.getOrder().getProductPrice() - topSell.getOrder().getProductPrice())/2.0, 0);
					}
				}
			} else if(topBuy != null) //topSell = null
			{
				Set<Integer> agentIDs = getAgentIDList().keySet();
				for(Integer agentID : agentIDs)
				{
					getAgentIDList().get(agentID).setCurrentTrade(product, topBuy.getOrder().getProductPrice(), 0);
				}
				
			}else if(topSell != null) //topBuy = null
			{
				Set<Integer> agentIDs = getAgentIDList().keySet();
				for(Integer agentID : agentIDs)
				{
					getAgentIDList().get(agentID).setCurrentTrade(product, topSell.getOrder().getProductPrice(), 0);
				}
			}
		}
	}
	*/
}