package com.caved_in.chatmanager.listeners;

import com.caved_in.chatmanager.events.ChannelChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatManagerListener implements Listener
{
	public ChatManagerListener(JavaPlugin Plugin)
	{
		Plugin.getServer().getPluginManager().registerEvents(this, Plugin);
	}

	//@EventHandler /*Disabled; It was for debugging, will still be used while testing */
	public void onChannelChat(ChannelChatEvent Event)
	{
		Bukkit.getLogger().info("User [" + Event.getPlayer().getName() + "] Chatting in channel [" + Event.getChatChannel().getName() + "] with message \"" + Event.getMessage() + "\"");
	}
}
