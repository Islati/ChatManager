package com.caved_in.chatmanager.handlers.chat.channels;

import com.caved_in.chatmanager.ChatManager;

public class ChannelInvitation
{
	private String invitingPlayer = "";
	private String invitedPlayer = "";
	private String chatChannel = "";
	
	public ChannelInvitation(String chatChannel, String invitingPlayer, String invitedPlayer)
	{
		this.chatChannel = chatChannel;
		this.invitingPlayer = invitingPlayer;
		this.invitedPlayer = invitedPlayer;
	}
	
	public ChatChannel getChatChannel()
	{
		return (ChatManager.channelHandler.getChannel(this.chatChannel));
	}
	
	public String getChannelName()
	{
		return this.chatChannel;
	}
	
	public String getInvitingPlayer()
	{
		return this.invitingPlayer;
	}
	
	public String getInvitedPlayer()
	{
		return this.invitedPlayer;
	}
}
