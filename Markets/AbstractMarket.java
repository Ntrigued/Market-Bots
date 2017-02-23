package Markets;

import Agents.*;
import Products.*;
import Testing.*;

import java.util.*;

public abstract class AbstractMarket {

	Output test = new Output();
	
	AbstractAgent[] agents;

	private Map<String, Map<String, PriorityQueue<OrderInfo>>> orderbook;
	private Map<Integer, AbstractAgent> agentIDList;
	
	public AbstractMarket(AbstractAgent[] agents, String[] productNames)
	{
		this.agents = agents;
		
		int key = 1;
		agentIDList = new HashMap<Integer, AbstractAgent>();
		for(AbstractAgent agent : this.agents)
		{
			agentIDList.put(key, agent);
			key++;
		}
		
		orderbook = new HashMap<String, Map<String, PriorityQueue<OrderInfo>>>();
		
		Comparator<OrderInfo> orderCompare = new Comparator<OrderInfo>()
		{
			@Override
			public int compare(OrderInfo one, OrderInfo two)
			{
				if(one.getOrder().getProductPrice() > two.getOrder().getProductPrice())
				{
					return 1;
				}
				
				if(one.getOrder().getProductPrice() < two.getOrder().getProductPrice())
				{
					return -1;
				}
				
				return 0;
			}
		};
		for(String productName : productNames)
		{
			HashMap<String, PriorityQueue<OrderInfo>> orderMap = new HashMap<String, PriorityQueue<OrderInfo>>();
			orderMap.put("buy", new PriorityQueue<OrderInfo>(1000, orderCompare));
			orderMap.put("sell", new PriorityQueue<OrderInfo>(1000, orderCompare));
			orderMap.put("cancel", new PriorityQueue<OrderInfo>(1000, orderCompare));
			
			orderbook.put(productName, orderMap);
		}
	}
	
	public Map<Integer, AbstractAgent> getAgentIDList()
	{
		return this.agentIDList;
	}
	
	public Map<String, Map<String, PriorityQueue<OrderInfo>>> getOrderBook()
	{
		return this.orderbook;
	}
	
	//loop that runs every round of market activity
	public void marketLoop()
	{
		for(int i = 1; i <= agentIDList.size(); i++ )
		{
			receiveOrderDetails(agentIDList.get(i).sendOrderDetails(), i);
			processOrders();
		}
		
		for(AbstractAgent agent : agents)
		{
			agent.interact();
		}
		
		for(AbstractAgent agent : agents)
		{
			agent.updateAccounting();
		}
		
		for(AbstractAgent agent: agents)
		{
			agent.otherActions();
		}
	}
	
	//handles orders after they have been sent to their respective queue in orderbook
	public void processOrders() {}
	
	private void receiveOrderDetails(Order[] orders, int agentID)
	{
		for(Order order : orders)
		{
			OrderInfo orderInfo = new OrderInfo(order, agentID, order.getOrderID());
			Map<String, PriorityQueue<OrderInfo>> orderList = orderbook.get(order.getProductName());
		
			PriorityQueue<OrderInfo> orderTypeList;
			String orderType;
			
			if(order.getOrderType() == 1) orderType = "buy";
			else if(order.getOrderType() == 0) orderType = "sell";
			else orderType = "cancel";
			
			orderTypeList = orderList.get(orderType);
			
			orderTypeList.add(orderInfo);
			
			//Might not be necessary, but why risk it?
			test.println("type: " + orderType);
			orderList.put(orderType, orderTypeList);
			orderbook.put(order.getProductName(), orderList); 
		}
	}
}
