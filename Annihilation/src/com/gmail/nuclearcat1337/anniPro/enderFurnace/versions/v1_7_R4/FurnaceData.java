package com.gmail.nuclearcat1337.anniPro.enderFurnace.versions.v1_7_R4;

import net.minecraft.server.v1_7_R4.TileEntityFurnace;

class FurnaceData extends com.gmail.nuclearcat1337.anniPro.enderFurnace.api.FurnaceData
{
    public FurnaceData(TileEntityFurnace furnace)
    {
        super(asBukkitCopy(furnace.getContents()),furnace.burnTime,furnace.ticksForCurrentFuel,furnace.cookTime);
    }

    private static org.bukkit.inventory.ItemStack[] asBukkitCopy(net.minecraft.server.v1_7_R4.ItemStack[] stacks)
    {
        org.bukkit.inventory.ItemStack[] items = new org.bukkit.inventory.ItemStack[stacks.length];
        for(int i = 0; i < items.length; i++)
        {
            items[i] = org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asBukkitCopy(stacks[i]);
        }
        return items;
    }
}
