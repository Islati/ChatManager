package com.caved_in.chatmanager.config;

import com.caved_in.chatmanager.handlers.chat.channels.XmlChatChannel;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

public class Configuration
{
	@ElementList(name = "chatChannels", type = XmlChatChannel.class, inline = true)
	private List<XmlChatChannel> xmlChatChannels = new ArrayList<XmlChatChannel>();
}
