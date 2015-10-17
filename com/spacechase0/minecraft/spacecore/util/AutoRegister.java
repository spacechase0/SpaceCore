package com.spacechase0.minecraft.spacecore.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import net.minecraftforge.common.config.Configuration;

import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;

public abstract class AutoRegister
{
	protected abstract boolean isValid( Class c );
	protected abstract void registerSpecific( Field field, ModObject annot, Object result );

	public void register( BaseMod mod, Configuration config )
	{
		int idCounter = 0;
		
		Class c = this.getClass();
		Field[] fields = c.getDeclaredFields();
		for ( Field field : fields )
		{
			if ( !field.isAnnotationPresent( ModObject.class ) )
			{
				continue;
			}
			ModObject annot = field.getAnnotation( ModObject.class );
			
			if ( annot.skip() ) continue;
			
			Class objectClass = field.getType();
			if ( !isValid( objectClass ) )
			{
				SpaceCoreLog.severe( "@ModObject annotation (" + annot + ") was given to invalid field (" + field + ")." );
				continue;
			}
			
			Field params = null;
			try
			{
				params = c.getField( field.getName() + "Params" );
			}
			catch ( Exception exception )
			{
			}

			String name = annot.name();
			if ( name.equals( "" ) )
			{
				name = field.getName();
			}
			
			boolean enabled = true;
			if ( annot.optional() )
			{
				enabled = config.getBoolean( "enabled", name, true, "Enable " + name );
			}
			
			if ( !enabled )
			{
				continue;
			}
			
			Object[] paramArray = new Object[ 0 ];
			if ( params != null )
			{
				try
				{
					Object[] extraParamArray = ( Object[] ) params.get( this );
					if ( extraParamArray != null )
					{
						paramArray = extraParamArray;
					}
				}
				catch ( Exception exception )
				{
					exception.printStackTrace();
				}
			}
			
			try
			{
				SpaceCoreLog.fine( "Registering " + name + " object..." );
				Constructor constructor = objectClass.getDeclaredConstructor( getClasses( paramArray ) );
				Object result = constructor.newInstance( paramArray );
				
				field.set( this, result );
				registerSpecific( field, annot, result );
			}
			catch ( Exception exception )
			{
				SpaceCoreLog.severe( "Failed to create object " + name + "." );
				exception.printStackTrace();
			}
		}
	}
	
	protected Class[] getClasses( Object[] array )
	{
		Class[] classes = new Class[ array.length ];
		for ( int i = 0; i < classes.length; ++i )
		{
			classes[ i ] = array[ i ].getClass();
			if ( classes[ i ].equals( Integer.class ) )
			{
				classes[ i ] = Integer.TYPE;
			}
			else if ( classes[ i ].equals( Boolean.class ) )
			{
				classes[ i ] = Boolean.TYPE;
			}
			else if ( classes[ i ].equals( Byte.class ) )
			{
				classes[ i ] = Byte.TYPE;
			}
			else if ( classes[ i ].equals( Short.class ) )
			{
				classes[ i ] = Short.TYPE;
			}
			else if ( classes[ i ].equals( Long.class ) )
			{
				classes[ i ] = Long.TYPE;
			}
			else if ( classes[ i ].equals( Float.class ) )
			{
				classes[ i ] = Float.TYPE;
			}
			else if ( classes[ i ].equals( Double.class ) )
			{
				classes[ i ] = Double.TYPE;
			}
		}
		
		return classes;
	}
}
