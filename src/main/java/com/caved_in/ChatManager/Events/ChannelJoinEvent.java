package com.caved_in.chatmanager.events;

import com.caved_in.chatmanager.handlers.chat.channels.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * User: Brandon
 */
public class ChannelJoinEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private ChatChannel chatChannel;
	private boolean isCancelled = false;

	/**
	 * @param chatChannel
	 * @param player
	 */
	public ChannelJoinEvent(ChatChannel chatChannel, Player player) {
		super(false);
		this.player = player;
		this.chatChannel = chatChannel;
	}

	public Player getPlayer() {
		return this.player;
	}

	public ChatChannel getChatChannel() {
		return this.chatChannel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean Cancel) {
		this.isCancelled = Cancel;
	}
}
