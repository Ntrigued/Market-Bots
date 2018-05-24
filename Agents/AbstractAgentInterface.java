package MarketBots.Agents;

import MarketBots.Markets.*;

public interface AbstractAgentInterface 
{
	public void closingActions(); //called after market loop has completed
	public void decreaseBalance(double decreaseAmount);
	public double getBalance();
	public void setCurrentTrade(String productName, double price, int tradeVolume);
	public void increaseBalance(double increaseAmount);
	public void interact(); //Called during agent interaction stage
	public void otherActions(); //last method called in marketloop
	public void receive(String msg); // For sending free-form message to an agent
	public boolean receiveInventoryChange(String productName, int amountTraded, double balanceChange, int orderID); //Called if order has been filled
	public Order[] sendOrderDetails(); //Called for bid/offer stage
	public void setBalance(double balance);
	public void updateAccounting(); //called for update accounting stage
}
