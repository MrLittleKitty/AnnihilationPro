package net.techcable.npclib.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NMS
{
    public Player spawnPlayer(Player player, Location location, NPC npc);
    public void onDespawn(NPC npc);
//    public void notifyOfSpawn(Player[] paramArrayOfPlayer1, Player[] paramArrayOfPlayer2);
//    public void notifyOfDespawn(Player[] paramArrayOfPlayer1, Player[] paramArrayOfPlayer2);
}
