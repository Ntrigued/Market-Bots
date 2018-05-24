package MarketBots.Agents;

import java.util.*;

import MarketBots.Communication.Message;

public class BasicInternetProviderAgent extends AbstractAgent 
										implements InternetProviderInterface
{
	Map<Integer, InternetAgentInterface> agentIDMap;
	
	public BasicInternetProviderAgent(InternetAgentInterface[] agents)
	{
		this.agentIDMap = new HashMap<Integer, InternetAgentInterface>();
		
		int currentID = 1;
		for(InternetAgentInterface agent : agents)
		{
			this.agentIDMap.put(currentID, agent);
			currentID++;
		}
		for(int ID : this.agentIDMap.keySet())
		{
			InternetAgentInterface agent = this.agentIDMap.get(ID);
			agent.receiveID(ID);
			agent.setInternetProvider(this);
		}
	}
	
	public InternetAgentInterface getAgentByID(int agentID)
	{
		return this.agentIDMap.get(agentID);
	}
	
	public Integer[] getAgentIDs()
	{
		Integer[] x = new Integer[0];
		return this.agentIDMap.keySet().toArray(x);
	}
	
	public void receiveMsg(Message msg)
	{
		if(msg == null) return;
		
		InternetAgentInterface toAgent = this.agentIDMap.get(msg.getToID());
		Message returnedMsg = toAgent.receiveMsg(msg);
		if(returnedMsg != null) receiveMsg(returnedMsg);
	}
	
	@Override
	public void otherActions()
	{
		for(InternetAgentInterface agent : this.agentIDMap.values())
		{
			this.receiveMsg(agent.sendMsg());
		}
	}

	public void decreaseBalance(double decreaseAmount) {
		// TODO Auto-generated method stub
		
	}

	public void increaseBalance(double increaseAmount) {
		// TODO Auto-generated method stub
		
	}

	public boolean receiveInventoryChange(String productName, int amountTraded,
			double balanceChange, int orderID) 
	{
		return true;
	}
}
