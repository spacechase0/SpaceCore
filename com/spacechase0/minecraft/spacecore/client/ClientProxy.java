package com.spacechase0.minecraft.spacecore.client;

import net.minecraftforge.fml.common.FMLCommonHandler;

import com.spacechase0.minecraft.spacecore.CommonProxy;
import com.spacechase0.minecraft.spacecore.client.tick.SplashHandler;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		FMLCommonHandler.instance().bus().register( splash = new SplashHandler() );

		// Why was this here?
		//Minecraft.getMinecraft().refreshResources();
	}
	
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	private SplashHandler splash;
}
