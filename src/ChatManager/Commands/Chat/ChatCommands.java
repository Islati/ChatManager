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
	public void Message(Player Player, String[] Args)
	{
		if (!Args[0].isEmpty())
		{
			String Name = Args[0];
			if (!Args[1].isEmpty())
			{
				String Message = Args[1];
				if (PlayerHandler.isOnline(Name))
				{
					PlayerHandler.getPlayer(Name).sendMessage(ChatColor.WHITE + "[" + ChatColor.YELLOW + Player.getName() + ChatColor.AQUA + " -> " + ChatColor.GREEN + "You" + ChatColor.WHITE + "] " + Message);
				}
			}
		}
	}
	
}
