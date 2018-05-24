package MarketBots.Agents.Implementations;

import MarketBots.Agents.BasicAgent;
import MarketBots.Agents.InternetAgentInterface;
import MarketBots.Agents.InternetProviderInterface;
import MarketBots.Communication.*;

public class BasicInternetAgent extends BasicAgent implements InternetAgentInterface
{
	private int ID;
	private InternetProviderInterface internet;
	
	public BasicInternetAgent(double balance, String[] products)
	{
		super(balance, products);
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
		return new Message(this.ID, msg.getFromID(), 
				  		   "EXAMPLE_RESPONSE", "thanks for the message");
	}
	
	public Message sendMsg() {return null;}

	public void setInternetProvider(InternetProviderInterface internet) {
		this.internet = internet;
	}
}
