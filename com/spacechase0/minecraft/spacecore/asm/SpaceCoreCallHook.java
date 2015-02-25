package com.spacechase0.minecraft.spacecore.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLCallHook;

public class SpaceCoreCallHook implements IFMLCallHook
{
	public SpaceCoreCallHook()
	{
	}
	
	@Override
	public Void call() throws Exception
	{
		return null;
	}

	@Override
	public void injectData( Map<String, Object> data )
	{
		//RelaunchClassLoader classLoader = ( RelaunchClassLoader ) data.get( "classLoader" );
		//classLoader.registerTransformer( "com.spacechase0.minecraft.spacecore.asm.gui.TooltipTransformer" );
	}
}
