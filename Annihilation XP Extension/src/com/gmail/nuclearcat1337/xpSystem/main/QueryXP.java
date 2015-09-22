package com.gmail.nuclearcat1337.xpSystem.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.gmail.nuclearcat1337.xpSystem.database.AsyncQuery;
import com.gmail.nuclearcat1337.xpSystem.utils.Acceptor;

class QueryXP implements AsyncQuery
{
	private final UUID playerID;
	private final Acceptor<Integer> acceptor;
	
	private int xp = 0;
	
	public QueryXP(UUID playerID, Acceptor<Integer> acceptor)
	{
		assert playerID != null;
		assert acceptor != null;
		
		this.playerID = playerID;
		this.acceptor = acceptor;
	}
	
	@Override
	public void run()
	{
		acceptor.accept(xp);
	}

	@Override
	public boolean isCallback()
	{
		return true;
	}

	@Override
	public String getQuerey()
	{
		return "SELECT * FROM tbl_player_xp WHERE ID='"+playerID.toString()+"'";
		//return "INSERT INTO tbl_player_xp (ID, XP) VALUES ('"+p.ID.toString()+"', "+XP+") ON DUPLICATE KEY UPDATE XP=XP+VALUES(XP);";
	}

	@Override
	public void setResult(ResultSet set)
	{
		try
		{
			if(set.next())
			{
				xp = set.getInt("XP");
				set.close();
			}
		}
		catch (SQLException e)
		{	
			e.printStackTrace();
		}
	}
}
