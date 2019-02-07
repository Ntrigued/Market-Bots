package Agents.Implementations;

import Agents.InternetAgentInterface;
import Agents.InternetProviderInterface;
import Communication.Message;

public class UtilInternetAgent extends BasicUtilityAgent implements InternetAgentInterface
{
	private int ID;
	private InternetProviderInterface internet;
	
	public UtilInternetAgent(double balance, String[] products)
	{
		super(balance, products);
		this.ID = 0;
		this.internet = null;
	}

	public int getID()
	{
		return this.ID;
	}
	
	public void receiveID(int ID)
	{
		this.ID = ID;
	}
	
	public Message receiveMsg(Message msg) 
	{
		System.out.println(msg.getMessage());
		return new Message(this.ID, msg.getFromID(), 
				  		   "EXAMPLE_RESPONSE", "thanks for the message");
	}
	
	public Message sendMsg() {return null;}

	public void setInternetProvider(InternetProviderInterface internet) {
		this.internet = internet;
	}
}