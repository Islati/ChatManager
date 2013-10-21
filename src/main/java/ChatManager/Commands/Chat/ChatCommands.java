package ChatManager.Commands.Chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ChatManager.ChatManager;
import ChatManager.Commands.CommandController.*;
import ChatManager.Handlers.Chat.Channels.ChatChannel;
import ChatManager.Handlers.HelpMenu.HelpScreen;
import ChatManager.Handlers.Player.*;

public class ChatCommands
{
	@CommandHandler
	(
		name = "msg",
		usage = "/msg <player> <msg> to send a player a message"
		//permission = "vaeconnetwork.chat.message"
	)
	public void messageCommand(Player player, String[] commandArgs)
	{
		if (!commandArgs[0].isEmpty())
		{
			String messagingPlayer = commandArgs[0];
			if (!commandArgs[1].isEmpty())
			{
				String Message = commandArgs[1];
				if (PlayerHandler.isOnline(messagingPlayer))
				{
					PlayerHandler.getPlayer(messagingPlayer).sendMessage(ChatColor.WHITE + "[" + ChatColor.YELLOW + player.getDisplayName()	 + ChatColor.AQUA + " -> " + ChatColor.GREEN + "You" + ChatColor.WHITE + "] " + Message);
				}
				PlayerHandler.getPlayer(messagingPlayer).sendMessage(ChatColor.WHITE + "[" + ChatColor.YELLOW + "You" + ChatColor.AQUA + " -> " + ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + "] " + Message);
			}
		}
	}
	
}
