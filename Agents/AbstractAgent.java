package MarketBots.Agents;

import MarketBots.Markets.Order;

public abstract class AbstractAgent implements AbstractAgentInterface{
	
	private double balance;
	
	public void closingActions() {} //called after market loop has completed
	public double getBalance() { return this.balance; }
	public void setCurrentTrade(String productName, double price, int tradeAmount) {}
	public void interact() {} //Called during agent interaction stage
	public void otherActions() {} //last method called in marketloop
	public void receive(String msg) {} // For sending free-form message to an agent
	public boolean receiveInventoryChange(String productName, int amountTraded, double balanceChange) { return true; } //Called if order has been filled
	public Order[] sendOrderDetails() { return new Order[0]; } //Called for bid/offer stage
	public void setBalance(double balance) { this. balance = balance; }
	public void updateAccounting() {} //called for update accounting stage
}
