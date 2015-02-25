package com.spacechase0.minecraft.spacecore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.spacechase0.minecraft.spacecore.entity.PlayerUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StarterItemEventHandler
{
	@SubscribeEvent
	public void entityJoinedWorld( EntityJoinWorldEvent event )
	{
		if ( event.world.isRemote || !( event.entity instanceof EntityPlayer ) )
		{
			return;
		}
		EntityPlayer player = ( EntityPlayer ) event.entity;
		
		NBTTagCompound data = player.getEntityData();
		if ( !data.hasKey( EntityPlayer.PERSISTED_NBT_TAG ) )
		{
			data.setTag( EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound() );
		}
		NBTTagCompound persist = data.getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
		
		Iterator< Map.Entry< String, ItemStack > > it = items.entrySet().iterator();
		while ( it.hasNext() )
		{
			Map.Entry< String, ItemStack > entry = it.next();
			String checkTag = entry.getKey();
			ItemStack stack = entry.getValue();
			
			PlayerUtils.giveItemOnce( player, checkTag, stack );
		}
	}
	
	public static void addStarterItem( String tag, ItemStack stack )
	{
		items.put( "Received" + tag, stack.copy() );
	}
	
	private static Map< String, ItemStack > items = new HashMap< String, ItemStack >();
}
