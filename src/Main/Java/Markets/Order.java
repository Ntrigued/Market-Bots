package Markets;

import Products.*;

public class Order 
{
	private final String productName;
	private final int productQuantity;
	private final double productPrice;
	private final int orderType;
	private final int orderID;
	
	//Just for use in AbstractAgent
	public Order()
	{
		this.productName = "";
		this.productQuantity = 0;
		this.productPrice = 0;
		this.orderType = 0;
		this.orderID = 0;
	}
	
	public Order(AbstractProduct product, int quantity, double price, int orderType, int orderID) //orderType=1 => buy; orderType=0 => sell; orderType=-1 => retract orders with given orderID
	{
		this.productName = product.getName();
		this.productQuantity = quantity;
		this.productPrice = price;
		this.orderType = orderType;
		this.orderID = orderID;
	}

	public Order(String productName, int quantity, double price, int orderType, int orderID) //orderType=1 => buy; orderType=0 => sell; orderType=-1 => retract orders with given orderID
	{
		this.productName = productName;
		this.productQuantity = quantity;
		this.productPrice = price;
		this.orderType = orderType;
		this.orderID = orderID;
	}
	
	public Order(String productName, int quantity, double price, String orderType, int orderID) //orderType= {"buy", "sell", "cancel"}
	{
		this.productName = productName;
		this.productQuantity = quantity;
		this.productPrice = price;
		if(orderType.equals("buy")) this.orderType = 1;
		else if(orderType.equals("sell")) this.orderType = 0;
		else this.orderType = -1;
		this.orderID = orderID;
	}
	
	public int getOrderID()
	{
		return this.orderID;
	}
	
	public int getOrderType()
	{
		return this.orderType;
	}
	
	public String getProductName()
	{
		return this.productName;
	}
	
	public double getProductPrice()
	{
		return this.productPrice;
	}
	
	public int getProductQuantity()
	{
		return this.productQuantity;
	}
}