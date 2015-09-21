package com.gmail.nuclearcat1337.anniPro.enderFurnace.api;


import net.minecraft.server.v1_8_R2.TileEntityFurnace;
import org.bukkit.inventory.ItemStack;

public abstract class FurnaceData
{
    private ItemStack[] items;
    private int burnTime;
    private int ticksForCurrentFuel;
    private int cookTime;


    /**
     *EntityFurnace getProperty() IDS
     *0 = burn time
     *1 = ticks for current fuel
     *2 = cook time
     *3 = cook time total
     * @param burnTime "getProperty(0)"
     * @param ticksForCurrentFuel "getProperty(1)"
     * @param cookTime "getProperty(2)"
     */
    public FurnaceData(ItemStack[] items, int burnTime, int ticksForCurrentFuel, int cookTime)
    {
        this.items = items;
        this.burnTime = burnTime;
        this.ticksForCurrentFuel = ticksForCurrentFuel;
        this.cookTime = cookTime;
    }

    public int getBurnTime()
    {
        return burnTime;
    }

    public int getTicksForCurrentFuel()
    {
        return ticksForCurrentFuel;
    }

    public int getCookTime()
    {
        return cookTime;
    }

    public ItemStack[] getItems()
    {
        return items;
    }

//
//    public FurnaceData(net.minecraft.server.v1_8_R2.TileEntityFurnace furnace)
//    {
//        this.items = new ItemStack[3];
//        for(int x = 0; x < 3; x++)
//            this.items[x] = org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack.asBukkitCopy(furnace.getItem(x));
//        this.burnTime = furnace.burnTime;
//        this.ticksForCurrentFuel = furnace.getProperty(1); //gets the ticksForCurrentFuel
//        this.cookTime = furnace.cookTime;
//    }
//
//    public void load(net.minecraft.server.v1_8_R2.TileEntityFurnace furnace)
//    {
//        for(int x = 0; x < 3; x++)
//            furnace.setItem(x,  org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack.asNMSCopy(items[x]));
//        furnace.burnTime = this.burnTime;
//        furnace.b(1,ticksForCurrentFuel);
//        furnace.cookTime = this.cookTime;
//    }
    
//	public FurnaceData(net.minecraft.server.v1_7_R3.TileEntityFurnace furnace)
//	{
//		this.items = new ItemStack[3];
//		for(int x = 0; x < 3; x++)
//			this.items[x] = org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack.asBukkitCopy(furnace.getItem(x));
//		this.burnTime = furnace.burnTime;
//		this.ticksForCurrentFuel = furnace.ticksForCurrentFuel;
//		this.cookTime = furnace.cookTime;
//	}
//
//	public FurnaceData(net.minecraft.server.v1_7_R4.TileEntityFurnace furnace)
//	{
//		this.items = new ItemStack[3];
//		for(int x = 0; x < 3; x++)
//			this.items[x] = org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asBukkitCopy(furnace.getItem(x));
//		this.burnTime = furnace.burnTime;
//		this.ticksForCurrentFuel = furnace.ticksForCurrentFuel;
//		this.cookTime = furnace.cookTime;
//	}
//
//	public void load(net.minecraft.server.v1_7_R3.TileEntityFurnace furnace)
//	{
//		for(int x = 0; x < 3; x++)
//			furnace.setItem(x,  org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack.asNMSCopy(items[x]));
//		furnace.burnTime = this.burnTime;
//		furnace.ticksForCurrentFuel = this.ticksForCurrentFuel;
//		furnace.cookTime = this.cookTime;
//	}
//
//	public void load(net.minecraft.server.v1_7_R4.TileEntityFurnace furnace)
//	{
//		for(int x = 0; x < 3; x++)
//			furnace.setItem(x,  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asNMSCopy(items[x]));
//		furnace.burnTime = this.burnTime;
//		furnace.ticksForCurrentFuel = this.ticksForCurrentFuel;
//		furnace.cookTime = this.cookTime;
//	}
}
