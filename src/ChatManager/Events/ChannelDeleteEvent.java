package ChatManager.Events;

import org.bukkit.Bukkit;
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
public class ChannelDeleteEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private CommandSender Sender;
	private ChatChannel Channel;
	private boolean isCancelled = false;
	private ChannelDeleteReason DeleteReason;

	/**
	 * 
	 * @param Channel
	 * @param Sender
	 */
	public ChannelDeleteEvent(ChatChannel Channel, CommandSender Sender)
	{
		super(false);
		this.Sender = (Sender == null) ? Bukkit.getConsoleSender() : Sender; 
		this.Channel = Channel;
		if (Channel.getMembers().size() <= 0)
		{
			this.DeleteReason = ChannelDeleteReason.EMPTY;
		}
		else
		{
			if (Sender instanceof Player)
			{
				Player Player = (Player)Sender;
				if (!Channel.getCreator().equalsIgnoreCase(Player.getName()))
				{
					this.DeleteReason = ChannelDeleteReason.DELETED_BY_STAFF;
				}
				else
				{
					this.DeleteReason = ChannelDeleteReason.DELETED_BY_OWNER;
				}
			}
			else
			{
				this.DeleteReason = ChannelDeleteReason.OTHER;
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
		return this.Sender;
	}
	
	public boolean isSenderPlayer()
	{
		return (this.Sender instanceof Player);
	}
	
	public boolean isSenderConsole()
	{
		return (this.Sender instanceof ConsoleCommandSender);
	}

	public ChatChannel getChannel()
	{
		return this.Channel;
	}
	
	public ChannelDeleteReason getDeleteReason()
	{
		return this.DeleteReason;
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
