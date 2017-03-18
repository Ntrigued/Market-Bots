package Markets;

import Agents.*;
import Products.*;
import Testing.*;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class BasicMarket extends AbstractMarket {

	Output test = new Output();
	
	private Map<String, Map<String, PriorityQueue<OrderInfo>>> orderbook;
	private Map<Integer, AbstractAgent> agentIDList;
	
	public BasicMarket(AbstractAgent[] agents, String[] productNames, int loopIterations)
	{
		super(agents, productNames);
	
		orderbook = getOrderBook();
		agentIDList = getAgentIDList();
		
		for(int i  = 0; i< loopIterations; i++)
		{
			marketLoop();
		}
		
		for(AbstractAgent agent : agents)
		{
			agent.closingActions();
		}
	}
	
	public void cancelOrders()
	{
		orderbook = getOrderBook();
	
		for(String product : ProductList.getList())
		{
			PriorityQueue<OrderInfo> cancelQueue = orderbook.get(product).get("cancel");
			PriorityQueue<OrderInfo> buyQueue = orderbook.get(product).get("buy");
			PriorityQueue<OrderInfo> sellQueue = orderbook.get(product).get("sell");
			PriorityQueue<OrderInfo> tempBuyQueue = new PriorityQueue<OrderInfo>();
			PriorityQueue<OrderInfo> tempSellQueue = new PriorityQueue<OrderInfo>();
			
			test.println("cancelQueue size: " + cancelQueue.size());
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
							test.println(canceledOrder.getOrderID());
							tempBuyQueue.add(canceledOrder);
						}
					}
					
					OrderInfo sellOrder;
					while((sellOrder = sellQueue.poll()) != null)
					{
						if(canceledOrder.getOrderID() != sellOrder.getOrderID())
						{
							test.println(canceledOrder.getOrderID());
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
	
	@Override
	public void processOrders()
	{
		orderbook = getOrderBook();
		
		for(String product : ProductList.getList())
		{
			Map<String, PriorityQueue<OrderInfo>> productbook = orderbook.get(product);
			PriorityQueue<OrderInfo> buyQueue = productbook.get("buy");
			PriorityQueue<OrderInfo> sellQueue = productbook.get("sell");
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
			
				AbstractAgent buyer = agentIDList.get(topBuy.getAgentID());
				AbstractAgent seller = agentIDList.get(topSell.getAgentID());
				
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
				
				test.println("trade amount: " + tradeAmount);
				test.println("trade price: " + tradePrice);
				
				processOrders(); //Repeat until selling price > buying price
			}
			
		}
	}
}
