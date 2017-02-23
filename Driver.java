import Agents.*;
import Markets.*;
import Products.*;
import Testing.*;

/* Sample, extremely simple market */
public class Driver {

	public static void main(String[] args)
	{
		Output test = new Output();
		String[] productList = ProductList.getList();
		
		BasicAgent[] agents = 
		{
				new BasicAgent(100000000, productList),
				new BasicAgent(337, productList)
		};
		
		BasicMarket market = new BasicMarket(agents, productList, 10);
	}
}
