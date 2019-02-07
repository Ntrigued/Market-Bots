package MarketBots.Agents;

import java.util.*;

import MarketBots.Testing.Output;

public class InfoGatherAgent extends BasicMemoryAgent
{
	Map<String, List<Double[]>> tradeHistoryMap;
	
	public InfoGatherAgent(String[] products)
	{
		super(0, products);
		this.tradeHistoryMap = new HashMap<String, List<Double[]>>(); //Maps: productname --> [prices, tradevolumes]
		for(String productName : this.getCurrentPriceMap().keySet())
			tradeHistoryMap.put(productName, new ArrayList<Double[]>());
	}
	
	public InfoGatherAgent(double balance, String[] products) {
		super(balance, products);
		this.tradeHistoryMap = new HashMap<String, List<Double[]>>(); //Maps: productname --> [prices, tradevolumes]
		for(String productName : this.getCurrentPriceMap().keySet())
			tradeHistoryMap.put(productName, new ArrayList<Double[]>());
	}

	public List<Double[]> getTradeHistory(String productName)
	{
		return this.tradeHistoryMap.get(productName);
	}
	
	public void printTradeHistory(String product)
	{
		List<Double[]> tradeHistory = this.tradeHistoryMap.get(product);
		for(Double[] trade : tradeHistory)
		{
			Output.println(trade[0] + " " + trade[1]);
		}
	}
	
	public void updateAccounting()
	{
		Map<String, Double>  priceMap = this.getCurrentPriceMap();
		Map<String, Integer> volumeMap = this.getCurrentVolumeMap();
		for(String productName : priceMap.keySet())
		{
			Double[] trade_info = new Double[]{priceMap.get(productName), new Double(volumeMap.get(productName))};
			List<Double[]> history = tradeHistoryMap.get(productName);
			history.add(trade_info);
			tradeHistoryMap.put(productName, history);
		}
	}
}
