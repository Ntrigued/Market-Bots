import java.util.*;

import Agents.*;
import Agents.Implementations.AltUtilInternetAgent;
import Agents.Implementations.UtilInternetAgent;
import Markets.*;
import Products.*;

public class Driver {

	public static void main(String[] args)
	{
		///////////////////////////////////////////////////////////////////////
		//                                                                   //
		// Products (defined in Products) used in this simulation //
		// 		Used in argument for agent classes                           //
		//                                                                   //
		///////////////////////////////////////////////////////////////////////
		String[] productList = new String[]
				{
					"BasicBond"
				};
		
		//////////////////////////////////////////////////////////
		//                                                      //
		// Place all agents in this array                       //
		// Except the Internet Provider                         //
		//                                                      //
		//////////////////////////////////////////////////////////
		AbstractAgentInterface[] agentArray = new AbstractAgentInterface[]
				{
					new InfoGatherAgent(productList), //Gathers Trade history data
					// Etc... 
				};
		
		ArrayList<AbstractAgentInterface> nonInternetAgents = new ArrayList<AbstractAgentInterface>();
		for(AbstractAgentInterface agent : agentArray)
		{
			nonInternetAgents.add(agent);
		}
		
		ArrayList<InternetAgentInterface> internetAgentsList = new ArrayList<InternetAgentInterface>();
		for(AbstractAgentInterface agent : nonInternetAgents)
			if(agent instanceof InternetAgentInterface)
				internetAgentsList.add((InternetAgentInterface) agent);
		InternetAgentInterface[] internetAgents = new InternetAgentInterface[internetAgentsList.size()];
		for(int i = 0; i < internetAgentsList.size(); i++)
			internetAgents[i] = internetAgentsList.get(i);
		
		//////////////////////////////////////////////////////////////////////////////////
		//                                                                              //
		// Place Internet Provider here                                                 //
		//	- with internetAgents as all agents which  implement InternetAgentInterface //
		//                                                                              //
		//////////////////////////////////////////////////////////////////////////////////
		InternetProviderInterface internetProvider = new BasicInternetProviderAgent(internetAgents);
		
		AbstractAgentInterface[] agents = new AbstractAgentInterface[1 + nonInternetAgents.size()];
		for(int i = 0; i < agents.length - 1; i++)
		{
			agents[i] = nonInternetAgents.get(i);
		}
		agents[agents.length - 1] = internetProvider;
				
		//////////////////////////////////////////////////////////////////////
		//                                                                  //                  
		// Choose the type of market that the agents will interact in       //
		//                                                                  //
		//////////////////////////////////////////////////////////////////////
		BasicCommunicatingMarket market = new BasicCommunicatingMarket(agents, productList);
		
		///////////////////////////////////////////
		//                                       //
		// run time steps here                   //
		// and perform inter-time step actions   //
		//                                       //
		///////////////////////////////////////////
		market.runTimeSteps(500);
	 // market.addAgent( new BasicAgent(1000, productList) );
	 // market.runTimeSteps(500);
		
		market.close();
	}
}
