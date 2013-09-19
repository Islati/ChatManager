package ChatManager.Handlers.Chat.Formatting.Variables;

public class ChatVariable implements IChatVariable
{
    private String Variable = "";
	private String Replaces = "";
	private boolean isReplaceable = false;

    public ChatVariable(String Variable)
    {
        this.Variable = Variable;
    }

	public ChatVariable(String Variable, boolean isReplaceable, String Replaces)
	{
		this.Variable = Variable;
		this.isReplaceable = isReplaceable;
		this.Replaces = Replaces;
	}

    @Override
    public String getVariable()
    {
        return this.Variable;
    }

    @Override
    public String getReplaces()
    {
	    if (isReplaceable())
	    {
		    return this.Replaces;
	    }
	    else
	    {
		    return "";
	    }
    }

    @Override
    public boolean isReplaceable()
    {
        return this.isReplaceable;
    }
}
