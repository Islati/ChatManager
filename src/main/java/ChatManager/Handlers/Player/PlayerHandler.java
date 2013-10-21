
package ChatManager.Handlers.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHandler
{
	private static Map<String, cPlayer> PlayerMap = new HashMap<String,cPlayer>();
    public PlayerHandler() { }

	public static boolean hasData(String Name)
	{
		return PlayerMap.containsKey(Name);
	}

	public static cPlayer getData(String Name)
	{
		return PlayerMap.get(Name);
	}

	public static void addData(String Name)
	{
		if (!hasData(Name))
		{
			PlayerMap.put(Name, new cPlayer(Name));
		}
	}

	public static void removeData(String Name)
	{
		if (hasData(Name))
		{
			PlayerMap.remove(Name);
		}
	}

    public static boolean isOnline(String Name)
    {
        return Bukkit.getOfflinePlayer(Name).isOnline();
    }
    
    public static Player getPlayer(String Name)
    {
        return Bukkit.getPlayer(Name);
    }
}
