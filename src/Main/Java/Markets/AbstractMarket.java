package Markets;

import Markets.*;
import Agents.*;
import Products.*;
import Testing.*;

import java.util.*;

public abstract class AbstractMarket
{
	public abstract void close(); // perform closing actions on market
	public abstract void cancelOrders();
	public abstract void processOrders(); //handles orders after they have been sent to their respective queue in orderbook
	public abstract void runTimeStep();
	public abstract void runTimeSteps(int iterations);

	Output test = new Output();
	
	AbstractAgentInterface[] agents;

	private Map<String, Map<String, PriorityQueue<OrderInfo>>> orderbook;
	private Map<Integer, AbstractAgentInterface> agentIDList;
	
	public AbstractMarket(AbstractAgentInterface[] agents, String[] productNames)
	{
		this.agents = agents;
		
		int key = 1;
		agentIDList = new HashMap<Integer, AbstractAgentInterface>();
		for(AbstractAgentInterface agent : this.agents)
		{
			agentIDList.put(key, agent);
			key++;
		}
		
		orderbook = new HashMap<String, Map<String, PriorityQueue<OrderInfo>>>();
		
		Comparator<OrderInfo> orderCompare = new Comparator<OrderInfo>()
		{
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
	
	public void addAgent(AbstractAgentInterface newAgent)
	{
		AbstractAgentInterface[] temp = this.agents;
		this.agents = new AbstractAgentInterface[this.agents.length + 1];
		for(int i = 0; i < temp.length; i++) 
			this.agents[i] = temp[i];
		this.agents[ this.agents.length - 1 ] = newAgent;
	}
	
	public void displayOrderBook()
	{
		Map<String, Map<String, PriorityQueue<OrderInfo>>> orderbook = getOrderBook();
		Set<String> products = orderbook.keySet();
	
		for(String product : products)
		{
			System.out.println(product + ":");
			
			Map<String, PriorityQueue<OrderInfo>> productbook = orderbook.get(product);
		
			System.out.println("buy orders");
			PriorityQueue<OrderInfo> buyQueue = productbook.get("buy");
			for(OrderInfo buy : buyQueue)
			{
				System.out.println(buy.getAgentID() + "\t" + buy.getOrder().getProductQuantity() + "\t" + buy.getOrder().getProductPrice());
			}
			
			System.out.println("\n\nsell orders");
			PriorityQueue<OrderInfo> sellQueue = productbook.get("sell");
			for(OrderInfo sell : sellQueue)
			{
				System.out.println(sell.getAgentID() + "\t" + sell.getOrder().getProductQuantity() + "\t" + sell.getOrder().getProductPrice());
			}
		
			System.out.println("\n\ncancel orders");
			PriorityQueue<OrderInfo> cancelQueue = productbook.get("cancel");
			for(OrderInfo sell : cancelQueue)
			{
				System.out.println(sell.getAgentID() + "\t" + sell.getOrder().getProductQuantity() + "\t" + sell.getOrder().getProductPrice());
			}
			
			System.out.println("\n\n\n"); //Spacing between products
		}		
	}

	protected Map<Integer, AbstractAgentInterface> getAgentIDList()
	{
		return this.agentIDList;
	}
	
	public Map<String, Map<String, PriorityQueue<OrderInfo>>> getOrderBook()
	{
		return this.orderbook;
	}
	
	public void setProductBook(String productName, Map<String, PriorityQueue<OrderInfo>> productBook)
	{
		orderbook.put(productName, productBook);
	}
	
	//loop that runs every round of market activity
	public void marketLoop()
	{
		for(int i = 1; i <= agentIDList.size(); i++ )
		{
			receiveOrderDetails(agentIDList.get(i).sendOrderDetails(), i);
		}
		cancelOrders();
		processOrders();
		
		for(AbstractAgentInterface agent : agents)
		{
			agent.interact();
		}
		
		AbstractProduct.registerTimeStep();
		
		for(AbstractAgentInterface agent : agents)
		{
			agent.updateAccounting();
		}
		
		for(AbstractAgentInterface agent: agents)
		{
			agent.otherActions();
		}
	}
	
	private void receiveOrderDetails(Order[] orders, int agentID)
	{
		if(orders == null)
		{
			test.println("orders is null");
			return;
		}
		
		System.out.println(agentID);
		
		int numOfOrder = 1;
		for(Order order : orders)
		{
			if(order.getProductQuantity() <= 0)
				continue;
			
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
			orderList.put(orderType, orderTypeList);
			orderbook.put(order.getProductName(), orderList); 
		
		}
	}
	
}