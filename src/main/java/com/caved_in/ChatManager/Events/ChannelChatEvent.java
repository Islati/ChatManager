package com.caved_in.chatmanager.events;

import com.caved_in.chatmanager.handlers.chat.channels.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * User: Brandon
 */
public class ChannelChatEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private String message = "";
	private ChatChannel chatChannel;
	private boolean isCancelled = false;

	/**
	 * @param chatChannel
	 * @param player
	 * @param message
	 */
	public ChannelChatEvent(ChatChannel chatChannel, Player player, String message) {
		super(true);
		this.player = player;
		this.chatChannel = chatChannel;
		this.player = player;
		this.message = message;
	}

	public Player getPlayer() {
		return this.player;
	}

	public ChatChannel getChatChannel() {
		return this.chatChannel;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String Message) {
		this.message = Message;
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
