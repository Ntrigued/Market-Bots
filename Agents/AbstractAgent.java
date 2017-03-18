package Agents;

import Markets.Order;

public abstract class AbstractAgent {
	
	private double balance;
	
	public void closingActions() {} //called after market loop has completed
	public double getBalance() { return this.balance; }
	public void setCurrentPrice(String productName, double price) {}
	public void interact() {} //Called during agent interaction stage
	public void otherActions() {} //last method called in marketloop
	public boolean receiveInventoryChange(String productName, int amountTraded, double balanceChange) { return true; } //Called if order has been filled
	public Order[] sendOrderDetails() { return new Order[1]; } //Called for bid/offer stage
	public void setBalance(double balance) { this. balance = balance; }
	public void updateAccounting() {} //called for update accounting stage
}
