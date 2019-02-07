package MarketBots.Agents.Implementations;

import MarketBots.Agents.InternetAgentInterface;
import MarketBots.Agents.InternetProviderInterface;
import MarketBots.Communication.Message;

public class AltUtilInternetAgent extends AltUtilityAgent implements InternetAgentInterface
{
	private int ID;
	private InternetProviderInterface internet;
	private Integer[] agentIDs;
	
	public AltUtilInternetAgent(double balance, String[] products)
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
		return null;
	}
	
	public Message sendMsg() 
	{
		for(int agentID : agentIDs)
		{
			InternetAgentInterface agent = this.internet.getAgentByID(agentID);
			if(agent != null && agent instanceof UtilInternetAgent)
			{
				System.out.println(agent.getClass());
				return new Message(this.ID, agent.getID(), "EXAMPLE_MESSAGE", "here's your message");
			}
		}
		
		return null;
	}

	public void setInternetProvider(InternetProviderInterface internet) {
		this.internet = internet;
		this.agentIDs = this.internet.getAgentIDs();
	}
}