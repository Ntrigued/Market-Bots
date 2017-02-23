package Testing;

public class Output {

	boolean testing = true;
	
	public void println(Object s)
	{
		if(testing) System.out.println(s);
	}
	
	public void print(Object s)
	{
		if(testing) System.out.print(s);
	}
	
	public void errorln(Object s)
	{
		if(testing) System.err.println(s);
	}
	
	public void error(Object s)
	{
		if(testing) System.err.print(s);
	}
}
