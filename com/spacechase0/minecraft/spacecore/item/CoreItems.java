package com.spacechase0.minecraft.spacecore.item;

import net.minecraft.item.ItemStack;
import static net.minecraft.init.Items.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.util.ModObject;

import cpw.mods.fml.common.registry.GameRegistry;

public class CoreItems extends Items
{
	@Override
	public void register( BaseMod mod, Configuration config )
	{
		super.register( mod, config );
		
		if ( nugget != null )
		{
			OreDictionary.registerOre( "nuggetIron", new ItemStack( nugget, 1, 0 ) );
			OreDictionary.registerOre( "nuggetDiamond", new ItemStack( nugget, 1, 1 ) );
			OreDictionary.registerOre( "nuggetEmerald", new ItemStack( nugget, 1, 2 ) );

			GameRegistry.addShapelessRecipe( new ItemStack( nugget, 9, 0 ), iron_ingot );
			GameRegistry.addShapelessRecipe( new ItemStack( nugget, 9, 1 ), diamond );
			GameRegistry.addShapelessRecipe( new ItemStack( nugget, 9, 2 ), emerald );
			
			GameRegistry.addRecipe( new ShapedOreRecipe( iron_ingot,
			                                             "***",
			                                             "***",
			                                             "***",
			                                             '*', "nuggetIron" ) );
			GameRegistry.addRecipe( new ShapedOreRecipe( diamond,
                                                         "***",
			                                             "***",
			                                             "***",
			                                             '*', "nuggetDiamond" ) );
			GameRegistry.addRecipe( new ShapedOreRecipe( emerald,
			                                             "***",
			                                             "***",
			                                             "***",
			                                             '*', "nuggetEmerald" ) );
		}
	}
	
	@ModObject( optional = true )
	public MultiItem nugget;
	public Object[] nuggetParams = new Object[] { "spacecore", "nugget", new String[] { "iron", "diamond", "emerald" } };
}
