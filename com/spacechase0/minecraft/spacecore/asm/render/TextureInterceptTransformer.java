package com.spacechase0.minecraft.spacecore.asm.render;

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
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedType;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;

public class TextureInterceptTransformer implements IClassTransformer
{
    public TextureInterceptTransformer()
    {
    }
    
    public byte[] transform( String name, String transformedName, byte[] bytes )
    {
    	if ( bytes == null || !transformedName.equals( "net.minecraft.client.renderer.texture.TextureManager" ) )
    	{
    		return bytes;
    	}
		SpaceCoreLog.fine( "Found TextureManager." );
    	
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
        	MethodNode method = it.next();
        	ObfuscatedMethod obfMethod = ObfuscatedMethod.fromObf( name, method.name, method.desc );
        	
        	String obfType = ObfuscationUtils.asmify( transformedName );
        	
        	ObfuscatedMethod bindTexture = ObfuscatedMethod.fromMcp( obfType, "bindTexture", "(Lnet/minecraft/util/ResourceLocation;)V" );
        	
        	if ( obfMethod.equals( bindTexture ) )
        	{
        		SpaceCoreLog.fine( "Found TextureManager.bindTexture(...)." );
        		injectInterceptor( method, name );

                ClassWriter writer = new ClassWriter( /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS );
                classNode.accept( writer );
                return writer.toByteArray();
        	}
        }
        
        return bytes;
    }
    
    private void injectInterceptor( MethodNode method, String realName )
    {
		SpaceCoreLog.fine( "Injecting into beginning of method." );
		
		String stackClass = ObfuscationUtils.asmify( realName );
		
		ObfuscatedType resLoc = ObfuscatedType.fromDeobf( "net/minecraft/util/ResourceLocation" );
		
		InsnList instructions = new InsnList();
		instructions.add( new VarInsnNode( ALOAD, 1 ) );
		instructions.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/spacecore/client/render/TextureOverrides", "intercept", "(L" + resLoc.obfName + ";)L" + resLoc.obfName + ";" ) );
		instructions.add( new VarInsnNode( ASTORE, 1 ) );
		
		method.instructions.insertBefore( method.instructions.getFirst(), instructions );
    }
}
