package com.spacechase0.minecraft.spacecore.asm.config;

import static org.objectweb.asm.Opcodes.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.io.ByteStreams;
import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.asm.mod.ModPackageIndex;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedType;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;
import com.spacechase0.minecraft.spacecore.util.FileUtils;

public class GuiFactoryTransformer implements IClassTransformer
{
    public GuiFactoryTransformer()
    {
    	try
    	{
    		baseConfigGui = ByteStreams.toByteArray( getClass().getResourceAsStream( "/com/spacechase0/minecraft/spacecore/config/BaseConfigGui.class" ) );
    		baseDummyEntry = ByteStreams.toByteArray( getClass().getResourceAsStream( "/com/spacechase0/minecraft/spacecore/config/BaseConfigGui$DummyEntry.class" ) );
    	}
    	catch ( Exception exception )
    	{
    		SpaceCoreLog.severe( "Failed to load base config GUI bytes, things WILL crash horribly!" );
    		exception.printStackTrace();
    	}
    }
    
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
    	// We base the artificial config gui class on this, unlike with the gui factory.
    	/*
    	if ( name.equals( "com.spacechase0.minecraft.spacecore.config.BaseConfigGui" ) )
    	{
    		System.out.println( "Found base config gui: " + bytes );
    		baseConfigGui = bytes;
    	}
    	*/
    	
    	if ( bytes != null ) return bytes;
    	
    	if ( name.startsWith( "com.spacechase0.minecraft." ) )
    	{
    		if ( name.endsWith( ".config.GuiFactory_ASM" ) )
	    	{
	    		return createFactory( name );
	    	}
	    	else if ( name.endsWith( ".config.ConfigGui_ASM" ) )
	    	{
	    		return createConfigGui( name );
	    	}
    	}
    	
    	// Check toDefine
    	
