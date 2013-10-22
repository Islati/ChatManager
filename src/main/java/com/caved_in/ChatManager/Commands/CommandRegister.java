package com.caved_in.chatmanager.commands;

import org.bukkit.plugin.java.JavaPlugin;

import com.caved_in.chatmanager.commands.chat.ChannelCommands;

public class CommandRegister
{
	
	public CommandRegister(JavaPlugin Plugin)
	{
		CommandController.registerCommands(Plugin, new ChannelCommands());
		//CommandController.registerCommands(Plugin, new ChatCommands());
	}

}
