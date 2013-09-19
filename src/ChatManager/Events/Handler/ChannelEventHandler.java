package ChatManager.Events.Handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ChatManager.ChatManager;
import ChatManager.Events.ChannelChatEvent;
import ChatManager.Events.ChannelCreateEvent;
import ChatManager.Events.ChannelDeleteEvent;
import ChatManager.Events.ChannelJoinEvent;
import ChatManager.Events.ChannelLeaveEvent;
import ChatManager.Handlers.Chat.ChannelHandler;
import ChatManager.Handlers.Chat.Channels.ChatChannel;
import ChatManager.Handlers.Player.PlayerHandler;
import ChatManager.Handlers.Player.cPlayer;

public class ChannelEventHandler
{
	
	public static void HandleChannelChatEvent(ChannelChatEvent Event)
	{
		Bukkit.getServer().getPluginManager().callEvent(Event);
		if (!Event.isCancelled())
		{
			Event.getChannel().sendToMembers(Event.getPlayer().getName(),Event.getMessage()); //Send the message to all users in the chat
		}
	}
	
	public static void HandleChannelDeleteEvent(ChannelDeleteEvent Event)
	{
		Bukkit.getServer().getPluginManager().callEvent(Event);
		if (!Event.isCancelled())
		{
			ChatChannel Channel = Event.getChannel();
			if (!Channel.getName().equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL) && !Channel.isPermanant())
			{
				if (Channel.getCreator().equalsIgnoreCase(Event.getDeleter().getName()))
				{
					ChatManager.ChannelHandler.removeChannel(Event.getChannel()); //Call the channel-handler to remove the channel
					Event.getDeleter().sendMessage(ChatColor.GREEN + "Channel Deleted"); //Tell the sender that the channel was deleted
				}
				else
				{
					if (Event.getDeleter().hasPermission("chatmanager.channels.delete"))
					{
						ChatManager.ChannelHandler.removeChannel(Event.getChannel()); //Call the channel-handler to remove the channel
						Event.getDeleter().sendMessage(ChatColor.GREEN + "Channel Deleted"); //Tell the sender that the channel was deleted
					}
					else
					{
						Event.getDeleter().sendMessage(ChatColor.RED + "You don't have permission to delete others channels.");
					}
				}
			}
			else
			{
				if (Channel.getName().equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL))
				{
					Event.getDeleter().sendMessage(ChatColor.RED + "The global chat can't be deleted");
				}
				else
				{
					Event.getDeleter().sendMessage(ChatColor.RED + "Permanant channels can't be deleted");
				}
			}
		}
	}
	
	public static void HandleChannelLeaveEvent(ChannelLeaveEvent Event)
	{
		Bukkit.getServer().getPluginManager().callEvent(Event);
		if (!Event.isCancelled())
		{
			Event.getChannel().removeMember(Event.getPlayer());
			if (!Event.getChannel().isPermanant())
			{
				if (Event.getChannel().getMembers().size() <= 0)
				{
					ChannelDeleteEvent DeleteEvent = new ChannelDeleteEvent(Event.getChannel(),null);
					HandleChannelDeleteEvent(DeleteEvent);
				}
			}
			if (!Event.getChannel().getName().equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL))
			{
				if (PlayerHandler.isOnline(Event.getPlayer()))
				{
					ChatChannel Channel = ChatManager.ChannelHandler.getChannel(ChatManager.GLOBAL_CHAT_CHANNEL);
					ChatManager.ChannelHandler.addPlayerToChannel(Event.getPlayer(), Channel);
					PlayerHandler.getData(Event.getPlayer()).setChatChannel(ChatManager.GLOBAL_CHAT_CHANNEL);
					if (Channel.allowJoinLeaveMessages())
					{
						Channel.sendToMembers(ChatColor.GRAY + Event.getPlayer() + " has joined the Channel!");
					}
				}
			}
			if (Event.getChannel().allowJoinLeaveMessages()) //Check for allowing of Join-Leave messages
			{
				Event.getChannel().sendToMembers(ChatColor.GRAY + Event.getPlayer() + " has left the chat."); //Notify all users that user has left
			}
		}
	}
	
	public static void HandleChannelCreateEvent(ChannelCreateEvent Event)
	{
		Bukkit.getServer().getPluginManager().callEvent(Event);
		if (!Event.isCancelled())
		{
			if (!ChatManager.ChannelHandler.isChannel(Event.getChannel()))
			{
				ChatManager.ChannelHandler.addChannel(Event.getChannel()); //Create the channel
				Event.getCreator().sendMessage(ChatColor.GREEN + "Channel '" + Event.getChannel().getName() + "' has been created."); //Send message saying it was created
			}
			else
			{
				Event.getCreator().sendMessage(ChatColor.RED + "Channel '" + Event.getChannel().getName() + "' already exists.");
			}
		}
	}
	
	public static void HandleChannelJoinEvent(ChannelJoinEvent Event)
	{
		Bukkit.getServer().getPluginManager().callEvent(Event);
		if (!Event.isCancelled())
		{
			ChatChannel Channel = Event.getChannel();
			if (Channel.hasPermission())
			{
				if (!Event.getPlayer().hasPermission(Channel.getPermission()))
				{
					return;
				}
			}
			
			if (Channel.isPrivate() && !Channel.getCreator().equalsIgnoreCase(Event.getPlayer().getName()))
			{
				if (ChatManager.ChannelHandler.hasChannelInvitation(Event.getPlayer().getName()))
				{
					if (ChatManager.ChannelHandler.acceptChannelInvitation(Event.getPlayer().getName()))
					{
						if (Channel.allowJoinLeaveMessages())
						{
							Channel.sendToMembers(ChatColor.GRAY + Event.getPlayer().getName() + " has joined the Channel!");
						}
					}
					else
					{
						Event.getPlayer().sendMessage(ChatColor.YELLOW + "Failed to join the channel.");
					}
				}
				else
				{
					Event.getPlayer().sendMessage(ChatColor.YELLOW + "You don't have an invitation to hoin this chat.");
				}
			}
			else
			{
				ChatChannel CurrentChannel = ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(Event.getPlayer().getName()).getChatChannel());
				ChannelLeaveEvent lEvent = new ChannelLeaveEvent(CurrentChannel,Event.getPlayer());
				ChannelEventHandler.HandleChannelLeaveEvent(lEvent);
				
				ChatManager.ChannelHandler.addPlayerToChannel(Event.getPlayer(), Channel);
				if (Channel.allowJoinLeaveMessages())
				{
					Channel.sendToMembers(ChatColor.GRAY + Event.getPlayer().getName() + " has joined the Channel!");
				}
				PlayerHandler.getData(Event.getPlayer().getName()).setChatChannel(Channel.getName());
				Event.getPlayer().sendMessage("You chat channel is now: " + Channel.getName());
			}
		}
	}
}
