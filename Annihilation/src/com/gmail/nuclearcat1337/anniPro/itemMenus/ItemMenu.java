package com.gmail.nuclearcat1337.anniPro.itemMenus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

/**
 * A Menu controlled by ItemStacks in an Inventory.
 */
public class ItemMenu
{
	// private JavaPlugin plugin;
	private String name;
	private Size size;
	private MenuItem[] items;
	private ItemMenu parent;

	/**
	 * The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.StaticMenuItem} that appears in empty
	 * slots if {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem#fillEmptySlots()} is
	 * called.
	 */
	@SuppressWarnings("deprecation")
	private static final MenuItem EMPTY_SLOT_ITEM = new StaticMenuItem(" ",
			new ItemStack(Material.STAINED_GLASS_PANE, 1,
					DyeColor.GRAY.getData()));

	/**
	 * Creates an {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 *
	 * @param name
	 *            The name of the inventory.
	 * @param size
	 *            The {@link com.gmail.nuclearcat1337.itemMenu.Size} of the
	 *            inventory.
	 * @param parent
	 *            The ItemMenu's parent.
	 */
	public ItemMenu(String name, Size size, ItemMenu parent)
	{
		// this.plugin = plugin;
		this.name = name;
		this.size = size;
		this.items = new MenuItem[size.getSize()];
		this.parent = parent;
	}

	/**
	 * Creates an {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} with no parent.
	 *
	 * @param name
	 *            The name of the inventory.
	 * @param size
	 *            The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem.Size} of the
	 *            inventory.
	 * @param plugin
	 *            The Plugin instance.
	 */
	public ItemMenu(String name, Size size)
	{
		this(name, size, null);
	}

	/**
	 * Gets the name of the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 *
	 * @return The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}'s name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem.Size} of the
	 * {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 *
	 * @return The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}'s
	 *         {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem.Size}.
	 */
	public Size getSize()
	{
		return size;
	}

	/**
	 * Checks if the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} has a parent.
	 *
	 * @return True if the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} has a
	 *         parent, else false.
	 */
	public boolean hasParent()
	{
		return parent != null;
	}

	/**
	 * Gets the parent of the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 *
	 * @return The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}'s parent.
	 */
	public ItemMenu getParent()
	{
		return parent;
	}

	/**
	 * Sets the parent of the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 *
	 * @param parent
	 *            The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}'s parent.
	 */
	public void setParent(ItemMenu parent)
	{
		this.parent = parent;
	}

	/**
	 * Sets the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} of a slot.
	 *
	 * @param position
	 *            The slot position.
	 * @param menuItem
	 *            The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 * @return The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 */
	public ItemMenu setItem(int position, MenuItem menuItem)
	{
		items[position] = menuItem;
		return this;
	}
	
	public ItemMenu clearItem(int position)
	{
		items[position] = null;
		return this;
	}
	
	public ItemMenu clearAllItems()
	{
		Arrays.fill(items, null);
		return this;
	}

