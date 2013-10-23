package com.caved_in.chatmanager.handlers.chat;

import com.caved_in.chatmanager.events.ChannelLeaveEvent;
import com.caved_in.chatmanager.events.handler.ChannelEventHandler;
import com.caved_in.chatmanager.handlers.util.StringUtil;
import com.caved_in.chatmanager.handlers.player.PlayerHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.caved_in.chatmanager.ChatManager;
import com.caved_in.chatmanager.handlers.chat.channels.ChannelInvitation;
import com.caved_in.chatmanager.handlers.chat.channels.ChatChannel;
import com.caved_in.chatmanager.handlers.player.cPlayer;
import com.caved_in.chatmanager.handlers.util.Cooldown;

import java.util.*;

public class ChannelHandler
{
	private Map<String, ChatChannel> chatChannels = new HashMap<String, ChatChannel>();
	private Map<String, ChannelInvitation> channelInvitations = new HashMap<String, ChannelInvitation>();
	private Cooldown invitationCooldown = new Cooldown(25);

	/**
	 *
	 */
	public ChannelHandler()
	{
		ChatChannel globalChat = new ChatChannel(ChatManager.GLOBAL_CHAT_CHANNEL, "");
		globalChat.setHasJoinLeaveMessages(false);
		globalChat.setPermanent(true);
		this.addChannel(globalChat);
	}

	/**
	 * @param chatChannels
	 */
	public void addChannel(ChatChannel... chatChannels)
	{
		for (ChatChannel chatChannel : chatChannels)
		{
			this.chatChannels.put(chatChannel.getName(), chatChannel);
		}
	}

