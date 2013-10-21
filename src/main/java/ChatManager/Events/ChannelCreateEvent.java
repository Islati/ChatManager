package ChatManager.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
public class ChannelCreateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private CommandSender Sender;
	private ChatChannel Channel;
	private boolean isCancelled = false;

	/**
	 * 
	 * @param Channel
	 * @param Sender
	 */
	public ChannelCreateEvent(ChatChannel Channel, CommandSender Sender)
	{
		super(false);
		this.Sender = Sender;
		this.Channel = Channel;
	}

	public CommandSender getCreator()
	{
		return this.Sender;
	}

	public ChatChannel getChannel()
	{
		return this.Channel;
	}
	
	public boolean isSenderPlayer()
	{
		return (this.Sender instanceof Player);
	}
	
	public boolean isSenderConsole()
	{
		return (this.Sender instanceof ConsoleCommandSender);
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
