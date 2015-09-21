package net.techcable.npclib.api;

import com.gmail.nuclearcat1337.anniPro.utils.VersionUtils;
import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//import net.techcable.npclib.NPC;
//import net.techcable.npclib.nms.NMS;

public class Util
{
    private Util() {}
    
    
    private static NMS nms;
    public static NMS getNMS() {
    	if (nms == null) {
    		try {
        		String version = VersionUtils.getVersion();
                String name = "net.techcable.npclib.versions." + version + ".NMS";
                //Bukkit.getLogger().info(name);
        		Class<?> rawClass = Class.forName(name);
        		Class<? extends NMS> nmsClass = rawClass.asSubclass(NMS.class);
        		Constructor<? extends NMS> constructor = nmsClass.getConstructor();
        		return constructor.newInstance();
        	} catch (ClassNotFoundException ex) {
        		throw new UnsupportedOperationException("Unsupported nms version", ex);
        	} catch (InvocationTargetException ex) {
        		throw Throwables.propagate(ex.getTargetException());
        	} catch (Exception ex) {
        		throw Throwables.propagate(ex);
        	}
    	}
    	return nms;
    }
    
//    public static String getVersion() {
//    	String packageName = Bukkit.getServer().getClass().getPackage().getName();
//    	return packageName.substring(packageName.lastIndexOf(".") + 1);
//    }
    
//    public static void look(Entity entity, Location toLook) {
//        if (!entity.getWorld().equals(toLook.getWorld()))
//            return;
//        Location fromLocation = entity.getLocation();
//        double xDiff, yDiff, zDiff;
//        xDiff = toLook.getX() - fromLocation.getX();
//        yDiff = toLook.getY() - fromLocation.getY();
//        zDiff = toLook.getZ() - fromLocation.getZ();
//
//        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
//        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);
//
//        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
//        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90;
//        if (zDiff < 0.0)
//            yaw += Math.abs(180 - yaw) * 2;
//
//        getNMS().look(entity, (float) yaw - 90, (float) pitch);
//    }
   
//    public static Entity spawn(Player player) {
//        //if (type.equals(EntityType.PLAYER)) {
//            return getNMS().sp
//        //} else throw new UnsupportedOperationException();
//    }
    
    public static Player[] getNearbyPlayers(int range, Location l) {
    	List<Player> nearby = new ArrayList<>(12);
    	for (Player p : Bukkit.getOnlinePlayers()) {
    		double distance = p.getLocation().distanceSquared(l);
    		if (distance <= range) {
    			nearby.add(p);
    		}
    	}
    	return nearby.toArray(new Player[nearby.size()]);
    }
}