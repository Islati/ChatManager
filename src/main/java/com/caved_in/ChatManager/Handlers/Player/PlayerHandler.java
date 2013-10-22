
package com.caved_in.chatmanager.handlers.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerHandler
{
	private static Map<String, cPlayer> playerData = new HashMap<String,cPlayer>();
    public PlayerHandler() { }

	public static boolean hasData(String playerName)
	{
		return playerData.containsKey(playerName);
	}

	public static cPlayer getData(String playerName)
	{
		return playerData.get(playerName);
	}

	public static void addData(String playerName)
	{
		if (!hasData(playerName))
		{
			playerData.put(playerName, new cPlayer(playerName));
		}
	}

	public static void removeData(String playerName)
	{
		if (hasData(playerName))
		{
			playerData.remove(playerName);
		}
	}

    public static boolean isOnline(String playerName)
    {
        return Bukkit.getOfflinePlayer(playerName).isOnline();
    }
    
    public static Player getPlayer(String playerName)
    {
        return Bukkit.getPlayer(playerName);
    }
}