	/**
	 * @param channelName
	 * @return
	 */
	public boolean isChannel(String channelName)
	{
		for(String chatChannel : this.chatChannels.keySet())
		{
			if (channelName.equalsIgnoreCase(chatChannel))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @param chatChannel
	 * @return
	 */
	public boolean isChannel(ChatChannel chatChannel)
	{
		return this.isChannel(chatChannel.getName());
	}

	/**
	 * @param chatChannel
	 */
	public void removeChannel(ChatChannel chatChannel)
	{
		//chatChannel.sendToMembers(ChatColor.YELLOW + "This channel's been deleted, you were moved to the global chat");
		for (String chatMember : chatChannel.getChatMembers())
		{
			if (PlayerHandler.hasData(chatMember))
			{
				PlayerHandler.getData(chatMember).setChatChannel(ChatManager.GLOBAL_CHAT_CHANNEL);
				PlayerHandler.getPlayer(chatMember).sendMessage(ChatColor.YELLOW + chatChannel.getName() + ChatColor.RED + " has been deleted, you've been placed in the global chat.");
			}
		}
		this.chatChannels.remove(chatChannel.getName());
	}

	/**
	 *
	 * @param chatChannel
	 * @param isReload
	 */
	public void removeChannel(ChatChannel chatChannel, boolean isReload)
	{
		for (String chatMember : chatChannel.getChatMembers())
		{
			if (PlayerHandler.hasData(chatMember))
			{
				PlayerHandler.getData(chatMember).setChatChannel(ChatManager.GLOBAL_CHAT_CHANNEL);
				PlayerHandler.getPlayer(chatMember).sendMessage(ChatColor.YELLOW + "MultiChat has been reloaded, and to stop any errors from happening you've been placed in the global chat.");
			}
		}
		this.chatChannels.remove(chatChannel.getName());
	}

	/**
	 * @param channelName
	 */
	public void removeChannel(String channelName)
	{
		if (isChannel(channelName))
		{
			this.removeChannel(this.chatChannels.get(channelName));
		}
	}

	/**
	 * @param channelName
	 * @return
	 */
	public ChatChannel getChannel(String channelName)
	{
		for(Map.Entry<String, ChatChannel> chatChannel : this.chatChannels.entrySet())
		{
			if (channelName.equalsIgnoreCase(chatChannel.getKey()))
			{
				return  chatChannel.getValue();
			}
		}
		return null;
	}

	/**
	 * @param player
	 * @param chatChannel
	 * @return
	 */
	public boolean addPlayerToChannel(Player player, ChatChannel chatChannel)
	{
		if (isChannel(chatChannel))
		{
			chatChannel.addMember(player.getName());
			return true;
		}
		return false;
	}

	/**
	 * @param playerName
	 * @param chatChannel
	 * @return
	 */
	public boolean addPlayerToChannel(String playerName, ChatChannel chatChannel)
	{
		if (isChannel(chatChannel))
		{
			chatChannel.addMember(playerName);
			return true;
		}
		return false;
	}

	/**
	 * @param playerName
	 * @return
	 */
	public boolean hasChannelInvitation(String playerName)
	{
		return this.channelInvitations.containsKey(playerName);
	}

	/**
	 * @param playerName
	 * @return
	 */
	public ChannelInvitation getInvitedChannel(String playerName)
	{
		return this.channelInvitations.get(playerName);
	}

	/**
	 * @param playerName
	 */
	public void removeChannelInvitation(String playerName)
	{
		if (this.hasChannelInvitation(playerName))
		{
			this.channelInvitations.remove(playerName);
		}
	}

	/**
	 * @param channelName
	 * @param invitingUser
	 * @param userInvited
	 */
	public boolean addChannelInvitation(String channelName, String invitingUser, String userInvited)
	{
		if (!this.isOnChannelInviteCooldown(invitingUser))
		{
			this.channelInvitations.put(userInvited, new ChannelInvitation(channelName, invitingUser, userInvited));
			this.setOnInviteCooldown(invitingUser);
			return true;
		}
		return false;
	}

	public void setOnInviteCooldown(String invitingUser)
	{
		this.invitationCooldown.setOnCooldown(invitingUser);
	}

	public boolean isOnChannelInviteCooldown(String invitingUser)
	{
		return this.invitationCooldown.isOnCooldown(invitingUser);
	}

	public boolean acceptChannelInvitation(String playerName)
	{
		if (PlayerHandler.hasData(playerName))
		{
			cPlayer cPlayer = PlayerHandler.getData(playerName);
			if (this.hasChannelInvitation(playerName))
			{
				ChannelInvitation chatInvitation = this.getInvitedChannel(playerName);
				String invitedChannelName = chatInvitation.getChannelName();

				if (!cPlayer.getChatChannel().equalsIgnoreCase(invitedChannelName))
				{
					ChatChannel playerCurrentChannel = ChatManager.channelHandler.getChannel(cPlayer.getChatChannel());
					ChannelLeaveEvent channelLeaveEvent = new ChannelLeaveEvent(playerCurrentChannel, PlayerHandler.getPlayer(playerName));
					ChannelEventHandler.handleChannelLeaveEvent(channelLeaveEvent);

					ChatManager.channelHandler.addPlayerToChannel(playerName, chatInvitation.getChatChannel());
					if (chatInvitation.getChatChannel().allowJoinLeaveMessages())
					{
						chatInvitation.getChatChannel().sendToMembers(StringUtil.formatColorCodes(String.format("&7{0} has joined the Channel!", playerName)));
					}
					PlayerHandler.getData(playerName).setChatChannel(invitedChannelName);
					PlayerHandler.getPlayer(playerName).sendMessage(StringUtil.formatColorCodes(String.format("&7You're now chatting in: &o{0}", invitedChannelName)));
					return true;
				}
				else
				{
					return false; //Player is already part of this channel.
				}
			}
			else
			{
				return false; //Player doesn't have a channel invitation
			}
		}
		return false; //Player Has no data
	}

	/**
	 * Get a list of the currently active chat channels
	 * @return
	 */
	public List<ChatChannel> getChannels()
	{
		return new ArrayList<ChatChannel>(this.chatChannels.values());
	}

	public void cleanChannels()
	{
		for(ChatChannel chatChannel : this.chatChannels.values())
		{
			if (!chatChannel.getName().equalsIgnoreCase(ChatManager.GLOBAL_CHAT_CHANNEL))
			{
				this.removeChannel(chatChannel,true);
			}
		}
	}
}
