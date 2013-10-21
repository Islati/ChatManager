package ChatManager.Commands.Chat;

import ChatManager.Handlers.Util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ChatManager.ChatManager;
import ChatManager.Commands.CommandController.CommandHandler;
import ChatManager.Commands.CommandController.SubCommandHandler;
import ChatManager.Events.*;
import ChatManager.Events.Handler.ChannelEventHandler;
import ChatManager.Handlers.Chat.Channels.ChannelInvitation;
import ChatManager.Handlers.Chat.Channels.ChatChannel;
import ChatManager.Handlers.HelpMenu.HelpScreen;
import ChatManager.Handlers.Player.PlayerHandler;

public class ChannelCommands
{
	
	public ChannelCommands() { }
	
	@CommandHandler(name = "channel", usage = "/channel", permission = "chatmanager.channel")
	public void channelBaseCommand(Player Player, String[] Args)
	{
		if (Args.length == 0)
		{
			Player.sendMessage(ChatColor.GOLD + "Please use the command '/channel help' for help.");
		}
	}
	
	@SubCommandHandler(name = "help", parent = "channel")
	public void channelHelpCommand(Player Player, String[] Args)
	{
		HelpScreen Screen = new HelpScreen("Channel-Command Help");
		Screen.setHeader(ChatColor.AQUA + "<name> (Page <page> of <maxpage>)");
		Screen.setFlipColor(ChatColor.GREEN, ChatColor.DARK_GREEN);
		Screen.setEntry("/channel create <Name>", "Make a new channel with the given name");
		Screen.setEntry("/channel create private <Name>", "Make an Invite-Only channel with the given name");
		Screen.setEntry("/channel join <Channel Name>", "Used to join a chat channel.");
		Screen.setEntry("/channel leave", "Used to leave the current channel you're in.");
		Screen.setEntry("/channel delete <name>", "Used to delete a channel with the given name");
		Screen.setEntry("/channel invite <name>", "Invite a user to your private channel");
		Screen.setEntry("/channel accept", "Accept a channel invitation");
		Screen.setEntry("/channel deny", "Deny a channel invitation");
		
		Screen.setFormat("<name> - <desc>");
		
		if (Args.length == 1)
		{
			Screen.sendTo(Player, 1, 7);
		}
		else
		{
			if (Args[1] != null && StringUtils.isNumeric(Args[1]))
			{
				int Page = Integer.parseInt(Args[1]);
				Screen.sendTo(Player, Page, 7);
			}
		}
	}
	
