package com.caved_in.chatmanager.listeners;

import com.caved_in.chatmanager.ChatManager;
import com.caved_in.chatmanager.events.ChannelChatEvent;
import com.caved_in.chatmanager.events.ChannelJoinEvent;
import com.caved_in.chatmanager.events.ChannelLeaveEvent;
import com.caved_in.chatmanager.events.handler.ChannelEventHandler;
import com.caved_in.chatmanager.handlers.player.PlayerHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * User: Brandon
 */
public class BukkitListener implements Listener {
	public BukkitListener(JavaPlugin Plugin) {
		Plugin.getServer().getPluginManager().registerEvents(this, Plugin);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		final String playerName = event.getPlayer().getName();
		ChatManager.runnableManager.runTaskLaterAsynch(new Runnable() {
			@Override
			public void run() {
				ChannelLeaveEvent channelLeaveEvent = new ChannelLeaveEvent(ChatManager.channelHandler.getChannel(PlayerHandler.getData(playerName)
						.getChatChannel()), playerName);
				ChannelEventHandler.handleChannelLeaveEvent(channelLeaveEvent);
				PlayerHandler.removeData(playerName);
			}
		}, 10);
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		PlayerHandler.addData(event.getPlayer().getName());
		ChatManager.runnableManager.runTaskLaterAsynch(new Runnable() {

			@Override
			public void run() {
				ChannelJoinEvent channelJoinEvent = new ChannelJoinEvent(ChatManager.channelHandler.getChannel(PlayerHandler.getData(event.getPlayer().getName
						()).getChatChannel()), event.getPlayer());
				ChannelEventHandler.handleChannelJoinEvent(channelJoinEvent);
			}
		}, 10);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		ChannelChatEvent channelChatEvent = new ChannelChatEvent(ChatManager.channelHandler.getChannel(PlayerHandler.getData(event.getPlayer().getName())
				.getChatChannel()), event.getPlayer(), event.getMessage());
		ChannelEventHandler.handleChannelChatEvent(channelChatEvent);
	}
}
