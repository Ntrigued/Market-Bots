package MarketBots.Testing;

public class Output {

	static boolean testing = true;
	
	public static void errorln(Object s)
	{
		if(testing) System.err.println(s);
	}
	
	public static void error(Object s)
	{
		if(testing) System.err.print(s);
	}
	
	public static void println(Object s)
	{
		System.out.println(s);
	}
	
	public static void print(Object s)
	{
		System.out.print(s);
	}
	
	public static void test(Object s)
	{
		if(testing) System.out.print(s);
	}

	public static void testln(Object s)
	{
		if(testing) System.out.println(s);
	}
}