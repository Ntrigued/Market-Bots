package Markets;

import Agents.*;
import Products.*;
import Testing.*;

import java.util.Map;
import java.util.PriorityQueue;

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
	}
	
	public void cancelOrders()
	{
		orderbook = getOrderBook();
	
		for(String product : ProductList.getList())
		{
			PriorityQueue<OrderInfo> cancelQueue = orderbook.get(product).get("cancel");
		
			//Delete canceled orders
			if(cancelQueue.size() != 0)
			{
		
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
			
			if(buyQueue.size() == 0 || sellQueue.size() == 0) return;
			
			OrderInfo topBuy = buyQueue.peek();
			OrderInfo topSell = sellQueue.peek();
			
			if(topBuy.getOrder().getProductPrice() < topSell.getOrder().getProductPrice()) return;
			else
			{
				int tradeAmount = Math.abs(topBuy.getOrder().getProductQuantity() - topSell.getOrder().getProductQuantity());
				double tradePrice = (topBuy.getOrder().getProductPrice() + topSell.getOrder().getProductPrice())/2;
			
				AbstractAgent buyer = agentIDList.get(topBuy.getAgentID());
				AbstractAgent seller = agentIDList.get(topSell.getAgentID());
				
				buyer.receiveInventoryChange(product, tradeAmount, -1*(tradePrice * tradeAmount));
				seller.receiveInventoryChange(product, -1 * tradeAmount, tradePrice * tradeAmount);
			
				processOrders(); //Repeat until selling price > buying price
			}
			
		}
	}
}
