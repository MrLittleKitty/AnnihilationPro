package com.gmail.nuclearcat1337.anniPro.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
 
public class ShopMenu
{
	private static final ItemMenu weapon;
	private static final ItemMenu brewing;
	static
	{
		weapon = new ItemMenu("Weapon Shop",Size.TWO_LINE);
		brewing = new ItemMenu("Brewing Shop",Size.THREE_LINE);
		buildBrewingShop(brewing);
		buildWeaponShop(weapon);
	}


	private static void buildWeaponShop(ItemMenu menu)
	{
		for(int x = 0; x < 27; x++)
		{
			ItemStack icon = null;
			int cost = 0;
			boolean cat = true;
			switch(x)
			{
				//TODO----------------MAKE SURE ALL THE COSTS ARE CORRECT
				//LINE 1
				default:
					cat = false;
					break;

				case 0:
					icon = new ItemStack(Material.CHAINMAIL_HELMET,1);
					cost = 10;
					break;
				case 1:
					icon = new ItemStack(Material.CHAINMAIL_CHESTPLATE,1);
					cost = 10;
					break;
				case 2:
					icon = new ItemStack(Material.CHAINMAIL_LEGGINGS,1);
					cost = 10;
					break;
				case 3:
					icon = new ItemStack(Material.CHAINMAIL_BOOTS,1);
					cost = 10;
					break;
				case 4:
					icon = new ItemStack(Material.IRON_SWORD,1);
					cost = 5;
					break;
				case 5:
					icon = new ItemStack(Material.BOW,1);
					cost = 5;
					break;
				case 6:
					icon = new ItemStack(Material.ARROW,16);
					cost = 5;
					break;
//				case 7:
//					icon = CustomItem.AWAND.toItemStack(1);
//					name = CustomItem.AWAND.toString();
//					cost = 16;
//					break;
//				case 8:
//					icon = CustomItem.MWAND.toItemStack(1);
//					name = CustomItem.MWAND.toString();
//					cost = 32;
//					break;
				//LINE 1
			//TODO----COSTS ARE MOST LIKELY INCORRECT		
				//LINE 2
				case 9:
					icon = new ItemStack(Material.FISHING_ROD,1);
					cost = 5;
					break;
				case 10:
					icon = new ItemStack(Material.CAKE,1);
					cost = 5;
					break;
				case 11:
					icon = new ItemStack(Material.RAW_BEEF,3);
					cost = 5;
					break;
				case 12:
					icon = new ItemStack(Material.BOOK,1);
					cost = 5;
					break;
				//LINE 2			
			}
			if(cat)
				menu.setItem(x, new ShopMenuItem(icon, icon, cost));
		}
	}
	
	private static void buildBrewingShop(ItemMenu menu)
	{
		for(int x = 0; x < 27; x++)
		{
			int cost = 0;
			ItemStack icon = null;
			boolean cat = true;
			switch(x)
			{
				//TODO----------------MAKE SURE ALL THE COSTS ARE CORRECT
				//LINE 1
				default:
					cat = false;
					break;

				case 0:
					icon = new ItemStack(Material.BREWING_STAND_ITEM,1);
					cost = 10;
					break;
				case 1:
					icon = new ItemStack(Material.GLASS_BOTTLE,3);
					cost = 1;
					break;
				case 2:
					icon = new ItemStack(Material.NETHER_STALK,1);
					cost = 5;
					break;
				//LINE 1
					
				
				
			//TODO----COSTS ARE MOST LIKELY INCORRECT		
				//LINE 2
				case 9:
					icon = new ItemStack(Material.REDSTONE,1);
					cost = 3;
					break;
				case 10:
					icon = new ItemStack(Material.GLOWSTONE_DUST,1);
					cost = 3;
					break;
				case 11:
					icon = new ItemStack(Material.FERMENTED_SPIDER_EYE,1);
					cost = 3;
					break;
				case 12:
					icon = new ItemStack(Material.SULPHUR,1);
					cost = 3;
					break;
				//LINE 2
					
					
					
				//LINE 3
				case 18:
					icon = new ItemStack(Material.MAGMA_CREAM,1);
					cost = 2;
					break;
				case 19:
					icon = new ItemStack(Material.SUGAR,1);
					cost = 2;
					break;
				case 20:
					icon = new ItemStack(Material.SPECKLED_MELON,1);
					cost = 2;
					break;
				case 21:
					icon = new ItemStack(Material.GHAST_TEAR,1);
					cost = 15;
					break;
				case 22:
					icon = new ItemStack(Material.GOLDEN_CARROT,1);
					cost = 2;
					break;
				case 23:
					icon = new ItemStack(Material.SPIDER_EYE,1);
					cost = 2;
					break;
//					case 24:
//						icon = new ItemStack(Material.BLAZE_POWDER,1);
//						cost = 15;
//						break;
				//LINE 3
				
			}
			if(cat)
				menu.setItem(x, new ShopMenuItem(icon, icon, cost){});
		}
	}
	
