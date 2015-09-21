package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class RegeneratingBlock
{
	public RegeneratingBlock(Material Type, int MaterialData, boolean Regenerate, boolean CobbleReplace, boolean NaturalBreak, int Time,
			TimeUnit Unit, int XP, Material Product, String Amount, int ProductData, String Effect)
	{
		this.Type = Type;
		this.Regenerate = Regenerate;
		this.CobbleReplace = CobbleReplace;
		this.NaturalBreak = NaturalBreak;
		this.Time = Time;
		this.Unit = Unit;
		this.XP = XP;
		this.Product = Product;
		this.Amount = Amount;
		this.MaterialData = MaterialData;
		this.Effect = Effect;
		this.ProductData = ProductData;
	}
	
	public final Material Type;
	public final int MaterialData;
	public final boolean Regenerate;
	public final boolean CobbleReplace;
	public final boolean NaturalBreak;
	public final int Time;
	public final TimeUnit Unit;
	public final int XP;
	public final Material Product;
	public final String Amount;
	public final int ProductData;
	public final String Effect;
	
	public void saveToConfig(ConfigurationSection configSection)
	{
		if(configSection != null)
		{
			configSection.set("Type", Type.name());
			configSection.set("MaterialData", MaterialData);
			configSection.set("Regenerate", Regenerate);
			configSection.set("CobbleReplace", CobbleReplace);
			configSection.set("NaturalBreak", NaturalBreak);
			configSection.set("Time", Time);
			configSection.set("Unit", Unit != null ? Unit.name() : "");
			configSection.set("XP", XP);
			configSection.set("Product", Product != null ? Product.name() : "");
			configSection.set("Amount", Amount);
			configSection.set("ProductData", ProductData);
			configSection.set("Effect", Effect);
		}
	}
}
