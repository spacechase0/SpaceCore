package com.spacechase0.minecraft.spacecore.asm.mod;

import static org.objectweb.asm.Opcodes.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.ListIterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;

// Automatically add non-existing preinit/init/postinit methods
// Also populates ModPackageIndex
public class ModInitTransformer implements IClassTransformer
{
    public ModInitTransformer()
    {
    }
    
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
    	if ( bytes == null ) return bytes;
    	
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        if ( classNode.superName.equals( "com/spacechase0/minecraft/spacecore/BaseMod" ) )
        {
        	checkInits( classNode );
        	ModPackageIndex.setModForPackage( name.substring( 0, name.lastIndexOf( '.' ) ), name.substring( name.lastIndexOf( '.' ) + 1 ) );
        	
            ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
            classNode.accept( writer );
            return writer.toByteArray();
        }
        
        return bytes;
    }

	// Add non-existing preinit/init/postinit methods
    private void checkInits( ClassNode classNode )
    {
    	boolean hasPre = false, hasInit = false, hasPost = false;
        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
        	MethodNode method = it.next();
        	if ( method.desc.contains( "FMLPreInitializationEvent" ) )
        	{
        		hasPre = true;
        	}
        	if ( method.desc.contains( "FMLInitializationEvent" ) )
        	{
        		hasInit = true;
        	}
        	if ( method.desc.contains( "FMLPostInitializationEvent" ) )
        	{
        		hasPost = true;
        	}
        }
        
        if ( !hasPre )
        {
        	addEventMethod( classNode, "preInit", "net/minecraftforge/fml/common/event/FMLPreInitializationEvent" );
        }
        
        if ( !hasInit )
        {
        	addEventMethod( classNode, "init", "net/minecraftforge/fml/common/event/FMLInitializationEvent" );
        }
        
        if ( !hasPost )
        {
        	addEventMethod( classNode, "postInit", "net/minecraftforge/fml/common/event/FMLPostInitializationEvent" );
        }
    }
    
    private void addEventMethod( ClassNode classNode, String name, String type )
    {
    	SpaceCoreLog.info( "Adding artificial " + name + " to " + classNode.name );
    	
    	String desc = "(L" + type + ";)V";
    	MethodNode method = new MethodNode( ACC_PUBLIC, name, desc, null, null );
    	method.visitAnnotation( "Lnet/minecraftforge/fml/common/Mod$EventHandler;", true );
    	
    	method.instructions.add( new VarInsnNode( ALOAD, 0 ) );
    	method.instructions.add( new VarInsnNode( ALOAD, 1 ) );
    	method.instructions.add( new MethodInsnNode( INVOKESPECIAL, "com/spacechase0/minecraft/spacecore/BaseMod", name, desc ) );
    	method.instructions.add( new InsnNode( RETURN ) );
    	
    	classNode.methods.add( method );
    }
}
