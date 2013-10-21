package ChatManager.Events;

import org.bukkit.Bukkit;
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
public class ChannelDeleteEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private CommandSender channelDeleter;
	private ChatChannel chatChannel;
	private boolean isCancelled = false;
	private ChannelDeleteReason deleteReason;

	/**
	 * 
	 * @param chatChannel
	 * @param channelDeleter
	 */
	public ChannelDeleteEvent(ChatChannel chatChannel, CommandSender channelDeleter)
	{
		super(false);
		this.channelDeleter = (channelDeleter == null) ? Bukkit.getConsoleSender() : channelDeleter;
		this.chatChannel = chatChannel;
		if (chatChannel.getChatMembers().size() <= 0)
		{
			this.deleteReason = ChannelDeleteReason.EMPTY;
		}
		else
		{
			if (channelDeleter instanceof Player)
			{
				Player player = (Player) channelDeleter;
				if (!chatChannel.getCreator().equalsIgnoreCase(player.getName()))
				{
					this.deleteReason = ChannelDeleteReason.DELETED_BY_STAFF;
				}
				else
				{
					this.deleteReason = ChannelDeleteReason.DELETED_BY_OWNER;
				}
			}
			else
			{
				this.deleteReason = ChannelDeleteReason.OTHER;
			}
		}
	}
	
	public enum ChannelDeleteReason
	{
		EMPTY,
		DELETED_BY_OWNER,
		DELETED_BY_STAFF,
		OTHER
	}

	public CommandSender getDeleter()
	{
		return this.channelDeleter;
	}
	
	public boolean isSenderPlayer()
	{
		return (this.channelDeleter instanceof Player);
	}
	
	public boolean isSenderConsole()
	{
		return (this.channelDeleter instanceof ConsoleCommandSender);
	}

	public ChatChannel getChatChannel()
	{
		return this.chatChannel;
	}
	
	public ChannelDeleteReason getDeleteReason()
	{
		return this.deleteReason;
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
