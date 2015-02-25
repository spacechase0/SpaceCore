package com.spacechase0.minecraft.spacecore.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.item.ItemBlock;

@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface BlockItem
{
	Class item();
}
