package com.gmail.nuclearcat1337.anniPro.announcementBar.versions.v1_7_R3;

import net.minecraft.server.v1_7_R3.DataWatcher;
import net.minecraft.server.v1_7_R3.EntityEnderDragon;
import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Dragon extends IDragon
{
    private EntityEnderDragon dragon;
    private int id;

    public Dragon(String name, Location loc)
    {
        super(name, loc);
    }

    @Override
    public Packet getSpawnPacket()
    {
        //Class<?> Entity = Util.getCraftClass("Entity");
        //Class<?> EntityLiving = Util.getCraftClass("EntityLiving");
        //Class<?> EntityEnderDragon = Util.getCraftClass("EntityEnderDragon");
        Packet packet = null;
        //dragon = EntityEnderDragon.getConstructor(Util.getCraftClass("World")).newInstance(getWorld());
        dragon = new EntityEnderDragon(getWorld());

        //Method setLocation = Util.getMethod(EntityEnderDragon, "setLocation", new Class<?>[] { double.class, double.class, double.class, float.class, float.class });
        //setLocation.invoke(dragon, getX(), getY(), getZ(), getPitch(), getYaw());
        dragon.setLocation(getX(),getY(),getZ(),getPitch(),getYaw());

        //Method setInvisible = Util.getMethod(EntityEnderDragon, "setInvisible", new Class<?>[] { boolean.class });
        //setInvisible.invoke(dragon, isVisible());
        dragon.setInvisible(isVisible());

        //Method setCustomName = Util.getMethod(EntityEnderDragon, "setCustomName", new Class<?>[] { String.class });
        //setCustomName.invoke(dragon, name);
        dragon.setCustomName(name);

        //Method setHealth = Util.getMethod(EntityEnderDragon, "setHealth", new Class<?>[] { float.class });
        //setHealth.invoke(dragon, health);
        dragon.setHealth(health);

        //Field motX = Util.getField(Entity, "motX");
        //motX.set(dragon, getXvel());
        dragon.motX  = getXvel();

        //Field motY = Util.getField(Entity, "motY");
        //motY.set(dragon, getYvel());
        dragon.motY = getYvel();

        //Field motZ = Util.getField(Entity, "motZ");
        //motZ.set(dragon, getZvel());
        dragon.motZ = getZvel();

        //Method getId = Util.getMethod(EntityEnderDragon, "getId", new Class<?>[] {});
        //this.id = (Integer) getId.invoke(dragon);
        this.id = dragon.getId();

        //Class<?> PacketPlayOutSpawnEntityLiving = Util.getCraftClass("PacketPlayOutSpawnEntityLiving");

        //packet = PacketPlayOutSpawnEntityLiving.getConstructor(new Class<?>[] { EntityLiving }).newInstance(dragon);

        packet = new PacketPlayOutSpawnEntityLiving(dragon);
        return packet;
    }

    @Override
    public Packet getDestroyPacket()
    {
        //Class<?> PacketPlayOutEntityDestroy = Util.getCraftClass("PacketPlayOutEntityDestroy");

        //Object packet = null;
       // try
        //{
//            packet = PacketPlayOutEntityDestroy.newInstance();
//            Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
//            a.setAccessible(true);
//            a.set(packet, new int[] { id });

        //    packet = new PacketPlayOutEntityDestroy(id);
        //} catch (SecurityException e) {
            //e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            //e.printStackTrace();
//        } catch (InstantiationException e) {
//            //e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            //e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            //e.printStackTrace();
//        }

        return new PacketPlayOutEntityDestroy(id);
    }

    @Override
    public Packet getMetaPacket(DataWatcher watcher)
    {
//        Class<?> DataWatcher = Util.getCraftClass("DataWatcher");
//
//        Class<?> PacketPlayOutEntityMetadata = Util.getCraftClass("PacketPlayOutEntityMetadata");

//        Object packet = null;
//        try {
//            packet = PacketPlayOutEntityMetadata.getConstructor(new Class<?>[] { int.class, DataWatcher, boolean.class }).newInstance(id, watcher, true);
//        } catch (IllegalArgumentException e) {
//            //e.printStackTrace();
//        } catch (SecurityException e) {
//            //e.printStackTrace();
//        } catch (InstantiationException e) {
//            //e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            //e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            //e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            //e.printStackTrace();
//        }

        return new PacketPlayOutEntityMetadata(id,watcher,true);
    }

    @Override
    public Packet getTeleportPacket(Location loc)
    {
//        Class<?> PacketPlayOutEntityTeleport = Util.getCraftClass("PacketPlayOutEntityTeleport");
//
//        Object packet = null;
//
//        try {
//            packet = PacketPlayOutEntityTeleport.getConstructor(new Class<?>[] { int.class, int.class, int.class, int.class, byte.class, byte.class }).newInstance(this.id, loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360));
//        } catch (IllegalArgumentException e) {
//            //e.printStackTrace();
//        } catch (SecurityException e) {
//            //e.printStackTrace();
//        } catch (InstantiationException e) {
//            //e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            //e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            //e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            //e.printStackTrace();
//        }

        return new PacketPlayOutEntityTeleport(this.id, loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360));
    }

    @Override
    public DataWatcher getWatcher()
    {
        //Class<?> Entity = Util.getCraftClass("Entity");
        //Class<?> DataWatcher = Util.getCraftClass("DataWatcher");

        DataWatcher watcher;// = null;
        //try {
        //watcher = DataWatcher.getConstructor(new Class<?>[] { Entity }).newInstance(dragon);
        //Method a = Util.getMethod(DataWatcher, "a", new Class<?>[] { int.class, Object.class });
        watcher = new DataWatcher(dragon);
        watcher.a();

        watcher.a(0, isVisible() ? (byte) 0 : (byte) 0x20);
        watcher.a(6, (Float) health);
        watcher.a(7, (Integer) 0);
        watcher.a( 8, (Byte) (byte) 0);
        watcher.a( 10, name);
        watcher.a(11, (Byte) (byte) 1);
//        } catch (IllegalArgumentException e) {
//
//            //e.printStackTrace();
//        } catch (SecurityException e) {
//
//            //e.printStackTrace();
//        } catch (InstantiationException e) {
//
//            //e.printStackTrace();
//        } catch (IllegalAccessException e) {
//
//            //e.printStackTrace();
//        } catch (InvocationTargetException e) {
//
//            //e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//
//            //e.printStackTrace();
//        }
        return watcher;
    }
}
