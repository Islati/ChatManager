package ChatManager.Events;

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
public class ChannelChatEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private Player Sender;
	private String Message = "";
	private ChatChannel Channel;
	private boolean isCancelled = false;

	/**
	 * 
	 * @param Channel
	 * @param Sender
	 * @param Message
	 */
	public ChannelChatEvent(ChatChannel Channel, Player Sender, String Message)
	{
		super(true);
		this.Sender = Sender;
		this.Channel = Channel;
		this.Sender = Sender;
		this.Message = Message;
	}

	public Player getPlayer()
	{
		return this.Sender;
	}

	public ChatChannel getChannel()
	{
		return this.Channel;
	}

	public String getMessage()
	{
		return this.Message;
	}

	public void setMessage(String Message)
	{
		this.Message = Message;
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
