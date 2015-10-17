package com.spacechase0.minecraft.spacecore.asm.config;

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

// No longer active - FML reads the annotations before I get a chance to edit them.
// I could hook in there and fix that, but meh. Another day.
public class AutoConfigTransformer implements IClassTransformer
{
    public AutoConfigTransformer()
    {
    }
    
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
    	if ( bytes == null ) return bytes;
    	
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        if ( classNode.visibleAnnotations == null ) return bytes;
        
        Iterator< AnnotationNode > it = classNode.visibleAnnotations.iterator();
        while ( it.hasNext() )
        {
        	AnnotationNode annot = it.next();
        	if ( annot.desc.equals( "Lcom/spacechase0/minecraft/spacecore/config/AutoConfig;" ) )
        	{
        		setGuiFactory( classNode );
            	
                ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
                classNode.accept( writer );
                return writer.toByteArray();
        	}
        }
        
        return bytes;
    }

    private void setGuiFactory( ClassNode classNode )
    {
        Iterator< AnnotationNode > it = classNode.visibleAnnotations.iterator();
        while ( it.hasNext() )
        {
        	AnnotationNode annot = it.next();
        	if ( annot.desc.equals( "Lnet/minecraftforge/fml/common/Mod;" ) )
        	{
        		// TODO: Check for existing value
        		
        		String factory = classNode.name.replace( '/', '.' ).substring( 0, classNode.name.lastIndexOf( '/' ) ) + ".config.GuiFactory_ASM";
        		annot.values.add( "guiFactory" );
        		annot.values.add( factory );
        		
        		return;
        	}
        }
        
        SpaceCoreLog.warning( "No @Mod for class with @AutoConfig" );
    }
}
