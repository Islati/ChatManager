package ChatManager.Handlers.Chat.Formatting.Variables;

public class ChatVariable implements IChatVariable
{
    private String chatVariable = "";
	private String variableReplace = "";
	private boolean isReplaceable = false;

    public ChatVariable(String chatVariable)
    {
        this.chatVariable = chatVariable;
    }

	public ChatVariable(String chatVariable, boolean isReplaceable, String variableReplace)
	{
		this.chatVariable = chatVariable;
		this.isReplaceable = isReplaceable;
		this.variableReplace = variableReplace;
	}

    @Override
    public String getChatVariable()
    {
        return this.chatVariable;
    }

    @Override
    public String getVariableReplace()
    {
	    if (isReplaceable())
	    {
		    return this.variableReplace;
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
