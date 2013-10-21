package ChatManager.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ChatManager.Handlers.Chat.Channels.ChatChannel;

/**
 * User: Brandon
 */
public class ChannelCreateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private CommandSender channelCreator;
	private ChatChannel chatChannel;
	private boolean isCancelled = false;

	/**
	 * 
	 * @param chatChannel
	 * @param channelCreator
	 */
	public ChannelCreateEvent(ChatChannel chatChannel, CommandSender channelCreator)
	{
		super(false);
		this.channelCreator = channelCreator;
		this.chatChannel = chatChannel;
	}

	public CommandSender getCreator()
	{
		return this.channelCreator;
	}

	public ChatChannel getChatChannel()
	{
		return this.chatChannel;
	}
	
	public boolean isSenderPlayer()
	{
		return (this.channelCreator instanceof Player);
	}
	
	public boolean isSenderConsole()
	{
		return (this.channelCreator instanceof ConsoleCommandSender);
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
