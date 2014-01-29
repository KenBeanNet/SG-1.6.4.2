package mods.scourgecraft;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import mods.scourgecraft.client.gui.GuiHandler;
import mods.scourgecraft.config.ConfigBlocks;
import mods.scourgecraft.creative.CreativeTabBlock;
import mods.scourgecraft.data.ChunkLoadingHandler;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.RaidManager;
import mods.scourgecraft.gen.GenerateHome;
import mods.scourgecraft.network.CommonProxy;
import mods.scourgecraft.network.PacketHandler;
import mods.scourgecraft.network.ConnectionHandler;
import mods.scourgecraft.permission.PermissionEventListener;
import mods.scourgecraft.player.PlayerEventListener;
import mods.scourgecraft.player.PlayerHandler;
import mods.scourgecraft.sound.SoundSystem;
import mods.scourgecraft.tileentity.TileEntityGoldProducer;
import mods.scourgecraft.tileentity.TileEntityGoldStorage;
import mods.scourgecraft.tileentity.TileEntityScourgeBuilding;
import mods.scourgecraft.tileentity.TileEntityHomeHall;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(
        modid = ScourgeCraftCore.modid,
        name = "ScourgeCraft",
        version = "0.1.0"
)
@NetworkMod(
        channels = {"scourgecraft"},
        clientSideRequired = true,
        serverSideRequired = true,
        packetHandler = PacketHandler.class,
        connectionHandler = ConnectionHandler.class
)
public class ScourgeCraftCore
{
    @Mod.Instance("ScourgeCraft")
    public static ScourgeCraftCore instance;
    public static final String modid = "ScourgeCraft";
    
    @SidedProxy(clientSide = "mods.scourgecraft.network.ClientProxy", serverSide = "mods.scourgecraft.network.CommonProxy")
    public static CommonProxy proxy;
    
    public static CreativeTabBlock tabBlocks;
    
    public static ConfigBlocks configBlocks;
    
    public static PermissionEventListener permissionEventListener;
    public static PlayerEventListener playerEventListener;
    
    @EventHandler
    public void loadConfigurationValues(FMLPreInitializationEvent event)
    {
    	tabBlocks = new CreativeTabBlock("SG : Blocks");

    	configBlocks = new ConfigBlocks();
    	
    	if(FMLCommonHandler.instance().getSide().isClient())
        {
        	MinecraftForge.EVENT_BUS.register(new SoundSystem());
        }
    }
    
    @EventHandler
    public void load(FMLInitializationEvent var1)
    {
        configBlocks.initConfig();
        
        configBlocks.load();
    	configBlocks.register();
    	configBlocks.languageRegister();
    	
    	new GuiHandler();
    	
    	proxy.registerHandlers();
    	
    	permissionEventListener = new PermissionEventListener();
		playerEventListener = new PlayerEventListener();
		
    	MinecraftForge.EVENT_BUS.register(permissionEventListener);
    	MinecraftForge.EVENT_BUS.register(playerEventListener);
    	
    	GameRegistry.registerWorldGenerator(new GenerateHome());
    	
    	ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoadingHandler());
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
    	GameRegistry.registerPlayerTracker(new PlayerHandler());
    	HomeManager.loadHomes();
    }
}