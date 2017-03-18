import Agents.*;
import Communication.Message;
import Markets.*;
import Products.*;
import Testing.*;


public class Driver {

	public static void main(String[] args)
	{
		Output test = new Output(); //Testing class
		String[] productList = ProductList.getList();
		
		AbstractAgent[] agents = 
		{ //Place market agents in here
				new BasicUtilityAgent(10000, productList),
				new AltUtilityAgent(10000, productList),
				new BasicAgent(200, productList)
		};
		
		//Choose the type of market that the agents will interact in
		BasicCommunicatingMarket market = new BasicCommunicatingMarket(agents, productList, 10); //Runs market loop automatically
	}
}
