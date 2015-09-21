package com.gmail.nuclearcat1337.anniPro.mapBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class FutureBlockReplace implements Runnable
{
	//private final Block	b;
	private final BlockState state;
	
	public FutureBlockReplace(Block b)
	{
		this.state = b.getState();		
	}

	public FutureBlockReplace(Block b, boolean cobble)
	{
		this.state = b.getState();
		b.setType(cobble? Material.COBBLESTONE:Material.AIR);
	}

	public void run()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Annihilation"), new Runnable()
		{
			@Override
			public void run()
			{
			//	Bukkit.getLogger().info("RAWR "+state.getType().toString());
				state.update(true);
			}	
		}
		);	
	}
}