package ChatManager;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import ChatManager.Commands.CommandRegister;
import ChatManager.Handlers.Chat.ChannelHandler;
import ChatManager.Handlers.Chat.Channels.*;
import ChatManager.Listeners.BukkitListener;
import ChatManager.Listeners.ChatManagerListener;
import ChatManager.Runnables.RunnableManager;

public class ChatManager extends JavaPlugin
{
	public static ChannelHandler ChannelHandler = new ChannelHandler();
	public static final String GLOBAL_CHAT_CHANNEL = "GLOBAL";
	public static RunnableManager RunnableManager;
	
	@Override
	public void onEnable()
	{
		RunnableManager = new RunnableManager(this);
		new BukkitListener(this);
		new ChatManagerListener(this);
		new CommandRegister(this);
	}
	
	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
	}
}
