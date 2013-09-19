package ChatManager.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ChatManager.ChatManager;
import ChatManager.Events.*;
import ChatManager.Events.Handler.ChannelEventHandler;
import ChatManager.Handlers.Player.PlayerHandler;

/**
 * User: Brandon
 */
public class BukkitListener implements Listener
{
	public BukkitListener(JavaPlugin Plugin)
	{
		Plugin.getServer().getPluginManager().registerEvents(this,Plugin);
	}
	
	@EventHandler
    public void PlayerQuit(PlayerQuitEvent Event)
    {
		final String PlayerName = Event.getPlayer().getName();
		ChatManager.RunnableManager.RunTaskLaterAsynch(new Runnable()
	    {
			@Override
			public void run()
			{
				ChannelLeaveEvent ChannelLeave = new ChannelLeaveEvent(ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(PlayerName).getChatChannel()),PlayerName);
				ChannelEventHandler.HandleChannelLeaveEvent(ChannelLeave);
			    PlayerHandler.removeData(PlayerName);
			}
	    }, 10);
    }
	
	@EventHandler
	public void PlayerJoin(final PlayerJoinEvent Event)
	{
		PlayerHandler.addData(Event.getPlayer().getName());
		ChatManager.RunnableManager.RunTaskLaterAsynch(new Runnable() {

			@Override
			public void run()
			{
				ChannelJoinEvent jEvent = new ChannelJoinEvent(ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(Event.getPlayer().getName()).getChatChannel()),Event.getPlayer());
				ChannelEventHandler.HandleChannelJoinEvent(jEvent);
			} }, 40);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerChat(AsyncPlayerChatEvent Event)
	{
		Event.setCancelled(true);
		ChannelChatEvent ChannelEvent = new ChannelChatEvent(ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(Event.getPlayer().getName()).getChatChannel()),Event.getPlayer(),Event.getMessage());
		ChannelEventHandler.HandleChannelChatEvent(ChannelEvent);
	}
}
