package com.gmail.nuclearcat1337.anniPro.anniMap;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;

public final class SignType
{
	public static SignType Weapon = new SignType((byte)1,null);
	public static SignType Brewing = new SignType((byte)2,null); 
	public static SignType Team = new SignType((byte)3,null);
	
	public static SignType newTeamSign(AnniTeam team)
	{
		return new SignType((byte)3,team);
	}
	
	private byte ID;
	private AnniTeam team;
	private SignType(byte ID, AnniTeam team)
	{
		this.ID = ID;
		this.team = team;
	}
	
	public AnniTeam getTeam()
	{
		return this.team;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
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
		SignType other = (SignType) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	
}