	public static void addGunPowder()
	{
		ItemStack s = new ItemStack(Material.BLAZE_POWDER);
		brewing.setItem(24, new ShopMenuItem(s,s,15));
	}
	
	public static void openWeaponShop(Player p)
	{
		weapon.open(p);
	}
	
	public static void openBrewingShop(Player p)
	{
		brewing.open(p);
	}
}
//    private String name;
//    private int size;
//    private ItemPurchaseEventHandler handler;
//    //private Plugin plugin;
//    private boolean destroy;
//   
//    private String[] optionNames;
//    private ItemStack[] optionIcons;
//    private ItemStack[] backingItems;
//   
//    public ShopMenu(String name, int size, ItemPurchaseEventHandler handler, boolean destroy) 
//    {
//        this.name = name;
//        this.size = size;
//        this.handler = handler;
//        this.destroy = destroy;
//        this.optionNames = new String[size];
//        this.optionIcons = new ItemStack[size];
//        this.backingItems = new ItemStack[size];
//        Bukkit.getPluginManager().registerEvents(this, AnnihilationMain.getInstance());
//    }
//    
//    public ShopMenu setNameLessOption(int position, ItemStack icon, ItemStack backing, String... info) 
//    {
//        optionNames[position] = "NA";
//        optionIcons[position] = setItemNameAndLore(icon, null, info);
//        backingItems[position] = backing;
//        return this;
//    }
//   
//    public ShopMenu setOption(int position, ItemStack icon, ItemStack backing, String name, String... info) 
//    {
//        optionNames[position] = name;
//        optionIcons[position] = setItemNameAndLore(icon, name, info);
//        backingItems[position] = backing;
//        return this;
//    }
//   
//    public void open(Player player) 
//    {
//        Inventory inventory = Bukkit.createInventory(player, size, name);
//        for (int i = 0; i < optionIcons.length; i++) 
//        {
//            if (optionIcons[i] != null) 
//            {
//                inventory.setItem(i, optionIcons[i]);
//            }
//        }
//        player.openInventory(inventory);
//    }
//   
//    public void destroy() 
//    {
//        HandlerList.unregisterAll(this);
//        handler = null;
//        optionNames = null;
//        optionIcons = null;
//    }
//   
//    @EventHandler(priority=EventPriority.MONITOR)
//    void onInventoryClick(InventoryClickEvent event) 
//    {
//        if (event.getInventory().getTitle().equals(name)) 
//        {
//            event.setCancelled(true);
//            int slot = event.getRawSlot();
//            if (slot >= 0 && slot < size && optionNames[slot] != null) 
//            {
//                ItemPurchaseEvent e = new ItemPurchaseEvent((Player)event.getWhoClicked(), slot, optionNames[slot], this.optionIcons[slot],this.backingItems[slot]);
//                handler.onItemPurchase(e);
//                if (e.willClose()) 
//                {
//                    final Player p = (Player)event.getWhoClicked();
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable() 
//                    {
//                        public void run() 
//                        {
//                            p.closeInventory();
//                        }
//                    }, 1);
//                }
//                if(e.destroy)
//                {
//                	destroy();
//                }
//            }
//        }
//    }
//    
//    @EventHandler(priority=EventPriority.MONITOR)
//    void onInventoryClose(InventoryCloseEvent event) 
//    {
//    	 if (event.getInventory().getTitle().equals(name)) 
//         {
//    		 if(this.destroy)
//    			 destroy();
//         }
//    }
//    
//    public interface ItemPurchaseEventHandler 
//    {
//        public void onItemPurchase(ItemPurchaseEvent event);       
//    }
//    
//    public class ItemPurchaseEvent 
//    {
//        private Player player;
//        private int position;
//        private String name;
//        private boolean close;
//        private boolean destroy;
//        private ItemStack icon;
//        private ItemStack backing;
//       
//        public ItemPurchaseEvent(Player player, int position, String name, ItemStack icon, ItemStack backing) 
//        {
//            this.player = player;
//            this.position = position;
//            this.name = name;
//            this.close = true;
//            this.destroy = false;
//            this.icon = icon;
//            this.backing = backing;
//        }
//        
//        public ItemStack getBacking()
//        {
//        	return this.backing;
//        }
//
//		public Player getPlayer()
//		{
//            return player;
//        }
//        
//        public ItemStack getIcon()
//        {
//        	return this.icon;
//        }
//       
//        public int getPosition() 
//        {
//            return position;
//        }
//       
//        public String getName() 
//        {
//            return name;
//        }
//       
//        public boolean willClose() 
//        {
//            return close;
//        }
//       
//        public boolean willDestroy() 
//        {
//            return destroy;
//        }
//       
//        public void setWillClose(boolean close) 
//        {
//            this.close = close;
//        }
//       
//        public void setWillDestroy(boolean destroy) 
//        {
//            this.destroy = destroy;
//        }
//    }
//   
//    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) 
//    {
//        ItemMeta im = item.getItemMeta();
//        	if(name != null)
//        		im.setDisplayName(name);
//            im.setLore(Arrays.asList(lore));
//        item.setItemMeta(im);
//        return item;
//    }
//   
//}


