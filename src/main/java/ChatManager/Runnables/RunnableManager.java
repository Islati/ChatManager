package ChatManager.Runnables;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RunnableManager
{
	private JavaPlugin Plugin;
	private HashMap<String, Integer> Tasks = new HashMap<String, Integer>();
	
	public RunnableManager(JavaPlugin Plugin)
	{
		this.Plugin = Plugin;
	}
	
	public void RegisterSynchRepeatTask(String Name, Runnable Task, long DelayInTicks, long RepeatTimeInTicks)
	{
		if (!Tasks.containsKey(Name))
		{
			Tasks.put(Name, this.Plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.Plugin, Task, DelayInTicks, RepeatTimeInTicks));
		}
	}
	
	public void RegisterASynchRepeatTask(String Name, Runnable Task, long DelayInTicks, long RepeatTimeInTicks)
	{
		if (!Tasks.containsKey(Name))
		{
			Tasks.put(Name, this.Plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(this.Plugin, Task, DelayInTicks, RepeatTimeInTicks));
		}
	}
	
	public void RunTaskNow(Runnable Task)
	{
		this.Plugin.getServer().getScheduler().runTask(this.Plugin, Task);
	}
	
	public void RunTaskAsynch(Runnable Task)
	{
		this.Plugin.getServer().getScheduler().runTaskAsynchronously(this.Plugin, Task);
	}
	
	public void RunTaskLater(Runnable Task, long DelayInTicks)
	{
		this.Plugin.getServer().getScheduler().runTaskLater(this.Plugin, Task, DelayInTicks);
	}
	
	public void RunTaskLaterAsynch(Runnable Task, long Delay)
	{
		this.Plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.Plugin, Task, Delay);
	}
	
	public boolean CancelTask(String Name)
	{
		if (this.Tasks.containsKey(Name))
		{
			Bukkit.getScheduler().cancelTask(this.Tasks.get(Name));
			this.Tasks.remove(Name);
			return true;
		}
		return false;
	}

}
