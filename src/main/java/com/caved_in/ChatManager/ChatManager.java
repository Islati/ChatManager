package com.caved_in.chatmanager;

import com.caved_in.chatmanager.events.ChannelJoinEvent;
import com.caved_in.chatmanager.events.Handler.ChannelEventHandler;
import com.caved_in.chatmanager.handlers.player.PlayerHandler;
import com.caved_in.chatmanager.commands.CommandRegister;
import com.caved_in.chatmanager.handlers.chat.ChannelHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.caved_in.chatmanager.listeners.BukkitListener;
import com.caved_in.chatmanager.runnables.RunnableManager;

public class ChatManager extends JavaPlugin
{
	public static ChannelHandler channelHandler = new ChannelHandler();
	public static final String GLOBAL_CHAT_CHANNEL = "GLOBAL";
	public static RunnableManager runnableManager;
	
	@Override
	public void onEnable()
	{
		runnableManager = new RunnableManager(this);
		new BukkitListener(this);
		//new ChatManagerListener(this);
		new CommandRegister(this);

		//Load all the data asynchronously for the currently online players
		for(Player player : Bukkit.getOnlinePlayers())
		{
			final Player cPlayer = player;
			final String playerName = cPlayer.getName();
			PlayerHandler.addData(playerName);

			//Asynchronous thread to call the channel join for each player
			ChatManager.runnableManager.runTaskLaterAsynch(new Runnable()
			{
				@Override
				public void run()
				{
					ChannelJoinEvent channelJoinEvent = new ChannelJoinEvent(ChatManager.channelHandler.getChannel(PlayerHandler.getData(playerName).getChatChannel()), cPlayer);
					ChannelEventHandler.handleChannelJoinEvent(channelJoinEvent);
				}
			}, 40);
		}
	}
	
	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
		Bukkit.getServer().getScheduler().cancelTasks(this);
	}
}
