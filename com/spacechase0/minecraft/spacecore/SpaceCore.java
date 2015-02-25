package com.spacechase0.minecraft.spacecore;

import com.spacechase0.minecraft.spacecore.config.AutoConfig;
import com.spacechase0.minecraft.spacecore.craftableenchantmentbooks.CraftableEnchantedBooks;
import com.spacechase0.minecraft.spacecore.item.CoreItems;
import com.spacechase0.minecraft.spacecore.network.PacketCodec;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

// 0.7.13 - Temporarily disabled config gui (crashes when selected on mods screen, and LunatriusCore).
// 0.7.12 - Translate utils crash fix.
// 0.7.11 - More model path fixes.
// 0.7.10 - Fixed model TE renderer bug.
// 0.7.9 - In-game config support test. Made BaseMod mods inherit preInit/init/postInit (as if they declared it themselves, so FML detects it) if they do not specify it explicitly. Disabled debugLightDisplay by default (in case it causes issues again in the future). Removed recipes since they can be done with MineTweaker, and are kinda meh anyways. Removed 'experimental' option for @ModObject.
// 0.7.8 - Fixed old book recipe recipe.
// 0.7.7 - Removed the vanilla override stuff, it doesn't work anymore and FML will have a replacement.
// 0.7.6 - Redid some networking stuff.
// 0.7.5 - Fixed old book recipe.
// 0.7.4 - Moved nuggets from EnderTech to SpaceCore. Fixed AutoRegister bug for items/blocks with no parameters. Hopefully fixed bug with things using Obfuscated___.srgName not working in MCP. Fixed up ReflectionUtils (which I hardly ever use!).
// 0.7.3 - Fixed craftable enchantment books using the config option of cracked stone bricks.
// 0.7.2 - Merged the old recipe "mods". Config file, disabled by default.
// 0.7.1 - Added stuff to override vanilla blocks/items. Added+fixed some stuff for connected block rendering.
// 0.7 - Changed a lot of stuff for Minecraft 1.7. One small thing left unimplemented, will do later.
// 0.6.2 - Fixed server crash with Component Equipment.
// 0.6.1 - Lighting debug stuff. Shows on F3, can be disabled in config file.
// 0.6 - Added ability for blocks/items to be optional (set ID to -1) or experimental. Added camera stuff for a future mod. Fixed coremod silliness (excluding spacecore instead of spacecore.asm). Cleaned up common code between base Blocks and Items, although it breaks almost all depending mods.
// 0.5.3 - Actually fix 0.5.2.
// 0.5.2 - Minor bugfix and tweak.
// 0.5.1 - Hopefully fixed server crashing. Silly client-only getters.
// 0.5 - Moved item/block init to preinit instead of init for mods using BaseMod. Added ability to override how language files are registered, allows for more organization for larger mods. Added another "Easter" egg.
// 0.4.1 - Fixed bug with custom smelters dropping items when smelter changed state.
// 0.4 - Fixed bug with tile entities remaining for custom smelters. Affects Component Equipment and Decorative Stuff. Changed some stuff for coremods.
// 0.3.3 - Fixed bug relating to Component Equipment recipe guides.
// 0.3.2 - Removed tooltip interceptor, should have used Forge event.
// 0.3.1 - Fixed compatibility with Larger Inventory/Hotbar.
// 0.3 - New "feature" in the code, specifically for Larger Inventory but generally useful as well. Also other fun stuff.
// 0.2.11 - Added class for Component Equipment.
// 0.2.10 - Fixed any relying mods silently overwriting other mod items if there is an ID conflict.
// 0.2.9 - Important update for Component Equipment.
// 0.2.8 - Another crashing bugfix, affects both Component Equipment and Decorative Stuff. Added something for Component Equipment.
// 0.2.7 - Another crashing bugfix for Component Equipment.
// 0.2.6 - Fixed crashing bug, applies to Component Equipment.
// 0.2.5 - Stuff for Component Equipment.
// 0.2.4 - Stuff for Chunk-in-a-Jar.
// 0.2.3 - More stuff for UsefulPets, + other mods
// 0.2.2 - Made it show up in mods list. Added stuff for UsefulPets (+others in the future)

// (0.2.9 dup fix for chestplate backpack)

// TODO: New ASM handler to auto-inject things to subclasses of BaseMod if needed
//       Base config handling stuff

@AutoConfig( categories = { "general", "enabled" } )
@Mod( modid = "SC0_SpaceCore", useMetadata = true/*, guiFactory = "com.spacechase0.minecraft.spacecore.config.GuiFactory_ASM"*/ )
public class SpaceCore extends BaseMod
{
	public SpaceCore()
	{
		super( "spacecore" );
	}
	
	@Instance( value = "SC0_SpaceCore" )
	public static SpaceCore instance;
	
	@SidedProxy( clientSide = "com.spacechase0.minecraft.spacecore.client.ClientProxy",
	             serverSide = "com.spacechase0.minecraft.spacecore.CommonProxy" )
	public static CommonProxy proxy;
	
	@Override
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		super.init( event );

		MinecraftForge.EVENT_BUS.register( starterItemHandler = new StarterItemEventHandler() );
		proxy.init();
	}
	
	public static Configuration config;
	public static CoreItems items;
	
	private StarterItemEventHandler starterItemHandler;
}
