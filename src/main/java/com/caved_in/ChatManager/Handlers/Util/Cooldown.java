package com.caved_in.chatmanager.handlers.util;

import java.util.HashMap;

public class Cooldown
{
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	private int cooldownTime = 0;

	public Cooldown(int cooldownTime)
	{
		this.cooldownTime = cooldownTime;
	}

	public void setOnCooldown(String playerName)
	{
		this.cooldowns.put(playerName, Long.valueOf(System.currentTimeMillis() / 1000L));
	}

	public void setCooldownTime(int cooldownTime)
	{
		this.cooldownTime = cooldownTime;
	}

	@Deprecated
	public double remainingSeconds(String playerName)
	{
		if (this.cooldowns.containsKey(playerName))
		{
			double Last_Used = this.cooldowns.get(playerName).longValue();
			long Time_Check = System.currentTimeMillis() / 1000L;
			return Time_Check - Last_Used;
		}

		return 0.0D;
	}

	@Deprecated
	public double remainingMinutes(String playerName)
	{
		if (this.cooldowns.containsKey(playerName))
		{
			double Last_Used = this.cooldowns.get(playerName).longValue();
			long Time_Check = System.currentTimeMillis() / 1000L;
			return (Time_Check - Last_Used) / 60.0D;
		}

		return 0.0D;
	}

	public boolean isOnCooldown(String playerName)
	{
		if (this.cooldowns.containsKey(playerName))
		{
			double playerTimeStamp = this.cooldowns.get(playerName).longValue();
			long timeStampSeconds = System.currentTimeMillis() / 1000L;
			return (timeStampSeconds - playerTimeStamp < this.cooldownTime);
		}
		return false;
	}
}