        return null;
    }

    private byte[] createFactory( String name )
    {
        ClassNode classNode = new ClassNode();
        classNode.access = ACC_PUBLIC;
        classNode.name = name.replace( '.', '/' );
        classNode.signature = "L" + classNode.name + ";";
        classNode.superName = "com/spacechase0/minecraft/spacecore/config/BaseGuiFactory";
        classNode.version = V1_6;
        
        MethodNode init = new MethodNode( ACC_PUBLIC, "<init>", "()V", null, null );
        init.instructions.add( new VarInsnNode( ALOAD, 0 ) );
        init.instructions.add( new MethodInsnNode( INVOKESPECIAL, "com/spacechase0/minecraft/spacecore/config/BaseGuiFactory", "<init>", "()V" ) );
        init.instructions.add( new InsnNode( RETURN ) );
        classNode.methods.add( init );
        
        MethodNode configGui = new MethodNode( ACC_PUBLIC, "mainConfigGuiClass", "()Ljava/lang/Class;", "()Ljava/lang/Class<+L" + ObfuscatedType.fromDeobf( "net/minecraft/client/gui/GuiScreen" ).obfName + ";>;", null );
        String type = name.substring( 0, name.lastIndexOf( '.' ) ) + ".ConfigGui_ASM";
        type = type.replace( '.', '/' );
        System.out.println("type is " + type );
        configGui.instructions.add( new LdcInsnNode( Type.getType( "L" + type + ";" ) ) );
        configGui.instructions.add( new InsnNode( ARETURN ) );
        classNode.methods.add( configGui );
        
        ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
        classNode.accept( writer );
        //FileUtils.saveBytes( name + ".class", writer.toByteArray() );
        return writer.toByteArray();
    }
    
    private byte[] createConfigGui( String name )
    {
    	ClassNode classNode = new ClassNode();
    	ClassReader reader = new ClassReader( baseConfigGui );
    	reader.accept( classNode, 0 );

    	classNode.name = classNode.name.replace( "spacecore", name.substring( name.indexOf( ".minecraft." ) + 11, name.indexOf( ".config" ) ) );
    	classNode.name = classNode.name.replace( "BaseConfigGui", "ConfigGui_ASM" );
    	classNode.signature = "L" + classNode.name + ";";
    	
    	String find = "com/spacechase0/minecraft/spacecore/config/BaseConfigGui";
    	Iterator< MethodNode > mit = classNode.methods.iterator();
    	while ( mit.hasNext() )
    	{
    		MethodNode method = mit.next();
    		
    		// Make sure things refer to the new class, not the old.
    		Iterator< AbstractInsnNode > iit = method.instructions.iterator();
    		while ( iit.hasNext() )
    		{
    			AbstractInsnNode insn = iit.next();
    			if ( insn instanceof MethodInsnNode )
    			{
    				MethodInsnNode minsn = ( MethodInsnNode ) insn;
    				if ( minsn.owner.equals( find ) )
    				{
    					minsn.owner = classNode.name;
    				}
    			}
    		}

			String pkg = name.substring( 0, name.lastIndexOf( '.', name.lastIndexOf( '.' ) - 1 ) );
			String mod = ModPackageIndex.getModForPackage( pkg );
			pkg = pkg.replace( '.', '/' );
			String full = pkg + "/" + mod;
    		
    		// Make getMod gets the proper mod (returns null in base).
    		if ( method.name.equals( "getMod" ) )
    		{
    			method.instructions.clear();
    			method.instructions.add( new FieldInsnNode( GETSTATIC, full, "instance", "L" + full + ";" ) );
    			method.instructions.add( new InsnNode( ARETURN ) );
    		}
    		// Construct it properly...
    		else if ( method.name.equals( "getConfigElements" ) )
    		{
    			// Complicated. :(
    			// 	Find array list constructor (+1 instruction)
    			// 	Find return (-1 instruction)
    			// In between these two (exclusive) is what we want to duplicate... And modify the existing one.
    			// Per-@AutoConfig entry, we need to have one with a proper clone of $DummyEntry,
    			// and change the index of categories that is referenced.
    			
    			// 1. Get @AutoConfig.categories()
    			String[] categories = null;
    			try
    			{
    				String file = "/" + full + ".class";
    				byte[] bytes = ByteStreams.toByteArray( getClass().getResourceAsStream( file ) );
    				
    				ClassNode modClass = new ClassNode();
    				ClassReader modReader = new ClassReader( bytes );
    				modReader.accept( modClass, 0 );
    				
    				Iterator< AnnotationNode > mait = modClass.visibleAnnotations.iterator();
    				while ( mait.hasNext() )
    				{
    					AnnotationNode annot = mait.next();
    					if ( !annot.desc.equals( "Lcom/spacechase0/minecraft/spacecore/util/AutoConfig;" ) )
    					{
    						continue;
    					}
    					
    					categories = ( String[] ) annot.values.get( 1 );
    					break;
    				}
    			}
    			catch ( Exception exception )
    			{
    				SpaceCoreLog.severe( "Failed to get mod class bytes! Things are about to go wrong!" );
    				SpaceCoreLog.severe( "Debug info: " + full );
    				exception.printStackTrace();
    			}
    			
    			// 2. Find what we need to duplicate, rip it out.
    			InsnList addCategory = new InsnList();
    			int sinceList = -1;
    			for ( int i = 0; i < method.instructions.size(); ++i )
    			{
    				AbstractInsnNode insn = method.instructions.get( i );
    				if ( insn instanceof MethodInsnNode )
    				{
    					MethodInsnNode minsn = ( MethodInsnNode ) insn;
    					if ( minsn.owner.equals( "java/util/ArrayList" ) && minsn.name.equals( "<init>" ) )
    					{
    						sinceList = 0;
    						continue;
    					}
    				}
    				else if ( insn instanceof InsnNode )
    				{
    					InsnNode iinsn = ( InsnNode ) insn;
    					if ( iinsn.getOpcode() == ARETURN )
    					{
    						addCategory.remove( addCategory.getLast() );
    						break;
    					}
    				}
    				
    				if ( sinceList >= 0 )
    				{
    					++sinceList;
    				}
    				
    				if ( sinceList > 1 )
    				{
    					addCategory.add( insn );
    				}
    			}
    			
    			// 3. Create the entries' inner classes, duplicating DummyEntry.
    			classNode.innerClasses.clear();
    			for ( String category : categories )
    			{
    				ClassNode innerClass = new ClassNode();
    				ClassReader innerReader = new ClassReader( baseDummyEntry );
    				innerReader.accept( innerClass, 0 );
    				
    				// ...
    				
    		        ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
    		        innerClass.accept( writer );
    		        toDefine.put( pkg.replace( '/',  '.' ) + ".config.ConfigGui_ASM$" + Character.toUpperCase( category.charAt( 0 ) ) + category.substring( 1 ) + "Entry", writer.toByteArray() );
    		        
    		        // put in innerclass field
    			}
    			
    			// 4. Recreate getConfigElements()
    			// ???
    		}
    	}
    	
        ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
        classNode.accept( writer );
        FileUtils.saveBytes( name.substring( name.lastIndexOf( '.' ) + 1 ) + ".class", writer.toByteArray() );
        return writer.toByteArray();
    }

    private byte[] baseConfigGui;
    private byte[] baseDummyEntry;
    private Map< String, byte[] > toDefine = new HashMap< String, byte[] >();
}
