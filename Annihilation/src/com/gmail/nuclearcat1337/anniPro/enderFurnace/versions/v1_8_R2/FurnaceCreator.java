package com.gmail.nuclearcat1337.anniPro.enderFurnace.versions.v1_8_R2;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.EnderFurnace;
import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.IFurnace;

public class FurnaceCreator implements com.gmail.nuclearcat1337.anniPro.enderFurnace.api.FurnaceCreator
{
    @Override
    public IFurnace createFurnace(final AnniPlayer player)
    {
        IFurnace f = new Furnace_v1_8_R2(player.getPlayer());
        if(EnderFurnace.getFurnaceData(player) != null)
            f.load(EnderFurnace.getFurnaceData(player));
        return f;
    }
}
