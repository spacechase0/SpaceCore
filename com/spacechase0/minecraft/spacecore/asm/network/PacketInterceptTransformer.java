package com.spacechase0.minecraft.spacecore.asm.network;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedType;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;

public class PacketInterceptTransformer implements IClassTransformer
{
    public PacketInterceptTransformer()
    {
    }
    
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
    	if ( bytes == null )
    	{
    		return bytes;
    	}
    	
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        ObfuscatedType netMan = null;
        ObfuscatedType obfClass = ObfuscatedType.fromObf( classNode.superName );
        
        if ( !obfClass.deobfName.equals( "net/minecraft/network/Packet" ) )
        {
        	//for ( String obj : classNode.interfaces )
        	{
                obfClass = ObfuscatedType.fromObf( classNode.superName );
                if ( obfClass.deobfName.equals( "net/minecraft/network/NetworkManager" ) )
                {
                	netMan = obfClass;
                	//break;
                }
        	}
        	
        	if ( netMan == null )
        	{
        		return bytes;
        	}
        }
        SpaceCoreLog.fine( "Examining class " + transformedName + "..." );
        
        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
        	MethodNode method = it.next();
        	ObfuscatedMethod obfMethod = ObfuscatedMethod.fromObf( name, method.name, method.desc );
        	String mappedProcessFunc = ObfuscatedMethod.fromSrg( "net/minecraft/network/Packet", "func_148833_a", "(Lnet/minecraft/network/INetHandler;)V" ).obfName; // processPacket
        	String mappedSendFunc = ObfuscatedMethod.fromSrg( "net/minecraft/network/NetworkManager", "func_150725_a", "(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V" ).obfName; // Add to send queue
        	//System.out.println(obfMethod+" "+mappedProcessFunc+" "+mappedSendFunc);
        	
        	boolean write = false;
        	if ( netMan == null && obfMethod.obfName.equals( mappedProcessFunc ) && obfMethod.deobfDesc.equals( "(Lnet/minecraft/network/INetHandler;)V" ) )
        	{
        		SpaceCoreLog.fine( "Found processPacket(...) for " + transformedName );
        		injectProcessInterceptor( method, classNode.superName, name, method.desc.substring( method.desc.indexOf( 'L' ) + 1, method.desc.indexOf( ';' ) ) );
        		write = true;
        	}
        	else if ( netMan != null && obfMethod.obfName.equals( mappedSendFunc ) && obfMethod.deobfDesc.equals( "(Lnet/minecraft/network/packet/Packet;)V" ) )
        	{
        		SpaceCoreLog.fine( "Found addToSendQueue(...) for " + transformedName );
        		injectSendInterceptor( method, netMan.obfName, method.desc.substring( method.desc.indexOf( 'L' ) + 1, method.desc.indexOf( ';' ) ) );
        		write = true;
        	}
        	
        	if ( write )
        	{
                ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
                classNode.accept( writer );
                return writer.toByteArray();
        	}
        }
        
        return bytes;
    }
    
    private void injectProcessInterceptor( MethodNode method, String parent, String realName, String methodParam )
    {
		SpaceCoreLog.fine( "Injecting at beginning of method...." );
		
		String realParent = ObfuscationUtils.asmify( parent );
		String realClass = ObfuscationUtils.asmify( realName );
		
		InsnList instructions = new InsnList();
		instructions.add( new VarInsnNode( ALOAD, 1 ) );
		instructions.add( new VarInsnNode( ALOAD, 0 ) );
		instructions.add( new TypeInsnNode( CHECKCAST, realParent ) );
		instructions.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/spacecore/network/PacketInterceptor", "intercept", "(L" + methodParam + ";L" + realParent + ";)V" ) );
		//System.out.println( methodParam+" "+realClass );
		
		method.instructions.insertBefore( method.instructions.get( 0 ), instructions );
    }
    
    private void injectSendInterceptor( MethodNode method, String parent, String methodParam )
    {
		SpaceCoreLog.fine( "Injecting at beginning of method...." );
		
		String realParent = ObfuscationUtils.asmify( parent );
		//String realClass = ObfuscationUtils.asmify( realName );
		
		InsnList instructions = new InsnList();
		instructions.add( new VarInsnNode( ALOAD, 0 ) );
		instructions.add( new VarInsnNode( ALOAD, 1 ) );
		instructions.add( new TypeInsnNode( CHECKCAST, realParent ) );
		instructions.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/spacecore/network/PacketInterceptor", "intercept", "(L" + realParent + ";L" + methodParam + ";)V" ) );
		//System.out.println( methodParam+" "+realClass );
		
		method.instructions.insertBefore( method.instructions.get( 0 ), instructions );
    }
}
