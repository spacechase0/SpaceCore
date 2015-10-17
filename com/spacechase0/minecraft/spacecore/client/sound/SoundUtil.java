package com.spacechase0.minecraft.spacecore.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class SoundUtil
{
	public static void playSound( String name )
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(name), 1.0F));
	}
}
