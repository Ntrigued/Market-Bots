package MarketBots.Agents;

import MarketBots.Communication.Message;

public interface InternetProviderInterface extends AbstractAgentInterface
{
	public InternetAgentInterface getAgentByID(int ID);
	public Integer[] getAgentIDs();
	public void receiveMsg(Message msg);
}
