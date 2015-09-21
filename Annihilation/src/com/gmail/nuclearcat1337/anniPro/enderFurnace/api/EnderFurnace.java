package com.gmail.nuclearcat1337.anniPro.enderFurnace.api;

import com.gmail.nuclearcat1337.anniPro.enderFurnace.*;
import com.gmail.nuclearcat1337.anniPro.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class EnderFurnace
{
    private static FurnaceCreator creator;

    private EnderFurnace()
    {}


    public static FurnaceCreator getCreator()
    {
        if(creator == null)
        {
            String version = VersionUtils.getVersion();
            String name = "com.gmail.nuclearcat1337.anniPro.enderFurnace.versions."+version+".FurnaceCreator";
            //Bukkit.getLogger().info(name);
            Class<?> rawClass = null;
            try
            {
                rawClass = Class.forName(name);
                Class<? extends FurnaceCreator> furnaceClass = rawClass.asSubclass(FurnaceCreator.class);
                Constructor<? extends FurnaceCreator> constructor = furnaceClass.getConstructor();
                creator = constructor.newInstance();
            }
            catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
            {
                //Bukkit.getLogger().info("This happened :(");
                creator = new TempCreator();
            }
        }
        return creator;
    }

    public static FurnaceData getFurnaceData(AnniPlayer player)
    {
        Object obj = player.getData("ED");
        if(obj == null)
            return null;
        return (FurnaceData)obj;
    }

    private static class TempCreator implements FurnaceCreator
    {
        @Override
        public IFurnace createFurnace(final AnniPlayer player)
        {
            return new TempFurnace(player);
        }

        private class TempFurnace implements IFurnace
        {
            private final String version;
            private final AnniPlayer p;
            public TempFurnace(AnniPlayer p)
            {
                this.p = p;
                version = VersionUtils.getVersion();
            }

            @Override
            public void tick() {}

            @Override
            public void open() { p.sendMessage("Sorry, this server is using an unsupported version for Ender Furnaces. Version: "+version); }

            @Override
            public FurnaceData getFurnaceData()
            {
                return null;
            }

            @Override
            public void load(final FurnaceData data) {}
        }
    }
}
