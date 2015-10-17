package com.spacechase0.minecraft.spacecore.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;

@TransformerExclusions( value = { "com.spacechase0.minecraft.spacecore.asm" } )
public class SpaceCorePlugin implements IFMLLoadingPlugin
{
	public SpaceCorePlugin()
	{
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]
			   {
				"com.spacechase0.minecraft.spacecore.asm.block.BlockDestructionTransformer",
				"com.spacechase0.minecraft.spacecore.asm.network.PacketInterceptTransformer",
				"com.spacechase0.minecraft.spacecore.asm.mcp.LoaderModInfoTransformer",
				"com.spacechase0.minecraft.spacecore.asm.mod.ModInitTransformer",
				//"com.spacechase0.minecraft.spacecore.asm.config.AutoConfigTransformer",
				"com.spacechase0.minecraft.spacecore.asm.config.GuiFactoryTransformer"
			   };
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return "com.spacechase0.minecraft.spacecore.asm.SpaceCoreCallHook";
	}

	@Override
	public void injectData( Map< String, Object > data )
	{
		ObfuscationUtils.setRuntimeDeobfuscated( ( Boolean ) data.get( "runtimeDeobfuscationEnabled" ) );
	}
}