	/**
	 * Fills all empty slots in the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}
	 * with a certain {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 *
	 * @param menuItem
	 *            The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 * @return The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 */
	public ItemMenu fillEmptySlots(MenuItem menuItem)
	{
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] == null)
			{
				items[i] = menuItem;
			}
		}
		return this;
	}

	/**
	 * Fills all empty slots in the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}
	 * with the default empty slot item.
	 *
	 * @return The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 */
	public ItemMenu fillEmptySlots()
	{
		return fillEmptySlots(EMPTY_SLOT_ITEM);
	}

	/**
	 * Opens the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} for a player.
	 *
	 * @param player
	 *            The player.
	 */
	public void open(Player player)
	{
		if (!ItemMenuListener.getInstance().isRegistered(
				AnnihilationMain.getInstance()))
		{
			ItemMenuListener.getInstance().register(
					AnnihilationMain.getInstance());
		}
		Inventory inventory = Bukkit.createInventory(new ItemMenuHolder(this,
				Bukkit.createInventory(player, size.getSize())),
				size.getSize(), name);
		apply(inventory, player);
		player.openInventory(inventory);
	}

	/**
	 * Updates the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} for a player.
	 *
	 * @param player
	 *            The player to update the
	 *            {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} for.
	 */
	@SuppressWarnings("deprecation")
	public void update(Player player)
	{
		if (player.getOpenInventory() != null)
		{
			Inventory inventory = player.getOpenInventory().getTopInventory();
			if (inventory.getHolder() instanceof ItemMenuHolder
					&& ((ItemMenuHolder) inventory.getHolder()).getMenu()
							.equals(this))
			{
				apply(inventory, player);
				player.updateInventory();
			}
		}
	}

	/**
	 * Applies the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem} for a player to an
	 * Inventory.
	 *
	 * @param inventory
	 *            The Inventory.
	 * @param player
	 *            The Player.
	 */
	private void apply(Inventory inventory, Player player)
	{
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] != null)
			{
				inventory.setItem(i, items[i].getFinalIcon(player));
			}
		}
	}

	/**
	 * Handles InventoryClickEvents for the
	 * {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 */
	@SuppressWarnings("deprecation")
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT)
		{
			int slot = event.getRawSlot();
			if (slot >= 0 && slot < size.getSize() && items[slot] != null)
			{
				Player player = (Player) event.getWhoClicked();
				ItemClickEvent itemClickEvent = new ItemClickEvent(player,event.getCurrentItem(),event.getClick());
				items[slot].onItemClick(itemClickEvent);
				if (itemClickEvent.willUpdate())
				{
					update(player);
				}
				else
				{
					player.updateInventory();
					if (itemClickEvent.willClose()
							|| itemClickEvent.willGoBack())
					{
						final String playerName = player.getName();
						Bukkit.getScheduler().scheduleSyncDelayedTask(
								AnnihilationMain.getInstance(), new Runnable()
								{
									public void run()
									{
										Player p = Bukkit
												.getPlayerExact(playerName);
										if (p != null)
										{
											p.closeInventory();
										}
									}
								}, 1);
					}
					if (itemClickEvent.willGoBack() && hasParent())
					{
						final String playerName = player.getName();
						Bukkit.getScheduler().scheduleSyncDelayedTask(
								AnnihilationMain.getInstance(), new Runnable()
								{
									public void run()
									{
										Player p = Bukkit
												.getPlayerExact(playerName);
										if (p != null)
										{
											parent.open(p);
										}
									}
								}, 3);
					}
				}
			}
		}
	}

	/**
	 * Destroys the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 */
	public void destroy()
	{
		name = null;
		size = null;
		items = null;
		parent = null;
	}

	/**
	 * Possible sizes of an {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
	 */
	public enum Size
	{
		ONE_LINE(9), TWO_LINE(18), THREE_LINE(27), FOUR_LINE(36), FIVE_LINE(45), SIX_LINE(
				54);

		private final int size;

		private Size(int size)
		{
			this.size = size;
		}

		/**
		 * Gets the {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem.Size}'s amount of
		 * slots.
		 *
		 * @return The amount of slots.
		 */
		public int getSize()
		{
			return size;
		}

		/**
		 * Gets the required {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem.Size} for
		 * an amount of slots.
		 *
		 * @param slots
		 *            The amount of slots.
		 * @return The required {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem.Size}.
		 */
		public static Size fit(int slots)
		{
			if (slots < 10)
			{
				return ONE_LINE;
			}
			else if (slots < 19)
			{
				return TWO_LINE;
			}
			else if (slots < 28)
			{
				return THREE_LINE;
			}
			else if (slots < 37)
			{
				return FOUR_LINE;
			}
			else if (slots < 46)
			{
				return FIVE_LINE;
			}
			else
			{
				return SIX_LINE;
			}
		}
	}
}