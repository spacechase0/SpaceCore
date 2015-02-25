package com.spacechase0.minecraft.spacecore.client.render;

import com.spacechase0.minecraft.spacecore.client.event.OrientationRotationEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class HelmetEffectsHandler
{
	@SubscribeEvent
	public void rotation( OrientationRotationEvent event )
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		if ( mc.thePlayer == null )
		{
			return;
		}
		EntityPlayer player = mc.thePlayer;
		
		if ( player.getCurrentArmor( 3 ) == null )
		{
			return;
		}
		ItemStack helmet = player.getCurrentArmor( 3 );
		
		if ( helmet.getDisplayName().equals( "Dinnerbone" ) || helmet.getDisplayName().equals( "Grumm" ) )
		{
			event.rotation += 180;
		}
	}
}
