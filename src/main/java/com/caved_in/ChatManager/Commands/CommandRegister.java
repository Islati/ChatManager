package com.caved_in.chatmanager.commands;

import com.caved_in.chatmanager.commands.chat.ChannelCommands;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegister {

	public CommandRegister(JavaPlugin Plugin) {
		CommandController.registerCommands(Plugin, new ChannelCommands());
		//CommandController.registerCommands(Plugin, new ChatCommands());
	}

}
