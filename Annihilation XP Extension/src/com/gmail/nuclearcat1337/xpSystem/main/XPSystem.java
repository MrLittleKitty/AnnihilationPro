package com.gmail.nuclearcat1337.xpSystem.main;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.Kit;
import com.gmail.nuclearcat1337.xpSystem.database.AsyncLogQuery;
import com.gmail.nuclearcat1337.xpSystem.database.Database;
import com.gmail.nuclearcat1337.xpSystem.utils.Acceptor;

public class XPSystem
{
	private Database database = null;
	
	public XPSystem(ConfigurationSection databaseSection)
	{
		assert databaseSection != null;
		database = loadDatabase(databaseSection);
		if(database != null)
		{
			try
			{
				database.updateSQL("CREATE TABLE IF NOT EXISTS tbl_player_xp (ID VARCHAR(40), XP INTEGER, UNIQUE (ID))");
				database.updateSQL("CREATE TABLE IF NOT EXISTS tbl_player_kits (ID VARCHAR(40), Kit VARCHAR(20))");
			}
			catch(Throwable t)
			{
				database = null;
			}
		}
		
	}
	
	public boolean isActive()
	{
		return database != null;
	}
	
	public void disable()
	{
		if(database != null)
			database.closeConnection();
		database = null;
	}
	
	private Database loadDatabase(ConfigurationSection section)
	{
		assert section != null;
		
		String host = section.getString("Host");
		String port = section.getString("Port");
		String data = section.getString("Database");
		String username = section.getString("Username");
		String password = section.getString("Password");
		return Database.getMySQLDatabase(host, port, data, username, password);
	}
	
	public void giveXP(final UUID playerID, final int XP)
	{
		if(XP > 0)
		{
			database.addNewAsyncLogQuery(new AsyncLogQuery()
			{
				@Override
				public String getQuery()
				{
					return "INSERT INTO tbl_player_xp (ID, XP) VALUES ('"+playerID.toString()+"', "+XP+") ON DUPLICATE KEY UPDATE XP=XP+VALUES(XP);";
				}
			});	
		}
	}
	
	public void removeXP(final UUID playerID, final int XP)
	{
		if(XP > 0)
		{
			database.addNewAsyncLogQuery(new AsyncLogQuery()
			{
				@Override
				public String getQuery()
				{
					return "INSERT INTO tbl_player_xp (ID, XP) VALUES ('"+playerID.toString()+"', "+XP+") ON DUPLICATE KEY UPDATE XP=XP-VALUES(XP);";
				}
			});	
		}
	}
	
	public void getXP(UUID playerID, Acceptor<Integer> acceptor)
	{
		database.addNewAsyncQuery(new QueryXP(playerID,acceptor));
	}
	
	public void loadKits(AnniPlayer player, Acceptor<AnniPlayer> postLoad)
	{
		database.addNewAsyncQuery(new LoadKits(player,postLoad));
	}
	
	@SuppressWarnings("unchecked")
	public void removeKit(final UUID id, final Kit kit)
	{
		AnniPlayer player = AnniPlayer.getPlayer(id);
		if(player != null)
		{
			Object obj = player.getData("Kits");
			if(obj != null && obj instanceof List)
			{
				List<String> str = (List<String>)obj;
				str.remove(kit.getName().toLowerCase());
			}
		}
		database.addNewAsyncLogQuery(new AsyncLogQuery(){
			@Override
			public String getQuery()
			{
				//return "INSERT INTO tbl_player_kits (ID,Kit) VALUES ('"+player.toString()+"','"+kit+"');";
				return "DELETE FROM tbl_player_kits WHERE ID='"+id.toString()+"' AND Kit='"+kit.getName()+"';";
			}});
	}
	
	@SuppressWarnings("unchecked")
	public void addKit(final UUID id, final Kit kit)
	{
		AnniPlayer player = AnniPlayer.getPlayer(id);
		if(player != null)
		{
			Object obj = player.getData("Kits");
			if(obj != null && obj instanceof List)
			{
				((List<String>)obj).add(kit.getName().toLowerCase());
			}
		}
		
		database.addNewAsyncLogQuery(new AsyncLogQuery(){
			@Override
			public String getQuery()
			{
				return "INSERT INTO tbl_player_kits (ID,Kit) VALUES ('"+id.toString()+"','"+kit.getName()+"');";
			}});
	}
}
