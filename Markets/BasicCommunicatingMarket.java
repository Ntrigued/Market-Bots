package Markets;

import java.util.*;
import java.util.Set;

import Agents.AbstractAgent;
import Products.ProductList;

public class BasicCommunicatingMarket extends BasicMarket {

	public BasicCommunicatingMarket(AbstractAgent[] agents, String[] productNames, int loopIterations) {
		super(agents, productNames, loopIterations);
	}
	
	public String[] getProductList()
	{
		return ProductList.getList();
	}
	
	@Override
	public void processOrders()
	{
		Map<String, Map<String, PriorityQueue<OrderInfo>>> orderbook = getOrderBook();
		
		for(String product : ProductList.getList())
		{
			Map<String, PriorityQueue<OrderInfo>> productbook = orderbook.get(product);
			PriorityQueue<OrderInfo> buyQueue = productbook.get("buy");
			PriorityQueue<OrderInfo> sellQueue = productbook.get("sell");
			boolean ordersPresent = true;
			
			if(buyQueue.size() == 0 && sellQueue.size() == 0) 
			{
				test.println("processOrders: no orders for " + product + " were submitted");
			}
			
			OrderInfo topBuy = buyQueue.peek();
			OrderInfo topSell = sellQueue.peek();
			
			if(topBuy != null && topSell != null && ordersPresent)
			{
				if(topBuy.getOrder().getProductPrice() >= topSell.getOrder().getProductPrice())
				{
					int tradeAmount =  Math.min(topBuy.getOrder().getProductQuantity(), topSell.getOrder().getProductQuantity());
					double tradePrice = topSell.getOrder().getProductPrice();
				
					AbstractAgent buyer = getAgentIDList().get(topBuy.getAgentID());
					AbstractAgent seller = getAgentIDList().get(topSell.getAgentID());
					
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
						getAgentIDList().get(agentID).setCurrentPrice(product, tradePrice);
					}
					
					test.println("trade amount: " + tradeAmount);
					test.println("trade price: " + tradePrice);
					
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
						getAgentIDList().get(agentID).setCurrentPrice(product, (topBuy.getOrder().getProductPrice() - topSell.getOrder().getProductPrice())/2.0);
					}
				}
			} else if(topBuy != null) //topSell = null
			{
				Set<Integer> agentIDs = getAgentIDList().keySet();
				for(Integer agentID : agentIDs)
				{
					getAgentIDList().get(agentID).setCurrentPrice(product, topBuy.getOrder().getProductPrice());
				}
				
			}else if(topSell != null) //topBuy = null
			{
				Set<Integer> agentIDs = getAgentIDList().keySet();
				for(Integer agentID : agentIDs)
				{
					getAgentIDList().get(agentID).setCurrentPrice(product, topSell.getOrder().getProductPrice());
				}
			}
		}
	}
}
