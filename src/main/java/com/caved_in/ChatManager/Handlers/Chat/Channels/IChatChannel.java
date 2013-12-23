package com.caved_in.chatmanager.handlers.chat.channels;

import java.util.List;

public interface IChatChannel {
	/**
	 * Get the name of the chat channel
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Get the channels prefix (if any)
	 *
	 * @return
	 */
	public String getPrefix();

	/**
	 * Set the channels name (Used as prefix if prefix's are enabled, but no prefix defined)
	 *
	 * @param Name
	 */
	public void setName(String Name);

	/**
	 * Set the channels chat-prefix
	 *
	 * @param Name
	 */
	public void setPrefix(String Name);

	/**
	 * Get the current users in this chat channel
	 *
	 * @return
	 */
	public List<String> getChatMembers();

	/**
	 * Add a member to the chat-channel
	 *
	 * @param Name
	 */
	public void addMember(String Name);

	/**
	 * Remove a member from the chat-channel
	 *
	 * @param Name
	 */
	public void removeMember(String Name);

	/**
	 * Check if the given player is a member of the chat-channel
	 *
	 * @param Name
	 * @return
	 */
	public boolean isMember(String Name);

	/**
	 * Check if the chat-channel allows messages whenever a users joins, and leaves
	 *
	 * @return true if join/leave messages are enabled, false otherwise
	 */
	public boolean allowJoinLeaveMessages();

	/**
	 * Get the permission required to join this channel (if any)
	 *
	 * @return Permission required to join if any is set, otherwise null.
	 */
	public String getChannelPermission();

	/**
	 * Check if the channel requires a permission
	 *
	 * @return true if there's a permissions requirement, false otherwise
	 */
	public boolean hasPermission();

	/**
	 * Does this channel require an invitation / elevated permissions in-order to join?
	 *
	 * @return
	 */
	public boolean isPrivate();

	/**
	 * Set the permission required to join / talk in the channel
	 *
	 * @param Permission
	 */
	public void setChannelPermission(String Permission);

	/**
	 * Set if the channel has join and leave messages
	 *
	 * @param Messages
	 */
	public void setHasJoinLeaveMessages(boolean Messages);

	/**
	 * Set whether this channel requires an invitation / elevated permission in-order to join or leave.
	 *
	 * @param Private
	 */
	public void setPrivate(boolean Private);

	/**
	 * Send a message to all members in the chat channel, from the defined sender
	 *
	 * @param Sender
	 * @param Message
	 */
	public void sendToMembers(String Sender, String Message);

	/**
	 * Get who created the chat-channel
	 *
	 * @return
	 */
	public String getCreator();

	/**
	 * Set the creator of the chat-channel
	 *
	 * @param channelCreator
	 */
	public void setCreator(String channelCreator);

	/**
	 * Get if the chat channel should never get deleted
	 *
	 * @return true if the channel is permanent, false otherwise
	 */
	public boolean isPermanent();

	/**
	 * Set whether the chat-channel is permanent or not
	 *
	 * @param Permanant
	 */
	public void setPermanent(boolean Permanant);
}
