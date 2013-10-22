package com.caved_in.chatmanager.commands;

public enum CommandMessage
{
	Deny("[Vaecon Network] You do not have permission for this command, if you believe this is an error please fill out a bug report on our forums."),
	Error("[Vaecon Network] There was an error while processing this command; Please check the syntax used, and if it persists fill out a bug report on our forums."),
	NonExist("[Vaecon Network] That command does not exist.");

	private final String Message;

	CommandMessage(String Message)
	{
		this.Message = Message;
	}

	public String getMessage()
	{
		return Message;
	}
}
