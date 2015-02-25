package com.spacechase0.minecraft.spacecore.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface ModObject
{
	String name() default "";
	boolean skip() default false;
	boolean optional() default false;
}
