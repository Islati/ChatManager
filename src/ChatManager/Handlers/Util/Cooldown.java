package ChatManager.Handlers.Util;

import java.util.HashMap;

public class Cooldown
{
	private HashMap<String, Long> Cooldowns = new HashMap<String, Long>();
	private int _CooldownTime = 0;

	public Cooldown(int CooldownTime)
	{
		this._CooldownTime = CooldownTime;
	}

	public void SetOnCooldown(String Player)
	{
		this.Cooldowns.put(Player, Long.valueOf(System.currentTimeMillis() / 1000L));
	}

	public void SetCooldownTime(int CooldownTime)
	{
		this._CooldownTime = CooldownTime;
	}

	public double RemainingSeconds(String Player)
	{
		if (this.Cooldowns.containsKey(Player))
		{
			double Last_Used = this.Cooldowns.get(Player).longValue();
			long Time_Check = System.currentTimeMillis() / 1000L;
			return Time_Check - Last_Used;
		}

		return 0.0D;
	}

	public double RemainingMinutes(String Player)
	{
		if (this.Cooldowns.containsKey(Player))
		{
			double Last_Used = this.Cooldowns.get(Player).longValue();
			long Time_Check = System.currentTimeMillis() / 1000L;
			return (Time_Check - Last_Used) / 60.0D;
		}

		return 0.0D;
	}

	public boolean IsOnCooldown(String Player)
	{
		if (this.Cooldowns.containsKey(Player))
		{
			double Last_Used = this.Cooldowns.get(Player).longValue();
			long Time_Check = System.currentTimeMillis() / 1000L;
			if (Time_Check - Last_Used < this._CooldownTime)
			{
				return true;
			}

			return false;
		}

		return false;
	}
}