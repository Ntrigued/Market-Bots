package Products;

public abstract class AbstractProduct {
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
	
	public void setDecayRate(double rate)
	{
		this.decayRate = rate;
	}
}
