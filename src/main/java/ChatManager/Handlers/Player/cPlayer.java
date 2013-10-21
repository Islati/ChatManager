package ChatManager.Handlers.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * User: Brandon
 */
public class cPlayer
{
	private String Name = "";
	private String ChatChannel = "GLOBAL";
	public cPlayer(String Name)
	{
		this.Name = Name;
	}

	public cPlayer(Player Player)
	{
		this.Name = Player.getName();
	}

	public String getName()
	{
		return this.Name;
	}

	public String getChatChannel()
	{
		return this.ChatChannel;
	}

	public void setChatChannel(String Channel)
	{
		this.ChatChannel = Channel;
	}

	public boolean isOnline()
	{
		return Bukkit.getOfflinePlayer(this.Name).isOnline();
	}
}
