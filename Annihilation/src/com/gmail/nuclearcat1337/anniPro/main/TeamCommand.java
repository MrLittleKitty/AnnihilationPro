package com.gmail.nuclearcat1337.anniPro.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ActionMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickHandler;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;

public class TeamCommand implements CommandExecutor, Listener
{
	//private AnnihilationMain plugin;
	private ItemMenu menu;
	private Map<ChatColor,ImageMessage> messages;
	public TeamCommand(JavaPlugin plugin)
	{
		menu = new ItemMenu("Join a Team",Size.ONE_LINE);
		messages = new EnumMap<ChatColor,ImageMessage>(ChatColor.class);
		plugin.getCommand("Team").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
//		for(int x = 3; x < 6; x++)
//		{
//			Permission perm = new Permission("Anni.JoinPhase."+x);
//			perm.setDefault(PermissionDefault.OP);
//			Bukkit.getPluginManager().addPermission(perm);
//			perm.recalculatePermissibles();
//		}
		
		int x = 0;
		for(final AnniTeam team : AnniTeam.Teams)
		{
			BufferedImage image;
			try
			{
				image = ImageIO.read(AnnihilationMain.getInstance().getResource("Images/"+team.getName()+"Team.png"));
				String[] lore = new String[]
				{
					"",
					"",
					"",
					"",
					Lang.JOINTEAM.toString(),
					team.getExternalColoredName()+" "+Lang.TEAM,
				};
				ImageMessage message =  new ImageMessage(image, 10, ImageChar.MEDIUM_SHADE.getChar()).appendText(lore);
				messages.put(team.getColor(), message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			byte datavalue;
			if(team.equals(AnniTeam.Red))
				datavalue = (byte)14;
			else if(team.equals(AnniTeam.Blue))
				datavalue = (byte)11;
			else if(team.equals(AnniTeam.Green))
				datavalue = (byte)13;
			else
				datavalue = (byte)4;
			
			ActionMenuItem item = new ActionMenuItem(team.getExternalColoredName(),new ItemClickHandler(){
				@Override
				public void onItemClick(ItemClickEvent event)
				{
					event.getPlayer().performCommand("Team "+team.getName());
					event.setWillClose(true);
				}},new ItemStack(Material.WOOL,0,datavalue),new String[]{});
			menu.setItem(x, item);
			x++;
		}
		menu.setItem(4, new ActionMenuItem(ChatColor.AQUA+"Leave a Team",new ItemClickHandler(){
			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().performCommand("Team Leave");
				event.setWillClose(true);
			}},new ItemStack(Material.WOOL),new String[]{}));
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void voteGUIcheck(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			final Player player = event.getPlayer();
			if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.TEAMMAP.getName()))
			{
				if(menu != null)
					menu.open(player);
				event.setCancelled(true);
			}
		}
	}
	
	//private ChatColor r = ChatColor.RED;
	//private ChatColor g = ChatColor.GREEN;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			final AnniPlayer p = AnniPlayer.getPlayer(((Player) sender).getUniqueId());
			if(args.length == 0) //checking the amount of players on a team
			{
				String[] messages = new String[8];
				for(int x = 0; x < 4; x++)
				{
					AnniTeam t = AnniTeam.Teams[x];
					int cat = x*2;
					messages[cat] = t.getColor()+"/Team "+t.getExternalName()+":";
					int y = t.getPlayerCount();
					//messages[cat+1] = t.Color+"There are "+y+" Players on the "+t.toString()+" Team.";
					messages[cat+1] = t.getColor()+Lang.TEAMCHECK.toStringReplacement(y, t.getExternalName());
				}
				sender.sendMessage(messages);
			}
			else if (args.length == 1) //Joining or leaving a team
			{
				if(args[0].equalsIgnoreCase("leave"))
				{
					if(!Game.isGameRunning())
					{
						if(p.getTeam() != null)
						{
							//sender.sendMessage(ChatColor.DARK_PURPLE+"You left "+p.getTeam().Color+p.getTeam().getName()+" Team");
							sender.sendMessage(Lang.LEAVETEAM.toStringReplacement(p.getTeam().getExternalColoredName()));
							//ScoreboardAPI.removePlayer(p.getTeam(), p.getPlayer());
							p.getTeam().leaveTeam(p);
						}
						else sender.sendMessage(Lang.NOTEAM.toString());
					}
					else sender.sendMessage(Lang.CANNOTLEAVE.toString());
				}
				else
				{
					AnniTeam t = AnniTeam.getTeamByName(args[0]);
					if(t != null)
					{
						//THIS IS WHERE WE SHOULD JOIN THEM IN A TEAM
						//p.setTeam(t);
						this.joinCheck(t, p);
						//sender.sendMessage(g+"You joined "+t.Color+t.getName()+" team");
					}
					else
						sender.sendMessage(Lang.INVALIDTEAM.toString());
				}
			}
			else sender.sendMessage(Lang.TEAMHELP.toString());
			
		}
		else sender.sendMessage("This command can only be used by a player!");
		return true;
	}
	
	private void joinCheck(AnniTeam team, AnniPlayer p)
	{
		if(p != null && team != null)
		{
			Object obj = p.getData("TeamDelay");
			if(obj == null || System.currentTimeMillis() >= (long)obj)
			{
				p.setData("TeamDelay", System.currentTimeMillis()+1000);
				Player player = p.getPlayer();
				if(p.getTeam() == null)
				{
					if(team.isTeamDead())
					{
						player.sendMessage(Lang.DESTROYEDTEAM.toString());
						return;
					}
					
					if(Game.getGameMap() != null)
					{
						int phase = Game.getGameMap().getCurrentPhase();
						if(phase > 2)
						{
							boolean allowed = false;
							for(int x = phase; x < 6; x++)
							{
								if(player.hasPermission("Anni.JoinPhase."+x))
								{
									allowed = true;
									break;
								}
							}
							
							if(!allowed)
							{
								player.sendMessage(Lang.WRONGPHASE.toString());
								return;
							}
						}
					}
					
					if(player.hasPermission("Anni.BypassJoin"))
					{
						joinTeam(p,team);
						return;
					}		
						
					if(GameVars.useTeamBalance())
					{
						//This should balance teams out with a 3 person grace period
						int currentTeamsPlayers = team.getPlayerCount();
						int smallest = Integer.MAX_VALUE;
						for(int x = 0; x < 4; x++)
						{
							AnniTeam t = AnniTeam.Teams[x];
							if(!t.isTeamDead() && t.getPlayerCount() < smallest)
								smallest = t.getPlayerCount();
								//if(!t.equals(team) && !t.isTeamDead())
								///	largest = t.getPlayerCount();

						}

						if(currentTeamsPlayers - smallest > GameVars.getBalanceTolerance())
						{
							player.sendMessage(Lang.JOINANOTHERTEAM.toString());
							return;
						}
						
//						if((currentTeamsPlayers+1) - largest > GameVars.getBalanceTolerance())
//						{
//							player.sendMessage(Lang.JOINANOTHERTEAM.toString());
//							return;
//						}
					}
					
					joinTeam(p,team);
				}
				else player.sendMessage(Lang.ALREADYHAVETEAM.toString());
			}
		}
	}
	
	private void joinTeam(AnniPlayer player, AnniTeam team)
	{
		team.joinTeam(player);
		Player p = player.getPlayer();
		ImageMessage m = messages.get(team.getColor());
		m.sendToPlayer(p);
		//player.sendMessage(ChatColor.DARK_PURPLE+"You have joined "+team.Color+team.toString()+ChatColor.DARK_PURPLE+" Team");
		if(Game.isGameRunning())
			p.setHealth(0);

	}

}
