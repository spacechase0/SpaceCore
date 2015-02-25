package com.spacechase0.minecraft.spacecore.client.event;

import cpw.mods.fml.common.eventhandler.Event;

public class OrientationRotationEvent extends Event
{
	public float rotation;
	
	public OrientationRotationEvent( float theRotation )
	{
		rotation = theRotation;
	}
}
