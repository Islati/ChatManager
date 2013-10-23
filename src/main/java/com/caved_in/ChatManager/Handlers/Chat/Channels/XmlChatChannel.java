package com.caved_in.chatmanager.handlers.chat.channels;

import com.caved_in.chatmanager.commands.CommandPermissions;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ChatChannel")
public class XmlChatChannel implements IChatChannel
{
	@Element(name = "channelName")
	private String channelName = "Example";

	@Element(name = "prefix",required = false)
	private String channelPrefix = "";

	@Element(name = "join_leave_messages")
	private boolean hasJoinLeaveMessages = true;

	@Element(name = "permission")
	private String channelPermission = CommandPermissions.CHANNEL_JOIN_PERMISSION;

	@Element(name = "creator")
	private String channelCreator = "Console";

	@Element(name = "chat_format")
	private String channelChatFormat = "%sender%: %message%";

	private boolean isPermanent = true;

	public XmlChatChannel(@Element(name = "channelName") String channelName,
						  @Element(name = "prefix",required = false) String channelPrefix,
						  @Element(name = "join_leave_messages") boolean hasJoinLeaveMessages,
						  @Element(name = "permission") String channelPermission,
						  @Element(name = "creator") String channelCreator)
	{
		this.channelName = channelName;
		this.channelPrefix = channelPrefix;
		this.hasJoinLeaveMessages = hasJoinLeaveMessages;
		this.channelPermission = channelPermission;
		this.channelCreator = channelCreator;
	}

	public XmlChatChannel(ChatChannel chatChannel)
	{
		this.channelCreator = chatChannel.getCreator();
		this.channelPrefix = chatChannel.getPrefix();
		this.hasJoinLeaveMessages = chatChannel.allowJoinLeaveMessages();
		this.channelPermission = chatChannel.getChannelPermission();
		this.channelName = chatChannel.getName();
	}

	public XmlChatChannel()
	{

	}

	public ChatChannel getChatChannel()
	{
		ChatChannel chatChannel = new ChatChannel(this.channelName, (this.channelPrefix == null || this.channelPrefix.equals("")) ? "[" + this.channelName + "]" : this.channelPrefix);
		chatChannel.setChannelPermission(this.channelPermission);
		chatChannel.setHasJoinLeaveMessages(this.hasJoinLeaveMessages);
		chatChannel.setCreator(this.channelCreator);
		chatChannel.setChatFormat(this.channelChatFormat);
		chatChannel.setPermanent(true);
		return chatChannel;
	}

	@Override
	public String getName()
	{
		return this.channelName;
	}

	@Override
	public String getPrefix()
	{
		return this.channelPrefix;
	}

	@Override
	public void setName(String Name)
	{
		this.channelName = Name;
	}

	@Override
	public void setPrefix(String Name)
	{
		this.channelPrefix = Name;
	}

	@Override
	public List<String> getChatMembers()
	{
		return null;
	}

	@Override
	public void addMember(String Name)
	{

	}

	@Override
	public void removeMember(String Name)
	{
	}

	@Override
	public boolean isMember(String Name)
	{
		return false;
	}

	@Override
	public boolean allowJoinLeaveMessages()
	{
		return this.hasJoinLeaveMessages;
	}

	@Override
	public String getChannelPermission()
	{
		return this.channelPermission;
	}

	@Override
	public boolean hasPermission()
	{
		return !(this.channelPermission.equalsIgnoreCase(CommandPermissions.CHANNEL_JOIN_PERMISSION));
	}

	@Override
	public boolean isPrivate()
	{
		return false;
	}

	@Override
	public void setChannelPermission(String Permission)
	{
		this.channelPermission = Permission;
	}

	@Override
	public void setHasJoinLeaveMessages(boolean Messages)
	{
		this.hasJoinLeaveMessages = Messages;
	}

	@Override
	public void setPrivate(boolean Private)
	{
	}

	@Override
	public void sendToMembers(String Sender, String Message)
	{
	}

	@Override
	public String getCreator()
	{
		return this.channelCreator;
	}

	@Override
	public void setCreator(String channelCreator)
	{
		this.channelCreator = channelCreator;
	}

	@Override
	public boolean isPermanent()
	{
		return true;
	}

	@Override
	public void setPermanent(boolean Permanant)
	{
		this.isPermanent = true;
	}
}
