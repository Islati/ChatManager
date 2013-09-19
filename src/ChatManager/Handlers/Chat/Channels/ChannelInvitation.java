package ChatManager.Handlers.Chat.Channels;

public class ChannelInvitation
{
	private String Inviter = "";
	private String Invited = "";
	private String Channel = "";
	
	public ChannelInvitation(String Channel, String Inviter, String Invited)
	{
		this.Channel = Channel;
		this.Inviter = Inviter;
		this.Invited = Invited;
	}
	
	public ChatChannel getChannel()
	{
		return (ChatManager.ChatManager.ChannelHandler.getChannel(this.Channel));
	}
	
	public String getChannelName()
	{
		return this.Channel;
	}
	
	public String getInviter()
	{
		return this.Inviter;
	}
	
	public String getInvited()
	{
		return this.Invited;
	}
}
