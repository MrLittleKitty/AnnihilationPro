package com.gmail.nuclearcat1337.anniPro.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.google.common.base.Predicate;

public class IDTools
{
	public static void getName(final UUID ID, final Predicate<String> callback)
	{
		Player p = Bukkit.getPlayer(ID);
		if(p != null)
		{
			callback.apply(p.getName());
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(AnnihilationMain.getInstance(), new Runnable(){
			@Override
			public void run()
			{
				String line = null;
				try
				{
					URL url = new URL("https://api.mojang.com/user/profiles/"
									+ ID.toString().replaceAll("-", "") + "/names");
					BufferedReader in = new BufferedReader(new InputStreamReader(
							url.openStream()));
					line = in.readLine();
					line = line.replace("[\"", "");
					line = line.replace("\"]", "");
				}
				catch (Exception ex)
				{
					//ex.printStackTrace();
				}
				final String s = line;
				Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable(){
					@Override
					public void run()
					{
						callback.apply(s);
					}});
			}});
	}
	
	public static void getUUID(final String player, final Predicate<UUID> callback)
	{
		@SuppressWarnings("deprecation")
		Player p = Bukkit.getPlayer(player);
		if(p != null)
		{
			callback.apply(p.getUniqueId());
			return;
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(AnnihilationMain.getInstance(), new Runnable(){
			@Override
			public void run()
			{
				String uuid = null;
				try
				{
					URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+player);
					BufferedReader in = new BufferedReader(new InputStreamReader(
							url.openStream()));
					String Line;
					while ((Line = in.readLine()) != null)
					{
						uuid = Line.substring(7, 39);
						uuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-"
								+ uuid.substring(12, 16) + "-" + uuid.substring(16, 20)
								+ "-" + uuid.substring(20, 32);
						break;
					}
					in.close();
				}
				catch (Exception ex)
				{
					//ex.printStackTrace();
				}
				UUID id = null;
				try
				{
					id = UUID.fromString(uuid);
				}
				catch(Exception e)
				{
					id = null;
				}
				final UUID d = id;
				Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable(){
					@Override
					public void run()
					{
						callback.apply(d);
					}});
			}});
	}		

//	private static String getUUIDs(String player)
//	{
//		try
//		{	
//			//URL url = new URL("[url]https://api.mojang.com/users/profiles/minecraft/"+player+"[/url]");
//			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+player);
//			BufferedReader in = new BufferedReader(new InputStreamReader(
//					url.openStream()));
//			String Line;
//			while ((Line = in.readLine()) != null)
//			{
//				String uuid = Line.substring(7, 39);
//				return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-"
//						+ uuid.substring(12, 16) + "-" + uuid.substring(16, 20)
//						+ "-" + uuid.substring(20, 32);
//			}
//			in.close();
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		return null;
//	}

	public static void getName(String uuid, Predicate<String> callback)
	{
		getName(UUID.fromString(uuid),callback);
	}
}

