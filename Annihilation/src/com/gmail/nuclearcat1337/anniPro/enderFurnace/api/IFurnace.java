package com.gmail.nuclearcat1337.anniPro.enderFurnace.api;

public interface IFurnace
{	
	void tick();
	
	void open();
	
	FurnaceData getFurnaceData();
	
	void load(FurnaceData data);
}
