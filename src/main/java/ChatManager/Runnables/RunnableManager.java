package ChatManager.Runnables;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RunnableManager
{
	private JavaPlugin plugin;
	private HashMap<String, Integer> managedTasks = new HashMap<String, Integer>();
	
	public RunnableManager(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public void registerSyncRepeatingTask(String taskName, Runnable task, long delayInTicks, long repeatTimeInTicks)
	{
		if (!managedTasks.containsKey(taskName))
		{
			managedTasks.put(taskName, this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, task, delayInTicks, repeatTimeInTicks));
		}
	}
	
	public void registerAsyncRepeatingTask(String taskName, Runnable task, long delayInTicks, long repeatTimeInTicks)
	{
		if (!managedTasks.containsKey(taskName))
		{
			managedTasks.put(taskName, this.plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(this.plugin, task, delayInTicks, repeatTimeInTicks));
		}
	}
	
	public void runTaskNow(Runnable taskToRun)
	{
		this.plugin.getServer().getScheduler().runTask(this.plugin, taskToRun);
	}
	
	public void runTaskAsync(Runnable taskToRun)
	{
		this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, taskToRun);
	}
	
	public void runTaskLater(Runnable task, long delayInTicks)
	{
		this.plugin.getServer().getScheduler().runTaskLater(this.plugin, task, delayInTicks);
	}
	
	public void runTaskLaterAsynch(Runnable task, long delayInTicks)
	{
		this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, task, delayInTicks);
	}
	
	public boolean cancelTask(String taskName)
	{
		if (this.managedTasks.containsKey(taskName))
		{
			Bukkit.getScheduler().cancelTask(this.managedTasks.get(taskName));
			this.managedTasks.remove(taskName);
			return true;
		}
		return false;
	}

}
