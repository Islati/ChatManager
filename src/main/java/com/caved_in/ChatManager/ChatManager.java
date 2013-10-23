package com.caved_in.chatmanager;

import com.caved_in.chatmanager.config.Configuration;
import com.caved_in.chatmanager.events.ChannelJoinEvent;
import com.caved_in.chatmanager.events.handler.ChannelEventHandler;
import com.caved_in.chatmanager.handlers.chat.channels.ChatChannel;
import com.caved_in.chatmanager.handlers.player.PlayerHandler;
import com.caved_in.chatmanager.commands.CommandRegister;
import com.caved_in.chatmanager.handlers.chat.ChannelHandler;
import com.caved_in.chatmanager.handlers.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.caved_in.chatmanager.listeners.BukkitListener;
import com.caved_in.chatmanager.runnables.RunnableManager;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;

public class ChatManager extends JavaPlugin
{
	public static ChannelHandler channelHandler = new ChannelHandler();
	public static final String GLOBAL_CHAT_CHANNEL = "Global";
	public static RunnableManager runnableManager;
	public static Configuration channelConfig;

	private static String multichatFolder = "plugins/MultiChat";

	@Override
	public void onEnable()
	{
		//Verify the configuration folder + file exists, if not then create them.
		configVerify();
		//Load the xml configuration
		loadXmlConfig();
		//Instance variables, commands, listeners, and handlers
		runnableManager = new RunnableManager(this);
		new BukkitListener(this);
		//new ChatManagerListener(this);
		new CommandRegister(this);

		//Load all the data asynchronously for the currently online players
		for (Player player : Bukkit.getOnlinePlayers())
		{
			final Player cPlayer = player;
			final String playerName = cPlayer.getName();

			//Add data for the player
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
			}, 10);
		}
	}

	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
		Bukkit.getServer().getScheduler().cancelTasks(this);
	}

	private static boolean configVerify()
	{
		try
		{
			File configFolder = new File(multichatFolder);
			File configFile = new File(multichatFolder + "/Config.xml");
			//Config folder doesn't exist
			if (!configFolder.exists())
			{
				//Make default config folder
				configFolder.mkdir();
			}

			if (!configFile.exists())
			{
				//Save default config
				configFile.createNewFile();
				Serializer serializer = new Persister();
				serializer.write(new Configuration(),configFile);
				Bukkit.getConsoleSender().sendMessage(StringUtil.formatColorCodes("&bDefault MultiChat configuration has been created at &eplugins/MultiChat/Config.xml"));
			}
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean loadXmlConfig()
	{
		try
		{
			configVerify();
			ChatManager.channelHandler.cleanChannels();
			Serializer serializer = new Persister();
			channelConfig = serializer.read(Configuration.class,new File(multichatFolder + "/Config.xml"));
			for (ChatChannel chatChannel : channelConfig.getChatChannels())
			{
				channelHandler.addChannel(chatChannel);
			}
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Loaded " + channelConfig.getXmlChatChannels().size() + " chat channels");
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean saveXmlConfig()
	{
		try
		{
			configVerify();
			Serializer serializer = new Persister();
			serializer.write(channelConfig,new File(multichatFolder + "/Config.xml"));
			Bukkit.getConsoleSender().sendMessage(StringUtil.formatColorCodes("&aConfiguration saved"));
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
