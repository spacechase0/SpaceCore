package com.spacechase0.minecraft.spacecore.block;

import net.minecraft.util.IIcon;

public interface ConnectedBlock
{
	public IIcon getConnectedIcon( int meta, int num );
	public IIcon getBorderIcon( int meta ); // For panes only
	public void setIconArray( IIcon[] theIcons );
	public IIcon[] getDefaultIconArray( int meta );
}
