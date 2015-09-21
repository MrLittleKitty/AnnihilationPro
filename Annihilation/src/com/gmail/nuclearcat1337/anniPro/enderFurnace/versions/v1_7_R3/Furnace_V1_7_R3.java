package com.gmail.nuclearcat1337.anniPro.enderFurnace.versions.v1_7_R3;

import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.IFurnace;
import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.ReflectionUtil;
import net.minecraft.server.v1_7_R3.Block;
import net.minecraft.server.v1_7_R3.Blocks;
import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.TileEntityFurnace;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_7_R3.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;

class Furnace_V1_7_R3 extends TileEntityFurnace implements IFurnace
{
    private EntityPlayer owningPlayer;
	private Furnace furnace;

	public Furnace_V1_7_R3(Player p)
	{
		EntityPlayer player = ((CraftPlayer) p).getHandle();
		this.owningPlayer = player;
		this.world = player.world;
		try
		{
			ReflectionUtil.setSuperValue(this, "o", "Ender Furnace");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean a(EntityHuman entityhuman)
	{
		//Bukkit.getLogger().info("a");
		return true;
		//super.onOpen(who);
	}

//	@Override
//	public void onOpen(CraftHumanEntity e)
//	{
//		Bukkit.getLogger().info("onOpen");
//		super.onOpen(e);
//	}
//
//	@Override
//	public void onClose(CraftHumanEntity e)
//	{
//		Bukkit.getLogger().info("onClose");
//		super.onOpen(e);
//	}

	@Override
	public int p()
	{
		//Bukkit.getLogger().info("p");
		return 0;
	}

	@Override
	public Block q()
	{
		//super.
		//Bukkit.getLogger().info("q");
		return Blocks.FURNACE;
	}

	@Override
	public InventoryHolder getOwner()
	{
		//Bukkit.getLogger().info("getOwner");
//		int x = 0;
//		org.bukkit.block.Block b = this.world.getWorld().getBlockAt(x, 0, 0);
//		while(b != null && b.getType() != Material.AIR)
//			b = this.world.getWorld().getBlockAt(++x,0,0);
//		Furnace furnace = new CraftFurnace(b);
		furnace = new CraftFurnace(this.world.getWorld().getBlockAt(0, 0, 0));
		try
		{
			ReflectionUtil.setValue(furnace, "furnace", this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return furnace;
	}

	@Override
	public void open()
	{
		//Bukkit.getLogger().info("open");
//		Bukkit.getLogger().info("Owning players name is "+this.owningPlayer.getName());
//		try
//		{
//			Bukkit.getLogger().info(super.getClass().getField("furnace").toString());
//		}
//		catch (NoSuchFieldException | SecurityException e)
//		{
//
//			e.printStackTrace();
//		}
//		try
////		{
////			if(furnace == null)
////				this.getOwner();
//			//ReflectionUtil.setValue(furnace, "furnace", this);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		this.owningPlayer.openFurnace(this);
	}

	@Override
	public void tick()
	{
		h();
	}


	public void setItemStack(int i, ItemStack itemstack)
	{
		setItem(i, CraftItemStack.asNMSCopy(itemstack));
	}

	public ItemStack getItemStack(int i)
	{
		return CraftItemStack.asBukkitCopy(getItem(i));
	}

	@Override
	public FurnaceData getFurnaceData()
	{
		return new FurnaceData(this);
	}

    @Override
    public void load(final com.gmail.nuclearcat1337.anniPro.enderFurnace.api.FurnaceData data)
    {
        ItemStack[] items = data.getItems();
        for(int x = 0; x < 3; x++)
            this.setItem(x, org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack.asNMSCopy(items[x]));
        this.burnTime = data.getBurnTime();
        this.ticksForCurrentFuel = data.getTicksForCurrentFuel();
        this.cookTime = data.getCookTime();
    }
}
