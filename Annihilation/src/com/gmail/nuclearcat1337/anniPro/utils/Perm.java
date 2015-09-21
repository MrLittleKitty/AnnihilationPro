package com.gmail.nuclearcat1337.anniPro.utils;

public class Perm implements Comparable<Perm>
{
	public final String perm;
	public final double multiplier;
	
	public Perm(String perm, int mult)
	{
		this.perm = perm;
		this.multiplier = mult;
	}

	@Override
	public int compareTo(Perm arg0)
	{
		return (int)Double.compare(multiplier, arg0.multiplier);
	}
}
