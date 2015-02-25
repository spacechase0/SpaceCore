package com.spacechase0.minecraft.spacecore.asm.block;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.util.Iterator;
import java.util.ListIterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedType;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;

public class BlockDestructionTransformer implements IClassTransformer
{
    public BlockDestructionTransformer()
    {
    }
    
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
    	if ( bytes == null || !transformedName.equals( "net.minecraft.server.management.ItemInWorldManager" ) )
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
        	ObfuscatedMethod obfMethod = ObfuscatedMethod.fromObf( name, method.name, method.desc );
        	String mappedHarvestFunc = ObfuscatedMethod.fromMcp( "net/minecraft/server/management/ItemInWorldManager", "tryHarvestBlock", "(III)Z" ).obfName;
        	
        	if ( obfMethod.mcpName.equals( mappedHarvestFunc ) && obfMethod.deobfDesc.equals( "(III)Z" ) )
        	{
        		SpaceCoreLog.fine( "Found ItemInWorldManager.tryHarvestBlock(...)." );
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
    	boolean foundHarvestBlock = false;
    	ListIterator< AbstractInsnNode > it = method.instructions.iterator();
    	while ( it.hasNext() )
    	{
    		AbstractInsnNode insn = it.next();
			if ( insn.getOpcode() == INVOKEVIRTUAL )
			{
				MethodInsnNode node = ( MethodInsnNode ) insn;
				
				ObfuscatedMethod obfMethod = ObfuscatedMethod.fromObf( node.owner, node.name, node.desc );
				String methodName = ObfuscatedMethod.fromMcp( "net/minecraft/server/management/ItemInWorldManager", "removeBlock", "(III)Z" ).obfName;
				
				boolean found = true;
				found = found && ObfuscatedType.fromObf( node.owner ).deobfName.equals( "net/minecraft/block/Block" );
				found = found && obfMethod.mcpName.equals( methodName );
				found = found && obfMethod.deobfDesc.equals( "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;IIII)V" );
				
				if ( found )
				{
					SpaceCoreLog.fine( "Found call to Block.harvestBlock" );
					foundHarvestBlock = true;
				}
			}
			else if ( insn.getOpcode() == ILOAD && foundHarvestBlock )
			{
				at = insn;
			}
    	}
    	
    	if ( at != null )
    	{
			SpaceCoreLog.fine( "Found ILOAD instruction, injecting interceptor." );
    		
    		String iiwmClass = ObfuscationUtils.asmify( realName );
    		
    		InsnList instructions = new InsnList();
    		instructions.add( new VarInsnNode( ALOAD, 0 ) );
    		instructions.add( new VarInsnNode( ILOAD, 1 ) );
    		instructions.add( new VarInsnNode( ILOAD, 2 ) );
    		instructions.add( new VarInsnNode( ILOAD, 3 ) );
    		instructions.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/spacecore/block/BlockDestroyedNotifier", "intercept", "(L" + iiwmClass + ";III)V" ) );
    		
    		method.instructions.insertBefore( at, instructions );
    	}
    }
}