	@SubCommandHandler(name = "create", parent = "channel", permission = "chatmanager.channel.create")//usage = "/addchannel <name>", permission = "vaeconnetwork.chat.message"
	public void channelCreateCommand(Player player, String[] commandArgs)
	{
		if (commandArgs.length > 1 && !commandArgs[1].isEmpty())
		{
			String channelName = commandArgs[1];
			if (!channelName.equalsIgnoreCase("private"))
			{
				if (!ChatManager.ChannelHandler.isChannel(channelName))
				{
					ChannelCreateEvent channelCreateEvent = new ChannelCreateEvent(new ChatChannel(channelName,"[" + channelName + "]"), player);
					ChannelEventHandler.HandleChannelCreateEvent(channelCreateEvent);
				}
				else
				{
					player.sendMessage(ChatColor.RED + "The channel '" + channelName + "' already exists");
				}
			}
			else
			{
				if (commandArgs.length > 2 && !commandArgs[2].isEmpty())
				{
					String pChannelName = commandArgs[2];
					if (!ChatManager.ChannelHandler.isChannel(pChannelName))
					{
						ChatChannel privateChatChannel = new ChatChannel(pChannelName,ChatColor.GRAY + "[" + pChannelName + "]" + ChatColor.RESET,player.getName(),true);
						ChannelCreateEvent Event = new ChannelCreateEvent(privateChatChannel,player);
						ChannelEventHandler.HandleChannelCreateEvent(Event);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "The channel '" + channelName + "' already exists");
					}
					
				}
				else
				{
					player.sendMessage(ChatColor.YELLOW + "To create a private channel do '" + ChatColor.GREEN + "/channel create private <Name>" + ChatColor.YELLOW + "'");
				}
			}
		}
	}
	
	@SubCommandHandler(name = "delete", parent = "channel", permission = "chatmanager.channel.delete")
	public void deleteChannelCommand(Player player, String[] commandArgs)
	{
		if (commandArgs.length > 1 && !commandArgs[1].isEmpty())
		{
			String channelName = commandArgs[1];
			if (ChatManager.ChannelHandler.isChannel(channelName))
			{
				ChannelDeleteEvent channelDeleteEvent = new ChannelDeleteEvent(ChatManager.ChannelHandler.getChannel(channelName), player);
				ChannelEventHandler.HandleChannelDeleteEvent(channelDeleteEvent);
			}
			else
			{
				player.sendMessage(ChatColor.RED + "The Chat Channel '" + channelName + "' doesn't exist.");
			}
		}
	}
	
	@SubCommandHandler(name = "join", parent = "channel",permission = "chatmanager.channel.join")
	public void joinChannelEvent(Player player, String[] commandArgs)
	{
		if (commandArgs.length > 1)
		{
			if (!commandArgs[1].isEmpty())
			{
				String channelName = commandArgs[1];
				if (!ChatManager.ChannelHandler.isChannel(channelName))
				{
					player.sendMessage("Chat channel " + channelName + " doesn't exist");
				}
				else
				{
					ChannelJoinEvent channelJoinEvent = new ChannelJoinEvent(ChatManager.ChannelHandler.getChannel(channelName), player);
					ChannelEventHandler.HandleChannelJoinEvent(channelJoinEvent);
				}
			}
		}
	}
	
	@SubCommandHandler(name = "leave", parent = "channel", permission = "chatmanager.channel.join")
	public void leaveChannelCommand(Player player, String[] commandArgs)
	{
		ChannelLeaveEvent channelLeaveEvent = new ChannelLeaveEvent(ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(player.getName()).getChatChannel()), player);
		ChannelEventHandler.HandleChannelLeaveEvent(channelLeaveEvent);
	}
	
	@SubCommandHandler(name = "invite", parent = "channel", permission = "chatmanager.channel.invite")
	public void InviteChannel(Player player, String[] commandArgs)
	{
		String playerName = player.getName();
		String playerDisplayName = player.getDisplayName();
		if (commandArgs.length > 1 && !commandArgs[1].isEmpty())
		{
			String invitedPlayer = commandArgs[1];
			if (PlayerHandler.isOnline(invitedPlayer))
			{
				ChatChannel invitingChannel = ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(playerName).getChatChannel());
				String invitingChannelName = invitingChannel.getName();
				if (!invitingChannelName.equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL))
				{
					if (ChatManager.ChannelHandler.addChannelInvitation(invitingChannelName, playerName, invitedPlayer))
					{
						PlayerHandler.getPlayer(invitedPlayer).sendMessage(StringUtil.formatColorCodes(String.format("&a{0}&e has invited you to join their chat channel.", playerDisplayName)));
						PlayerHandler.getPlayer(invitedPlayer).sendMessage(StringUtil.formatColorCodes("&eTo accept, type &a/channel accept"));
						PlayerHandler.getPlayer(invitedPlayer).sendMessage(StringUtil.formatColorCodes("&eTo deny type &c/channel deny"));
						player.sendMessage(StringUtil.formatColorCodes(String.format("&a{0} has been invited to join your channel.", playerDisplayName)));
					}
					else
					{
						player.sendMessage(StringUtil.formatColorCodes(String.format("&cFailed to invite &e{0}&c to your channel.",invitedPlayer)));
					}
				}
				else
				{
					player.sendMessage(StringUtil.formatColorCodes("&cYou can't invite players to join the global chat channel."));
				}
			}
			else
			{
				player.sendMessage(StringUtil.formatColorCodes(String.format("&e{0} &cis offline; &e{0} &cneeds to be online to invite them to your channel",invitedPlayer)));
			}
		}
		else
		{
			player.sendMessage(StringUtil.formatColorCodes("&cThe proper usage is &e/channel invite <name>"));
		}
	}
	
	@SubCommandHandler(name = "accept", parent = "channel", permission = "chatmanager.channel.invite")
	public void acceptChannelInvitation(Player player, String[] commandArgs)
	{
		String playerName = player.getName();
		String playerDisplayName = player.getDisplayName();
		if (ChatManager.ChannelHandler.hasChannelInvitation(playerName))
		{
			ChannelInvitation channelInvitation = ChatManager.ChannelHandler.getInvitedChannel(playerName);
			String channelInviter = channelInvitation.getInviter();
			if (PlayerHandler.isOnline(channelInviter))
			{
				if (ChatManager.ChannelHandler.acceptChannelInvitation(playerName))
				{
					player.sendMessage(StringUtil.formatColorCodes("&aYou've joined the chat channel!"));
					PlayerHandler.getPlayer(channelInviter).sendMessage(StringUtil.formatColorCodes(String.format("&a{0} has accepted your channel invitation!",playerDisplayName)));
				}
				else
				{
					player.sendMessage(StringUtil.formatColorCodes("&eFailed to accept channel invitation; Did the invitation expire?"));
				}
			}
			else
			{
				player.sendMessage(StringUtil.formatColorCodes(String.format("&cThis invitation has expired; {0} is offline.",channelInviter)));
				ChatManager.ChannelHandler.removeChannelInvitation(playerName);
			}
		}
		else
		{
			player.sendMessage(StringUtil.formatColorCodes("&cYou weren't invited to join any channels."));
		}
	}
	
	@SubCommandHandler(name = "deny", parent = "channel", permission = "chatmanager.channel.invite")
	public void denyInvitationCommand(Player player, String[] commandArgs)
	{
		String playerName = player.getName();
		String playerDisplayName = player.getDisplayName();
		if (ChatManager.ChannelHandler.hasChannelInvitation(player.getName()))
		{
			ChannelInvitation channelInvitation = ChatManager.ChannelHandler.getInvitedChannel(playerName);
			String channelInviter = channelInvitation.getInviter();
			if (PlayerHandler.isOnline(channelInviter))
			{
				PlayerHandler.getPlayer(channelInviter).sendMessage(StringUtil.formatColorCodes(String.format("&e{0}&c has declined your channel invitation.",playerDisplayName)));
			}
			ChatManager.ChannelHandler.removeChannelInvitation(playerName);
			player.sendMessage(StringUtil.formatColorCodes(String.format("&eYou've declined the channel invitation from &c{0}", channelInviter)));
		}
		else
		{
			player.sendMessage(StringUtil.formatColorCodes("&cYou don't have any channel invitations."));
		}
	}
}
