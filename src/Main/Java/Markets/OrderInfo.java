package Markets;

public class OrderInfo 
{

	private final Order order;
	private final int agentID;
	private final int  orderID;
	
	public OrderInfo(Order order, int agentID, int orderID)
	{
		this.order = order;
		this.agentID = agentID;
		this.orderID = orderID;
	}
	
	public Order getOrder()
	{
		return this.order;
	}
	
	public int getAgentID()
	{
		return this.agentID;
	}
	
	public int getOrderID()
	{
		return this.orderID;
	}
}