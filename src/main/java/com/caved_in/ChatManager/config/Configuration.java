package com.caved_in.chatmanager.config;

import com.caved_in.chatmanager.handlers.chat.channels.ChatChannel;
import com.caved_in.chatmanager.handlers.chat.channels.XmlChatChannel;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

	private String curseApiKey = "61945f6b25e49819925d9e035e65c260c5414ba2";

	@Element(name = "auto-update", required = true)
	private boolean autoUpdate = true;

	@Element(name = "language", required = false)
	private String messageLanguage = "english";

	@ElementList(name = "chatChannels", type = XmlChatChannel.class, inline = true)
	private List<XmlChatChannel> chatChannels = new ArrayList<XmlChatChannel>();

	public Configuration(@ElementList(name = "chatChannels", type = XmlChatChannel.class, inline = true) List<XmlChatChannel> chatChannels,
						 @Element(name = "auto-update") boolean autoUpdate,
						 @Element(name = "language", required = false) String messageLanguage
	) {
		this.chatChannels = chatChannels;
		this.autoUpdate = autoUpdate;
		this.messageLanguage = messageLanguage;
	}

	public Configuration() {
		this.chatChannels.add(new XmlChatChannel());
	}

	public List<ChatChannel> getChatChannels() {
		List<ChatChannel> chatChannels = new ArrayList<ChatChannel>();
		for (XmlChatChannel xmlChannel : this.chatChannels) {
			chatChannels.add(xmlChannel.getChatChannel());
		}
		return chatChannels;
	}

	public List<XmlChatChannel> getXmlChatChannels() {
		return this.chatChannels;
	}

	public boolean isAutoUpdateEnabled() {
		return this.autoUpdate;
	}

	public String getMessageLanguage() {
		return this.messageLanguage;
	}

	public String getCurseApiKey() {
		return this.curseApiKey;
	}
}
