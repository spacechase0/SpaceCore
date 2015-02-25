package com.spacechase0.minecraft.spacecore.asm.obf;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfuscatedType
{
	protected ObfuscatedType( String obf, String deobf )
	{
		obfName = obf;
		deobfName = deobf;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[ " + obfName + " -> " + deobfName + " ]";
	}
	
	@Override
	public boolean equals( Object obj )
	{
		if ( !( obj instanceof ObfuscatedType ) )
		{
			return false;
		}
		ObfuscatedType c = ( ObfuscatedType ) obj;
		
		return ( c.obfName.equals( obfName ) && c.deobfName.equals( deobfName ) );
	}
	
	public static ObfuscatedType fromObf( String name )
	{
		ObfuscatedType obj = ObfuscationUtils.getTypeFromObf( name );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedType( name, name );
	}
	
	public static ObfuscatedType fromDeobf( String name )
	{
		ObfuscatedType obj = ObfuscationUtils.getTypeFromDeobf( name );
		return ( ObfuscationUtils.isRuntimeDeobfuscated() ) ? obj : new ObfuscatedType( name, name );
	}
	
	public final String obfName;
	public final String deobfName;
}
