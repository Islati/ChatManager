package ChatManager.Handlers.Chat.Channels;

import java.util.*;

import ChatManager.Handlers.Chat.Formatting.*;
import ChatManager.Handlers.Player.PlayerHandler;

public class ChatChannel implements IChatChannel
{
    private String Channel_Name = "";
    private String Channel_Prefix = "";
    private ChatFormat ChatFormat = new ChatFormat("Default","%sender% - %message%");
    private List<String> Members = new ArrayList<String>();
    
    private boolean Private = false;
    
    private boolean Permanant = false;
    
    private boolean hasPermission = false;
    private String Permission = "";
    
    private boolean JoinLeaveMessages = true;
    
    private String ChannelCreator = "";
    

	public ChatChannel(String Channel_Name, String Channel_Prefix)
	{
		this.Channel_Name = Channel_Name;
		this.Channel_Prefix = Channel_Prefix;
	}
	
	public ChatChannel(String Channel_Name, String Channel_Prefix, String ChannelCreator, boolean isPrivate)
	{
		this.Channel_Name = Channel_Name;
		this.Channel_Prefix = Channel_Prefix;
		this.ChannelCreator = ChannelCreator;
		this.Private = isPrivate;
	}

    public ChatChannel(String Channel_Name, String Channel_Prefix, ChatFormat ChatFormat)
    {
        this.Channel_Name = Channel_Name;
	    this.Channel_Prefix = Channel_Prefix;
	    this.ChatFormat = ChatFormat;
    }
    
    public ChatChannel(String Channel_Name, String Channel_Prefix, ChatFormat ChatFormat, String ChannelCreator, boolean isPrivate)
    {
        this.Channel_Name = Channel_Name;
	    this.Channel_Prefix = Channel_Prefix;
	    this.ChatFormat = ChatFormat;
	    this.Private = isPrivate;
	    this.ChannelCreator = ChannelCreator;
    }
    
    public ChatChannel(String Channel_Name, String Channel_Prefix, ChatFormat ChatFormat, String Permission)
    {
        this.Channel_Name = Channel_Name;
	    this.Channel_Prefix = Channel_Prefix;
	    this.ChatFormat = ChatFormat;
	    if (!Permission.isEmpty())
	    {
	    	this.hasPermission = true;
	    	this.Permission = Permission;
	    }
    }
    
    public ChatChannel(String Channel_Name, String Channel_Prefix, ChatFormat ChatFormat,String ChannelCreator, boolean isPrivate, String Permission)
    {
        this.Channel_Name = Channel_Name;
	    this.Channel_Prefix = Channel_Prefix;
	    this.ChatFormat = ChatFormat;
	    this.Private = isPrivate;
	    this.ChannelCreator = ChannelCreator;
	    if (!Permission.isEmpty())
	    {
	    	this.hasPermission = true;
	    	this.Permission = Permission;
	    }
    }

	@Override
    public String getName()
    {
        return this.Channel_Name;
    }
    
    @Override
    public String getPrefix()
    {
        return this.Channel_Prefix;
    }

	@Override
	public void setName(String Name)
	{
		this.Channel_Name = Name;
	}

	@Override
	public void setPrefix(String Name)
	{
		this.Channel_Prefix = Name;
	}

	@Override
    public List<String> getMembers()
    {
        return this.Members;
    }
    
    @Override
    public void addMember(String Name)
    {
        if (!isMember(Name))
        {
            this.Members.add(Name);
        }
    }
    
    @Override
    public void removeMember(String Name)
    {
        if (isMember(Name))
        {
            this.Members.remove(Name);
        }
    }
    
    @Override
    public boolean isMember(String Name)
    {
        return this.Members.contains(Name);
    }
    
    @Override
    public void sendToMembers(String Sender, String Message)
    {
        for(String Member : this.getMembers())
        {
            if (PlayerHandler.isOnline(Member))
            {
                PlayerHandler.getPlayer(Member).sendMessage(this.getPrefix() + this.ChatFormat.getFormattedMessage(Sender,Message));
            }
        }
    }
    
    public void sendToMembers(String Message)
    {
        for(String Member : this.getMembers())
        {
            if (PlayerHandler.isOnline(Member))
            {
                PlayerHandler.getPlayer(Member).sendMessage(this.getPrefix() + Message);
            }
        }
    }

	@Override
	public boolean allowJoinLeaveMessages()
	{
		return this.JoinLeaveMessages;
	}

	@Override
	public String getPermission()
	{
		return this.Permission;
	}

	@Override
	public boolean hasPermission()
	{
		return this.hasPermission;
	}

	@Override
	public boolean isPrivate()
	{
		return this.Private;
	}

	@Override
	public void setPermission(String Permission)
	{
		this.Permission = Permission;
		this.hasPermission = !Permission.isEmpty();
	}

	@Override
	public void setJoinLeaveMessages(boolean Messages)
	{
		this.JoinLeaveMessages = Messages;
	}

	@Override
	public void setPrivate(boolean Private)
	{
		this.Private = Private;
	}

	@Override
	public String getCreator()
	{
		return this.ChannelCreator;
	}

	@Override
	public void setCreator(String Creator)
	{
		this.ChannelCreator = Creator;
	}

	@Override
	public boolean isPermanant()
	{
		return this.Permanant;
	}

	@Override
	public void setPermanant(boolean Permanant)
	{
		this.Permanant = Permanant;
	}
}
