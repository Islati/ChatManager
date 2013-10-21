package ChatManager.Events.Handler;

import ChatManager.Commands.CommandPermissions;
import ChatManager.Handlers.Util.StringUtil;
import org.bukkit.Bukkit;

import ChatManager.ChatManager;
import ChatManager.Events.ChannelChatEvent;
import ChatManager.Events.ChannelCreateEvent;
import ChatManager.Events.ChannelDeleteEvent;
import ChatManager.Events.ChannelJoinEvent;
import ChatManager.Events.ChannelLeaveEvent;
import ChatManager.Handlers.Chat.Channels.ChatChannel;
import ChatManager.Handlers.Player.PlayerHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChannelEventHandler
{
	/**
	 *
	 * @param channelChatEvent
	 */
	public static void handleChannelChatEvent(ChannelChatEvent channelChatEvent)
	{
		Bukkit.getServer().getPluginManager().callEvent(channelChatEvent);
		if (!channelChatEvent.isCancelled())
		{
			channelChatEvent.getChatChannel().sendToMembers(channelChatEvent.getPlayer().getName(),channelChatEvent.getMessage()); //Send the message to all users in the chat
		}
	}

	/**
	 *
	 * @param event
	 */
	public static void handleChannelDeleteEvent(ChannelDeleteEvent event)
	{
		Bukkit.getServer().getPluginManager().callEvent(event);

		CommandSender channelDeleter = event.getDeleter();
		ChatChannel channelToDelete = event.getChatChannel();
		String channelDeleterName = channelToDelete.getName();

		if (!event.isCancelled())
		{
			ChatChannel chatChannel = event.getChatChannel();
			if (!chatChannel.getName().equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL) && !chatChannel.isPermanent())
			{
				if (chatChannel.getCreator().equalsIgnoreCase(channelDeleterName))
				{
					ChatManager.channelHandler.removeChannel(channelToDelete); //Call the channel-handler to remove the channel
					channelDeleter.sendMessage(StringUtil.formatColorCodes("&aChannel Deleted")); //Tell the sender that the channel was deleted
				}
				else
				{
					if (channelDeleter.hasPermission(CommandPermissions.CHANNEL_DELETE_PERMISSION))
					{
						ChatManager.channelHandler.removeChannel(channelToDelete); //Call the channel-handler to remove the channel
						event.getDeleter().sendMessage(StringUtil.formatColorCodes("&aChannel Deleted")); //Tell the sender that the channel was deleted
					}
					else
					{
						event.getDeleter().sendMessage(StringUtil.formatColorCodes("&cYou don't have permission to delete others channels."));
					}
				}
			}
			else
			{
				if (chatChannel.getName().equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL))
				{
					channelDeleter.sendMessage(StringUtil.formatColorCodes("&cThe global chat can't be deleted"));
				}
				else
				{
					channelDeleter.sendMessage(StringUtil.formatColorCodes("&aPermanant channels can't be deleted"));
				}
			}
		}
	}
	
	public static void handleChannelLeaveEvent(ChannelLeaveEvent event)
	{
		String playerName = event.getPlayer();
		ChatChannel chatChannel = event.getChatChannel();
		String channelName = chatChannel.getName();

		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled())
		{
			chatChannel.removeMember(playerName);
			if (!chatChannel.isPermanent())
			{
				if (chatChannel.getChatMembers().size() <= 0)
				{
					ChannelDeleteEvent deleteEvent = new ChannelDeleteEvent(chatChannel,null);
					handleChannelDeleteEvent(deleteEvent);
				}
			}
			if (channelName.equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL))
			{
				if (PlayerHandler.isOnline(playerName))
				{
					ChatChannel globalChatChannel = ChatManager.channelHandler.getChannel(ChatManager.GLOBAL_CHAT_CHANNEL);
					ChatManager.channelHandler.addPlayerToChannel(playerName, globalChatChannel);
					PlayerHandler.getData(playerName).setChatChannel(ChatManager.GLOBAL_CHAT_CHANNEL);
					if (globalChatChannel.allowJoinLeaveMessages())
					{
						globalChatChannel.sendToMembers(StringUtil.formatColorCodes(String.format("&7{0} has joined the Channel!", playerName)));
					}
				}
			}
			if (chatChannel.allowJoinLeaveMessages()) //Check for allowing of Join-Leave messages
			{
				chatChannel.sendToMembers(StringUtil.formatColorCodes(String.format("&7{0} has left the chat.",playerName))); //Notify all users that user has left
			}
		}
	}
	
	public static void handleChannelCreateEvent(ChannelCreateEvent event)
	{
		Bukkit.getServer().getPluginManager().callEvent(event);

		ChatChannel chatChannel = event.getChatChannel();

		CommandSender channelCreator = event.getCreator();

		if (!event.isCancelled())
		{
			if (!ChatManager.channelHandler.isChannel(chatChannel))
			{
				ChatManager.channelHandler.addChannel(chatChannel); //Create the channel
				channelCreator.sendMessage(StringUtil.formatColorCodes(String.format("&cChannel '{0}' has been created.", chatChannel.getName()))); //Send message saying it was created
			}
			else
			{
				channelCreator.sendMessage(StringUtil.formatColorCodes(String.format("&cChannel '{0}' already exists.",chatChannel.getName())));
			}
		}
	}
	
	public static void handleChannelJoinEvent(ChannelJoinEvent event)
	{
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled())
		{
			ChatChannel chatChannel = event.getChatChannel();
			String chatChannelCreator = chatChannel.getCreator();

			Player player = event.getPlayer();
			String playerName = player.getName();
			String playerDisplayName = player.getDisplayName();

			if (chatChannel.hasPermission())
			{
				if (!event.getPlayer().hasPermission(chatChannel.getChannelPermission()))
				{
					player.sendMessage(StringUtil.formatColorCodes(String.format("&cNon-sufficient permissions to join the channel; You must have &e{0} to join.",chatChannel.getChannelPermission())));
					return;
				}
			}
			
			if (chatChannel.isPrivate() && !chatChannelCreator.equalsIgnoreCase(playerName))
			{
				if (ChatManager.channelHandler.hasChannelInvitation(playerName))
				{
					if (ChatManager.channelHandler.acceptChannelInvitation(playerName))
					{
						if (chatChannel.allowJoinLeaveMessages())
						{
							chatChannel.sendToMembers(StringUtil.formatColorCodes(String.format("&7{0} has joined the Channel!", playerDisplayName)));
						}
					}
					else
					{
						player.sendMessage(StringUtil.formatColorCodes("&eFailed to join the channel."));
					}
				}
				else
				{
					player.sendMessage(StringUtil.formatColorCodes("&eYou don't have an invitation to join this chat."));
				}
			}
			else
			{
				String playerCurrentChannelName = PlayerHandler.getData(playerName).getChatChannel(); //Get the players cPlayer instance

				ChatChannel currentPlayerChannel = ChatManager.channelHandler.getChannel(playerCurrentChannelName); //Get the players current chat channel

				ChannelLeaveEvent channelLeaveEvent = new ChannelLeaveEvent(currentPlayerChannel,player);
				ChannelEventHandler.handleChannelLeaveEvent(channelLeaveEvent);
				
				ChatManager.channelHandler.addPlayerToChannel(player, chatChannel);

				if (chatChannel.allowJoinLeaveMessages())
				{
					chatChannel.sendToMembers(StringUtil.formatColorCodes(String.format("&7{0} has joined the Channel!",playerName)));
				}
				PlayerHandler.getData(playerName).setChatChannel(chatChannel.getName());
				player.sendMessage("&7You're now chatting in: &o" + chatChannel.getName());
			}
		}
	}
}
