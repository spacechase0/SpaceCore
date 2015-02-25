package com.spacechase0.minecraft.spacecore.asm.obf;

import java.util.Map;
import java.util.Map.Entry;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfuscatedField
{
	protected ObfuscatedField( String theObfType, String theObfName, String theObfDesc, String theSrgName, String theDeobfType, String theDeobfName, String theDeobfDesc )
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
		return getClass().getSimpleName() + "[ " + obfDesc + " " + obfType + "." + obfName + " -> " + deobfDesc + " " + deobfType + "." + mcpName + " (" + srgName + ") ]";
	}
	
	@Override
	public boolean equals( Object obj )
	{
		if ( !( obj instanceof ObfuscatedField ) )
		{
			return false;
		}
		ObfuscatedField f = ( ObfuscatedField ) obj;
		
		return ( f.deobfType.equals( deobfType ) && f.mcpName.equals( mcpName ) && f.deobfDesc.equals( deobfDesc ) );
	}
	
	public static ObfuscatedField fromObf( String type, String name )
	{
		ObfuscatedField obj = ObfuscationUtils.getFieldFromObf( type, name );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedField( type, name, obj.obfDesc, obj.mcpName, type, name, obj.deobfDesc );
	}
	
	public static ObfuscatedField fromSrg( String type, String name )
	{
		ObfuscatedField obj = ObfuscationUtils.getFieldFromSrg( type, name );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedField( type, name, obj.obfDesc, obj.mcpName, type, name, obj.deobfDesc );
	}
	
	public static ObfuscatedField fromMcp( String type, String name )
	{
		ObfuscatedField obj = ObfuscationUtils.getFieldFromMcp( type, name );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedField( type, name, obj.obfDesc, obj.mcpName, type, name, obj.deobfDesc );
	}
	
	public final String obfType;
	public final String obfName;
	public final String obfDesc;
	
	public final String srgName;
	
	public final String deobfType;
	public final String mcpName;
	public final String deobfDesc;
}
