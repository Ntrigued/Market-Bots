package Agents;

/**
 * Allows an instance of AbstractMarket to inform an agent that a trade, which they weren't involved in, has completed.
 * @author grafs50
 *
 */
public interface MemoryAgentInterface 
{
	public void setCurrentTrade(String productName, double price, int tradeVolume);
}
