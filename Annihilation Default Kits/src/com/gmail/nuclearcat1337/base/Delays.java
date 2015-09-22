package com.gmail.nuclearcat1337.base;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;


public class Delays
{
	private static Delays instance;
	public static Delays getInstance()
	{
		if(instance == null)
			instance = new Delays();
		return instance;
	}
	
	private int counter = 0;
	private final Map<String,Integer> delayIDs;
	private final Map<Integer,DelayUpdate> delayHandlers;
	private final List<Delay> delays;
	
	private Delays()
	{
		delays = new ArrayList<Delay>();
		delayIDs = new HashMap<String,Integer>();
		delayHandlers = new HashMap<Integer,DelayUpdate>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(AnnihilationMain.getInstance(), new DelayUpdater(this), 20L, 20L);
	}
	
	private class DelayUpdater implements Runnable
	{
		private final Delays d;
		public DelayUpdater(Delays d)
		{
			this.d = d;
		}
		
		@Override
		public void run()
		{
			Iterator<Delay> it = d.delays.iterator();
			while(it.hasNext())
			{
				//Bukkit.getLogger().info("This");
				Delay d = it.next();
				if(d.update() <= 0)
				{
				///	Bukkit.getLogger().info("That");
					it.remove();
				}
			}
		}
	}
	
	public void createNewDelay(String delayName, DelayUpdate handler)
	{
		if(!delayIDs.containsKey(delayName.toLowerCase()))
		{
			delayIDs.put(delayName.toLowerCase(), counter);
			delayHandlers.put(counter, handler);
			counter++;
		}
	}
	
	public boolean hasActiveDelay(Player p, String delayType)
	{
		for(Delay d : delays)
		{
			if(d.playerID.equals(p.getUniqueId()) && d.DelayID == this.delayIDs.get(delayType.toLowerCase()) && System.currentTimeMillis() < d.endTime)
				return true;
		}
		return false;
	}
	
	public void addDelay(Player p, long endTime, String delay)
	{
		Integer id = delayIDs.get(delay.toLowerCase());
		Delay d = new Delay(p.getUniqueId(),id,endTime,delayHandlers.get(id));
		delays.add(d);
	}
	
	private class Delay
	{
		private UUID playerID;
		private int DelayID;
		private long endTime;
		private DelayUpdate handler;
		
		public Delay(UUID playerID, int ID, long end, DelayUpdate handler)
		{
			this.playerID = playerID;
			this.DelayID = ID;
			this.endTime = end;
			this.handler = handler;
		}
		
		public int update()
		{
			Player p = Bukkit.getPlayer(playerID);
			if(p != null)
			{
				int seconds = (int)((endTime-System.currentTimeMillis())/1000);
				handler.update(p, seconds);
				return seconds;
			}
			return 0;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + DelayID;
			result = prime * result + ((playerID == null) ? 0 : playerID.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Delay other = (Delay) obj;
			if (DelayID != other.DelayID)
				return false;
			if (playerID == null)
			{
				if (other.playerID != null)
					return false;
			}
			else if (!playerID.equals(other.playerID))
				return false;
			return true;
		}

	}
}
