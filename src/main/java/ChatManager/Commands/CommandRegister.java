package ChatManager.Commands;

import org.bukkit.plugin.java.JavaPlugin;

import ChatManager.Commands.Chat.ChannelCommands;
import ChatManager.Commands.Chat.ChatCommands;

public class CommandRegister
{
	
	public CommandRegister(JavaPlugin Plugin)
	{
		CommandController.registerCommands(Plugin, new ChannelCommands());
		//CommandController.registerCommands(Plugin, new ChatCommands());
	}

}
