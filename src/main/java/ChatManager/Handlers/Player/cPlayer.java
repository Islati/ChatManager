package ChatManager.Handlers.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * User: Brandon
 */
public class cPlayer
{
	private String playerName = "";
	private String chatChannel = "GLOBAL";
	public cPlayer(String playerName)
	{
		this.playerName = playerName;
	}

	/**
	 *
	 * @param player
	 */
	public cPlayer(Player player)
	{
		this.playerName = player.getName();
	}

	public String getName()
	{
		return this.playerName;
	}

	public String getChatChannel()
	{
		return this.chatChannel;
	}

	/**
	 *
	 * @param chatChannel
	 */
	public void setChatChannel(String chatChannel)
	{
		this.chatChannel = chatChannel;
	}

	public boolean isOnline()
	{
		return Bukkit.getOfflinePlayer(this.playerName).isOnline();
	}
}
