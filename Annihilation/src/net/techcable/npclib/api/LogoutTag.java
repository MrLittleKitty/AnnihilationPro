package net.techcable.npclib.api;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class LogoutTag extends BukkitRunnable
{
    private ItemStack[] inventory;
    private ItemStack[] armor;

    private Boolean wasKilled;

    private NPC npc;
    private UUID player;

    private String teamName;

    public LogoutTag(Player player)
    {
        this.player = player.getUniqueId();

        AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
        assert p != null;
        teamName = p.getTeam().getName();

        inventory = player.getInventory().getContents();
        armor = player.getInventory().getArmorContents();
        npc = new NPC(player,this);
        wasKilled = null;
        this.runTaskLater(AnnihilationMain.getInstance(), GameVars.getNpcTimeout()*20); //NPC timeout comes from the config
    }

    public Boolean wasKilled()
    {
        return wasKilled;
    }

//    public void equiptPlayer(Player player)
//    {
//        if(inventory != null)
//            player.getInventory().setContents(inventory);
//        if(armor != null)
//            player.getInventory().setArmorContents(armor);
//    }

    public boolean onKill(Player killer) //could be null if not killed by another entity
    {
        if(killer != null)
        {
            AnniPlayer pl = AnniPlayer.getPlayer(killer.getUniqueId());
            assert pl != null;
            if (pl.getTeam().getName().equals(teamName))
                return false;
        }

        wasKilled = true;
        Location loc = npc.getLocation();
        npc.despawn();
        cancel();
        World world = loc.getWorld();
        for(ItemStack s : inventory)
            if(s != null && s.getType() != Material.AIR)
                if(!KitUtils.isSoulbound(s))
                    world.dropItemNaturally(loc,s);
        for(ItemStack s : armor)
            if(s != null && s.getType() != Material.AIR)
                if(!KitUtils.isSoulbound(s))
                    world.dropItemNaturally(loc,s);
        inventory = null;
        armor = null;

        //This could be a very bad idea.
        //At this time I currently dont remember exactly what relies on this event
        //For all I know something that needs to actual Player object could use this event
        if(killer != null)
        {
            AnniPlayer k = AnniPlayer.getPlayer(killer.getUniqueId());
            AnniPlayer p = AnniPlayer.getPlayer(player);
            if(p != null && k != null)
                AnniEvent.callEvent(new PlayerKilledEvent(k,p));
        }

        return true;
    }

    public void close()
    {
        inventory = null;
        armor = null;
        npc = null;
        wasKilled = null;
        player = null;
    }

    @Override
    public void run()
    {
        wasKilled = false;
        npc.despawn();
    }
}
