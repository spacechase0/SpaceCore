package com.spacechase0.minecraft.spacecore.craftableenchantmentbooks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftableEnchantedBooks
{ 
	public static void addRecipes()
	{
		GameRegistry.addRecipe( new EnchantedBookRecipes() );
		
		EnchantmentData prot = new EnchantmentData( Enchantment.protection, 1 );
		EnchantmentData eff = new EnchantmentData( Enchantment.efficiency, 1 );
		EnchantmentData sharp = new EnchantmentData( Enchantment.sharpness, 1 );
		
		doEnchantment( Enchantment.protection, 3, Items.iron_ingot, 4, Items.leather, 4, null );
		doEnchantment( Enchantment.fireProtection, 3, Items.magma_cream, 2, Items.blaze_rod, 2, prot );
		doEnchantment( Enchantment.featherFalling, 3, Items.feather, 8, prot );
		doEnchantment( Enchantment.blastProtection, 3, Items.gunpowder, 8, prot );
		doEnchantment( Enchantment.projectileProtection, 3, Items.arrow, 8, prot );
		doEnchantment( Enchantment.respiration, 2, Items.nether_wart, 4, Items.reeds, 4, null );
		doEnchantment( Enchantment.aquaAffinity, 1, Items.water_bucket, 8, eff );
		doEnchantment( Enchantment.thorns, 2, Items.iron_ingot, 4, Item.getItemFromBlock( Blocks.cactus ), 4, null );
		
		doEnchantment( Enchantment.sharpness, 4, Items.iron_ingot, 6, Items.flint, 2, null );
		doEnchantment( Enchantment.smite, 4, Items.rotten_flesh, 4, Items.bone, 4, sharp );
		doEnchantment( Enchantment.baneOfArthropods, 4, Items.fermented_spider_eye, 4, Items.string, 4, sharp );
		doEnchantment( Enchantment.knockback, 1, Items.snowball, 5, Items.egg, 3, null );
		doEnchantment( Enchantment.fireAspect, 1, Items.blaze_powder, 4, null );
		doEnchantment( Enchantment.looting, 2, Item.getItemFromBlock( Blocks.lapis_block ), 4, null );
		
		doEnchantment( Enchantment.efficiency, 4, Item.getItemFromBlock( Blocks.redstone_block ), 4, null );
		doEnchantment( Enchantment.silkTouch, 1, Items.diamond, 2, Items.string, 6, null );
		doEnchantment( Enchantment.fortune, 2, Item.getItemFromBlock( Blocks.lapis_block ), 8, null );
		
		doEnchantment( Enchantment.power, 4, Items.iron_ingot, 7, Items.flint, 1, null );
		doEnchantment( Enchantment.punch, 2, Items.snowball, 3, Items.egg, 5, null );
		doEnchantment( Enchantment.flame, 2, Items.magma_cream, 4, null );
		doEnchantment( Enchantment.infinity, 1, Items.diamond, 2, Items.ender_eye, 6, null );
		
		doEnchantment( Enchantment.unbreaking, 2, Items.iron_ingot, 8, null );
	}
	
	private static void doEnchantment( Enchantment ench, int level, Item mat, int amt, EnchantmentData start )
	{
		doEnchantment( ench, level, mat, amt, null, -1, start );
	}
	
	private static void doEnchantment( Enchantment ench, int level, Item mat1, int amt1, Item mat2, int amt2, EnchantmentData start )
	{
		for ( int i = 1; i <= level; ++i )
		{
			ItemStack[] items = new ItemStack[ 8 ];
			int ii = 0;
			for ( ; ii < amt1; ++ii )
			{
				items[ ii ] = new ItemStack( mat1 );
			}
			for ( ; ii < amt1 + amt2; ++ii )
			{
				items[ ii ] = new ItemStack( mat2 );
			}
			EnchantmentData out = new EnchantmentData( ench, i );
			EnchantmentData in = ( i != 1 ) ? new EnchantmentData( ench, i - 1 ) : start;
			EnchantedBookRecipes.addEnchantmentRecipe( items, out, in );
		}
	}
}
