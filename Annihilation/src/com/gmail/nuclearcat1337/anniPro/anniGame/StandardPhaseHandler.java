package com.gmail.nuclearcat1337.anniPro.anniGame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.gmail.nuclearcat1337.anniPro.announcementBar.AnnounceBar;
import com.gmail.nuclearcat1337.anniPro.announcementBar.Announcement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gmail.nuclearcat1337.anniPro.anniMap.GameMap;
import com.gmail.nuclearcat1337.anniPro.announcementBar.MessageBar;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.main.Lang;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import com.gmail.nuclearcat1337.anniPro.utils.ShopMenu;
import com.gmail.nuclearcat1337.anniPro.voting.ScoreboardAPI;

public class StandardPhaseHandler implements Runnable
{
	private Map<Integer,ImageMessage> images;
	public StandardPhaseHandler()
	{
		images = new HashMap<Integer,ImageMessage>();
		for(int x = 1; x < 6; x++)
		{
			try
			{
				BufferedImage image = ImageIO.read(AnnihilationMain.getInstance().getResource("Images/Phase"+x+".png"));
				ImageMessage message =  new ImageMessage(image, 10, ImageChar.MEDIUM_SHADE.getChar());
				message.appendTextToLine(5,Lang.PHASESTART.toStringReplacement(x));
				if(x == 1)
					message.appendTextToLines(6,Lang.PHASE1MESSAGE.toStringArray());
				else if(x==2)
					message.appendTextToLines(6,Lang.PHASE2MESSAGE.toStringArray());
				else if(x==3)
					message.appendTextToLines(6,Lang.PHASE3MESSAGE.toStringArray());
				else if(x==4)
					message.appendTextToLines(6,Lang.PHASE4MESSAGE.toStringArray());
				else if(x==5)
					message.appendTextToLines(6,Lang.PHASE5MESSAGE.toStringArray());
				images.put(x, message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		sendImage(1);
	}
	
	private void sendImage(int x)
	{
		ImageMessage m = images.get(x);
//		for(AnniPlayer p : AnniPlayer.getPlayers().values())
//		{
//			Player player = p.getPlayer();
//			if(player != null)
//				m.sendToPlayer(player);
//		}
		for(Player player : Bukkit.getOnlinePlayers())
		{
			m.sendToPlayer(player);
		}
	}

	@Override
	public void run()
	{
		if(Game.getGameMap() != null)
		{
			GameMap map = Game.getGameMap();
			int newPhase = map.getCurrentPhase()+1;
            String m = Lang.PHASEBAR.toStringReplacement(newPhase) + " - {#}";
            Announcement ann = new Announcement(Lang.PHASEBAR.toStringReplacement(newPhase) + " - {#}");
			switch(newPhase)
			{
				case 2:
					map.setCanDamageNexus(true);
                    ann.setTime(map.getPhaseTime()).setCallback(this);
					break;
				case 3:
					for(Loc loc : map.getDiamondLocations())
					{
						Location l = loc.toLocation();
						l.getWorld().getBlockAt(l).setType(Material.DIAMOND_ORE);
					}
                    ann.setTime(map.getPhaseTime()).setCallback(this);
					break;
				case 4:
					ShopMenu.addGunPowder();
                    ann.setTime(map.getPhaseTime()).setCallback(this);
					break;
				case 5:
					map.setDamageMultiplier(2);
					//MessageBar.permanentBar(Lang.PHASEBAR.toStringReplacement(newPhase));
                    ann.setPermanent(true).setMessage(Lang.PHASEBAR.toStringReplacement(newPhase));
					break;
			}
            AnnounceBar.getInstance().countDown(ann);
			map.setPhase(map.getCurrentPhase()+1);
			ScoreboardAPI.updatePhase();
			Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE+"Phase "+ChatColor.AQUA+map.getCurrentPhase()+ChatColor.DARK_PURPLE+" has begun!");
			sendImage(map.getCurrentPhase());
			//Game.broadcastMessage(message);
		}
	}
}
