package com.caved_in.chatmanager.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.caved_in.chatmanager.handlers.chat.channels.ChatChannel;

/**
 * User: Brandon
 */
public class ChannelLeaveEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private String Player;
	private ChatChannel chatChannel;
	private boolean isCancelled = false;

	/**
	 * 
	 * @param chatChannel
	 * @param player
	 */
	public ChannelLeaveEvent(ChatChannel chatChannel, Player player)
	{
		super(false);
		this.Player = player.getName();
		this.chatChannel = chatChannel;
	}
	
	public ChannelLeaveEvent(ChatChannel chatChannel, String playerName)
	{
		this.chatChannel = chatChannel;
		this.Player = playerName;
	}

	public String getPlayer()
	{
		return this.Player;
	}

	public ChatChannel getChatChannel()
	{
		return this.chatChannel;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public boolean isCancelled()
	{
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean Cancel)
	{
		this.isCancelled = Cancel;
	}
}
