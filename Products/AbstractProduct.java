package MarketBots.Products;

public abstract class AbstractProduct {
	static int timeSteps = 0;
	
	double decayRate; //simulates depreciation of assets
	private String name;
	
	public AbstractProduct(String name)
	{
		this.name = name;
	}
	
	public double getDecayRate()
	{
		return this.decayRate;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public static int getTimeStep()
	{
		return timeSteps;
	}
	
	// Informs product that a time step has occurred
	public static void registerTimeStep()
	{	
		timeSteps++;
	}
	
	public void setDecayRate(double rate)
	{
		this.decayRate = rate;
	}
}