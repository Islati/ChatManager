package ChatManager.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;

import ChatManager.Handlers.Chat.Channels.ChatChannel;

/**
 * User: Brandon
 */
public class ChannelJoinEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private Player Player;
	private ChatChannel Channel;
	private boolean isCancelled = false;

	/**
	 * 
	 * @param Channel
	 * @param Player
	 */
	public ChannelJoinEvent(ChatChannel Channel, Player Player)
	{
		super(false);
		this.Player = Player;
		this.Channel = Channel;
	}

	public Player getPlayer()
	{
		return this.Player;
	}

	public ChatChannel getChannel()
	{
		return this.Channel;
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
