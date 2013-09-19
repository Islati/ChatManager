package ChatManager.Commands.Chat;

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
	
	@CommandHandler(name = "channel", usage = "/channel") //permission = "vaeconnetwork.chat.message"
	public void ChannelBase(Player Player, String[] Args)
	{
		if (Args.length == 0)
		{
			Player.sendMessage(ChatColor.GOLD + "Please use the command '/channel help' for help.");
		}
	}
	
	@SubCommandHandler(name = "help", parent = "channel")
	public void ChannelHelp(Player Player, String[] Args)
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
	
	@SubCommandHandler(name = "create", parent = "channel")//usage = "/addchannel <name>", permission = "vaeconnetwork.chat.message"
	public void MakeChannel(Player Player, String[] Args)
	{
		if (Args.length > 1 && !Args[1].isEmpty())
		{
			String ChannelName = Args[1];
			if (!ChannelName.equalsIgnoreCase("private"))
			{
				if (!ChatManager.ChannelHandler.isChannel(ChannelName))
				{
					ChannelCreateEvent Event = new ChannelCreateEvent(new ChatChannel(ChannelName,"[" + ChannelName + "]"), Player);
					ChannelEventHandler.HandleChannelCreateEvent(Event);
				}
				else
				{
					Player.sendMessage(ChatColor.RED + "The channel '" + ChannelName + "' already exists");
				}
			}
			else
			{
				if (Args.length > 2 && !Args[2].isEmpty())
				{
					String pChannelName = Args[2];
					if (!ChatManager.ChannelHandler.isChannel(pChannelName))
					{
						ChatChannel PrivateChannel = new ChatChannel(pChannelName,ChatColor.GRAY + "[" + pChannelName + "]" + ChatColor.RESET,Player.getName(),true);
						ChannelCreateEvent Event = new ChannelCreateEvent(PrivateChannel,Player);
						ChannelEventHandler.HandleChannelCreateEvent(Event);
					}
					else
					{
						Player.sendMessage(ChatColor.RED + "The channel '" + ChannelName + "' already exists");
					}
					
				}
				else
				{
					Player.sendMessage(ChatColor.YELLOW + "To create a private channel do '" + ChatColor.GREEN + "/channel create private <Name>" + ChatColor.YELLOW + "'");
				}
			}
		}
	}
	
	@SubCommandHandler(name = "delete", parent = "channel"/*, permission = "chatmanager.channels.delete"*/)
	public void DeleteChannel(Player Player, String[] Args)
	{
		if (Args.length > 1 && !Args[1].isEmpty())
		{
			String ChannelName = Args[1];
			if (ChatManager.ChannelHandler.isChannel(ChannelName))
			{
				ChannelDeleteEvent Event = new ChannelDeleteEvent(ChatManager.ChannelHandler.getChannel(ChannelName), Player);
				ChannelEventHandler.HandleChannelDeleteEvent(Event);
			}
			else
			{
				Player.sendMessage(ChatColor.RED + "The Chat Channel '" + ChannelName + "' doesn't exist.");
			}
		}
	}
	
	@SubCommandHandler(name = "join", parent = "channel")
	public void JoinChannel(Player Player, String[] Args)
	{
		if (Args.length > 1)
		{
			if (!Args[1].isEmpty())
			{
				String ChannelName = Args[1];
				if (!ChatManager.ChannelHandler.isChannel(ChannelName))
				{
					Player.sendMessage("Chat channel " + ChannelName + " doesn't exist");
				}
				else
				{
					ChannelJoinEvent Event = new ChannelJoinEvent(ChatManager.ChannelHandler.getChannel(ChannelName), Player);
					ChannelEventHandler.HandleChannelJoinEvent(Event);
				}
			}
		}
	}
	
	@SubCommandHandler(name = "leave", parent = "channel")
	public void LeaveChannel(Player Player,String[] Args)
	{
		ChannelLeaveEvent Event = new ChannelLeaveEvent(ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(Player.getName()).getChatChannel()), Player);
		ChannelEventHandler.HandleChannelLeaveEvent(Event);
	}
	
	@SubCommandHandler(name = "invite", parent = "channel")
	public void InviteChannel(Player Player, String[] Args)
	{
		if (Args.length > 1 && !Args[1].isEmpty())
		{
			String Invited = Args[1];
			if (PlayerHandler.isOnline(Invited))
			{
				ChatChannel InviterChannel = ChatManager.ChannelHandler.getChannel(PlayerHandler.getData(Player.getName()).getChatChannel());
				if (!InviterChannel.getName().equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL))
				{
					if (ChatManager.ChannelHandler.addChannelInvitation(InviterChannel.getName(), Player.getName(), Invited))
					{
						PlayerHandler.getPlayer(Invited).sendMessage(ChatColor.GREEN + Player.getName() + ChatColor.YELLOW + " has invited you to join their chat channel");
						PlayerHandler.getPlayer(Invited).sendMessage(ChatColor.YELLOW + "To accept, type " + ChatColor.GREEN + "/channel accept");
						PlayerHandler.getPlayer(Invited).sendMessage(ChatColor.YELLOW + "To deny type " + ChatColor.RED + "/channel deny");
						Player.sendMessage(ChatColor.GREEN + Invited + " has been invited to join your channel.");
					}
					else
					{
						Player.sendMessage(ChatColor.RED + "Failed to invite " + ChatColor.YELLOW + Invited + ChatColor.RED + " to your channel.");
					}
				}
			}
			else
			{
				Player.sendMessage(ChatColor.RED + Invited + " is offline; The player needs to be online to invite them to your channel.");
			}
		}
		else
		{
			Player.sendMessage(ChatColor.RED + "Proper usage is " + ChatColor.YELLOW + "/channel invite <name>");
		}
	}
	
	@SubCommandHandler(name = "accept", parent = "channel")
	public void AcceptInvitationChannel(Player Player, String[] Args)
	{
		if (ChatManager.ChannelHandler.hasChannelInvitation(Player.getName()))
		{
			ChannelInvitation Invite = ChatManager.ChannelHandler.getInvitedChannel(Player.getName());
			if (PlayerHandler.isOnline(Invite.getInviter()))
			{
				if (ChatManager.ChannelHandler.acceptChannelInvitation(Player.getName()))
				{
					Player.sendMessage(ChatColor.GREEN + "You've joined the chat channel!");
					PlayerHandler.getPlayer(Invite.getInviter()).sendMessage(ChatColor.GREEN + Player.getName() + " has accepted your channel invitation!");
				}
			}
			else
			{
				Player.sendMessage(ChatColor.RED + "This invitation has expired; " + Invite.getInviter() + " is offline.");
				ChatManager.ChannelHandler.removeChannelInvitation(Player.getName());
			}
		}
		else
		{
			Player.sendMessage(ChatColor.RED + "You weren't invited to join any channels.");
		}
	}
	
	@SubCommandHandler(name = "deny", parent = "channel")
	public void DenyInvitationChannel(Player Player, String[] Args)
	{
		if (ChatManager.ChannelHandler.hasChannelInvitation(Player.getName()))
		{
			ChannelInvitation Invite = ChatManager.ChannelHandler.getInvitedChannel(Player.getName());
			if (PlayerHandler.isOnline(Invite.getInviter()))
			{
				PlayerHandler.getPlayer(Invite.getInviter()).sendMessage(ChatColor.YELLOW + Player.getName() + ChatColor.RED + " has declined your channel invitation.");
			}
			ChatManager.ChannelHandler.removeChannelInvitation(Player.getName());
			Player.sendMessage(ChatColor.YELLOW + "You've declined the channel invitation from " + ChatColor.RED + Invite.getInviter());
		}
		else
		{
			Player.sendMessage(ChatColor.RED + "You don't have any channel invitations.");
		}
	}
}
