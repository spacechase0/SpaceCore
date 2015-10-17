package com.spacechase0.minecraft.spacecore.asm.mcp;

import static org.objectweb.asm.Opcodes.*;

import java.util.Iterator;
import java.util.ListIterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;

// This class is so that the mcmod.info gets loaded in MCP
public class LoaderModInfoTransformer implements IClassTransformer
{
    public LoaderModInfoTransformer()
    {
    }
    
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
    	if ( bytes == null || ObfuscationUtils.isRuntimeDeobfuscated() || !transformedName.equals( "net.minecraftforge.fml.common.Loader" ) )
    	{
    		return bytes;
    	}
    	
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
        	MethodNode method = it.next();
        	
        	if ( method.name.equals( "<init>" ) )
        	{
        		SpaceCoreLog.fine( "Found Loader constructor, hacking mcmod.info generation." );
        		injectInterceptor( method, name );

                ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
                classNode.accept( writer );

                return writer.toByteArray();
        	}
        }
        
        return bytes;
    }
    
    private void injectInterceptor( MethodNode method, String realName )
    {
    	AbstractInsnNode at = null;
    	ListIterator< AbstractInsnNode > it = method.instructions.iterator();
    	while ( it.hasNext() )
    	{
    		AbstractInsnNode insn = it.next();
			if ( insn.getOpcode() == RETURN )
			{
				at = insn;
			}
    	}
    	
    	if ( at != null )
    	{
    		SpaceCoreLog.fine( "Found return, injecting generation." );
    		
    		String stackClass = ObfuscationUtils.asmify( realName );
    		
    		InsnList instructions = new InsnList();
    		instructions.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/spacecore/mcp/ModInfoGenerator", "generate", "()V" ) );
    		//instructions.add( new InsnNode( POP ) );
    		
    		method.instructions.insertBefore( at, instructions );
    	}
    }
}
