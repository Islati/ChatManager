package ChatManager.Handlers.Chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ChatManager.ChatManager;
import ChatManager.Events.Handler.*;
import ChatManager.Events.*;
import ChatManager.Handlers.Chat.Channels.ChannelInvitation;
import ChatManager.Handlers.Chat.Channels.ChatChannel;
import ChatManager.Handlers.Player.PlayerHandler;
import ChatManager.Handlers.Player.cPlayer;
import ChatManager.Handlers.Util.Cooldown;

import java.util.*;

public class ChannelHandler
{
	private Map<String, ChatChannel> ChatChannelList = new HashMap<String, ChatChannel>();
	private Map<String, ChannelInvitation> ChannelInvitations = new HashMap<String, ChannelInvitation>();
	private Cooldown InvitationCooldown = new Cooldown(25);

	/**
	 * 
	 */
    public ChannelHandler()
    {
    	ChatChannel Global = new ChatChannel("GLOBAL","");
    	Global.setJoinLeaveMessages(false);
    	Global.setPermanant(true);
	    this.addChannel(Global);
    }

    /**
     * 
     * @param Channels
     */
	public void addChannel(ChatChannel... Channels)
	{
		for (ChatChannel ChatChannel : Channels)
		{
			this.ChatChannelList.put(ChatChannel.getName(),ChatChannel);
		}
	}

	/**
	 * 
	 * @param Name
	 * @return
	 */
	public boolean isChannel(String Name)
	{
		return this.ChatChannelList.containsKey(Name);
	}

	/**
	 * 
	 * @param Channel
	 * @return
	 */
	public boolean isChannel(ChatChannel Channel)
	{
		return this.isChannel(Channel.getName());
	}

	/**
	 * 
	 * @param Channel
	 */
	public void removeChannel(ChatChannel Channel)
	{
		Channel.sendToMembers(ChatColor.YELLOW + "This channel has been deleted, you were moved to the global chat");
		for (String Member : Channel.getMembers())
		{
			if (PlayerHandler.hasData(Member))
			{
				cPlayer Player = PlayerHandler.getData(Member);
				Player.setChatChannel(ChatManager.GLOBAL_CHAT_CHANNEL);
			}
		}
		this.ChatChannelList.remove(Channel.getName());
	}

	/**
	 * 
	 * @param Channel
	 */
	public void removeChannel(String Channel)
	{
		if (isChannel(Channel))
		{
			this.removeChannel(this.ChatChannelList.get(Channel));
		}
	}

	/**
	 * 
	 * @param Name
	 * @return
	 */
	public ChatChannel getChannel(String Name)
	{
		return this.ChatChannelList.get(Name);
	}

	/**
	 * 
	 * @param Player
	 * @param Channel
	 * @return
	 */
	public boolean addPlayerToChannel(Player Player, ChatChannel Channel)
	{
		if (isChannel(Channel))
		{
			Channel.addMember(Player.getName());
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param Player
	 * @param Channel
	 * @return
	 */
	public boolean addPlayerToChannel(String Player, ChatChannel Channel)
	{
		if (isChannel(Channel))
		{
			Channel.addMember(Player);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param Player
	 * @return
	 */
	public boolean hasChannelInvitation(String Player)
	{
		return this.ChannelInvitations.containsKey(Player);
	}
	
	/**
	 * 
	 * @param Player
	 * @return
	 */
	public ChannelInvitation getInvitedChannel(String Player)
	{
		return this.ChannelInvitations.get(Player);
	}
	
	/**
	 * 
	 * @param Player
	 */
	public void removeChannelInvitation(String Player)
	{
		if (this.hasChannelInvitation(Player))
		{
			this.ChannelInvitations.remove(Player);
		}
	}
	
	/**
	 * 
	 * @param Channel
	 * @param Inviter
	 * @param Invited
	 */
	public boolean addChannelInvitation(String Channel, String Inviter, String Invited)
	{
		if (!this.isOnChannelInviteCooldown(Inviter))
		{
			this.ChannelInvitations.put(Invited, new ChannelInvitation(Channel,Inviter,Invited));
			this.setOnInviteCooldown(Inviter);
			return true;
		}
		return false;
	}
	
	public void setOnInviteCooldown(String Inviter)
	{
		this.InvitationCooldown.SetOnCooldown(Inviter);
	}
	
	public boolean isOnChannelInviteCooldown(String Inviter)
	{
		return this.InvitationCooldown.IsOnCooldown(Inviter);
	}
	
	public boolean acceptChannelInvitation(String Player)
	{
		if (PlayerHandler.hasData(Player))
		{
			cPlayer cPlayer = PlayerHandler.getData(Player);
			if (this.hasChannelInvitation(Player))
			{
				ChannelInvitation Invitation = this.getInvitedChannel(Player);
				if (!cPlayer.getChatChannel().equalsIgnoreCase(Invitation.getChannelName()))
				{
					ChatChannel CurrentChannel = ChatManager.ChannelHandler.getChannel(cPlayer.getChatChannel());
					ChannelLeaveEvent Event = new ChannelLeaveEvent(CurrentChannel,PlayerHandler.getPlayer(Player));
					ChannelEventHandler.HandleChannelLeaveEvent(Event);
					
					ChatManager.ChannelHandler.addPlayerToChannel(Event.getPlayer(), Invitation.getChannel());
					if (Invitation.getChannel().allowJoinLeaveMessages())
					{
						Invitation.getChannel().sendToMembers(ChatColor.GRAY + Player + " has joined the Channel!");
					}
					PlayerHandler.getData(Player).setChatChannel(Invitation.getChannelName());
					PlayerHandler.getPlayer(Player).sendMessage("You chat channel is now: " + Invitation.getChannelName());
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
}
