package com.caved_in.chatmanager.config;

import com.caved_in.chatmanager.handlers.util.StringUtil;

public class MessageConfiguration
{

	public static String configurationSaved = StringUtil.formatColorCodes("&aMultiChat configuration has been saved");
	public static String configurationLoaded = StringUtil.formatColorCodes("&aMultiChat configuration has been loaded");

	public MessageConfiguration() { } //Potential localization support, not sure yet.
}
