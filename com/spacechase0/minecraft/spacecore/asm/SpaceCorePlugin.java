package com.spacechase0.minecraft.spacecore.asm;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.compress.utils.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;
import com.spacechase0.minecraft.spacecore.util.FileUtils;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

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
				"com.spacechase0.minecraft.spacecore.asm.render.TextureInterceptTransformer",
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
