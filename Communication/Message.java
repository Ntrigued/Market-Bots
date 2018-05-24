package MarketBots.Communication;

public class Message 
{
	private int fromID, toID;
	private String msgType, message;
	
	public Message(int fromID, int toID, String msgType, String message)
	{
		this.fromID  = fromID;
		this.message = message;
		this.msgType = msgType;
		this.toID    = toID;
	}
	
	public int getFromID()
	{
		return this.fromID;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public String getMsgType()
	{
		return this.msgType;
	}
	
	public int getToID()
	{
		return this.toID;
	}
}
