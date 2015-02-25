package com.spacechase0.minecraft.spacecore.asm.obf;

import java.util.Map;
import java.util.Map.Entry;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfuscatedMethod
{
	protected ObfuscatedMethod( String theObfType, String theObfName, String theObfDesc, String theSrgName, String theDeobfType, String theDeobfName, String theDeobfDesc )
	{
		obfType = theObfType;
		obfName = theObfName;
		obfDesc = theObfDesc;
		
		srgName = theSrgName;
		
		deobfType = theDeobfType;
		mcpName = theDeobfName;
		deobfDesc = theDeobfDesc;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[ " + obfType + "." + obfName + obfDesc + " -> " + deobfType + "." + mcpName + deobfDesc + " (" + srgName + ") ]";
	}
	
	@Override
	public boolean equals( Object obj )
	{
		if ( !( obj instanceof ObfuscatedMethod ) )
		{
			return false;
		}
		ObfuscatedMethod m = ( ObfuscatedMethod ) obj;
		
		return ( m.deobfType.equals( deobfType ) && m.mcpName.equals( mcpName ) && m.deobfDesc.equals( deobfDesc ) );
	}
	
	public static ObfuscatedMethod fromObf( String type, String name, String desc )
	{
		ObfuscatedMethod obj = ObfuscationUtils.getMethodFromObf( type, name, desc );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedMethod( type, name, desc, obj.mcpName, type, name, desc );
	}
	
	public static ObfuscatedMethod fromSrg( String type, String name, String desc )
	{
		ObfuscatedMethod obj = ObfuscationUtils.getMethodFromSrg( type, name, desc );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedMethod( type, obj.mcpName, desc, obj.mcpName, type, obj.mcpName, desc );
	}
	
	public static ObfuscatedMethod fromMcp( String type, String name, String desc )
	{
		ObfuscatedMethod obj = ObfuscationUtils.getMethodFromMcp( type, name, desc );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedMethod( type, name, desc, obj.mcpName, type, name, desc );
	}
	
	// Should I have kept the deobfuscateDescriptor methods?
	// Add it back from old versions if I need it
	
	public final String obfType;
	public final String obfName;
	public final String obfDesc;
	
	public final String srgName;
	
	public final String deobfType;
	public final String mcpName;
	public final String deobfDesc;
}
