package net.techcable.npclib.api;

import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NPC
{
    public NPC(Player player, LogoutTag tag)
    {
        //this.uuid = player.getUniqueId();
        this.tag = tag;
        spawn(player,player.getLocation());
    }

    private LogoutTag tag;
    private boolean spawned;
    private Entity entity;
    private Loc loc;
    //private UUID uuid;

    public boolean despawn()
    {
        if(!spawned) return false;
        this.spawned = false;
        //Util.getNMS().onDespawn(this);

        Util.getNMS().onDespawn(this);

        entity.remove();
        entity = null;
        //update();
        //cancel();
       // getRegistry().deregister(this);

        tag = null;
        loc = null;

        return true;
    }

    public Location getLocation()
    {
        return loc.toLocation();
    }

    private boolean spawn(Player player, Location toSpawn)
    {
        if (spawned) return false;
        Entity spawned = Util.getNMS().spawnPlayer(player,toSpawn,this);
        loc = new Loc(toSpawn,true);
        if (spawned != null)
        {
            this.spawned = true;
            this.entity = spawned;
            //update();
            //tryEquipmentChangeNotify(Bukkit.getOnlinePlayers());
            return true;
        } else return false;
    }

//    public void update()
//    {
//        update(Util.getNearbyPlayers(128, getEntity().getLocation()));
//    }

//    public void update(Player[] players)
//    {
//        if (spawned) Util.getNMS().notifyOfSpawn(players, new Player[] { (Player)getEntity() });
//        else
//            Util.getNMS().notifyOfDespawn(players, new Player[] { (Player)getEntity() });
//    }

    public boolean onKill(Player killer)
    {
        return tag.onKill(killer);
    }

    public Entity getEntity()
    {
        return entity;
    }
}
