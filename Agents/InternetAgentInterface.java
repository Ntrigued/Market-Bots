package MarketBots.Agents;

import MarketBots.Communication.Message;

public interface InternetAgentInterface extends AbstractAgentInterface
{
	
	public int getID();
	public void receiveID(int yourID);
	public Message receiveMsg(Message msg);
	public Message sendMsg();
	public void setInternetProvider(InternetProviderInterface internet);
	
}