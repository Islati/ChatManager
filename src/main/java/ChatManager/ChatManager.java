package ChatManager;

import ChatManager.Events.ChannelJoinEvent;
import ChatManager.Events.Handler.ChannelEventHandler;
import ChatManager.Handlers.Player.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import ChatManager.Commands.CommandRegister;
import ChatManager.Handlers.Chat.ChannelHandler;
import ChatManager.Listeners.BukkitListener;
import ChatManager.Runnables.RunnableManager;

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
