package com.caved_in.chatmanager.config;

import com.caved_in.chatmanager.handlers.chat.channels.ChatChannel;
import com.caved_in.chatmanager.handlers.chat.channels.XmlChatChannel;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

public class Configuration
{
	@ElementList(name = "chatChannels", type = XmlChatChannel.class, inline = true)
	private List<XmlChatChannel> chatChannels = new ArrayList<XmlChatChannel>();

	public Configuration(@ElementList(name = "chatChannels", type = XmlChatChannel.class, inline = true) List<XmlChatChannel> chatChannels)
	{
		this.chatChannels = chatChannels;
	}

	public Configuration()
	{
		this.chatChannels.add(new XmlChatChannel());
	}

	public List<ChatChannel> getChatChannels()
	{
		List<ChatChannel> chatChannels = new ArrayList<ChatChannel>();
		for (XmlChatChannel xmlChannel : this.chatChannels)
		{
			chatChannels.add(xmlChannel.getChatChannel());
		}
		return chatChannels;
	}

	public List<XmlChatChannel> getXmlChatChannels()
	{
		return this.chatChannels;
	}

}
