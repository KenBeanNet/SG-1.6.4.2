package mods.scourgecraft.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.helpers.json.JSONObject;
import mods.scourgecraft.network.packet.Packet1HomeInfo;
import mods.scourgecraft.player.ExtendedPlayer;
import mods.scourgecraft.tileentity.TileEntityGoldProducer;
import mods.scourgecraft.tileentity.TileEntityGoldStorage;
import mods.scourgecraft.tileentity.TileEntityHomeHall;
import mods.scourgecraft.tileentity.TileEntityScourgeBuilding;
import mods.scourgecraft.tileentity.TileEntityScourgeResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class HomeManager {

	public static HashMap<String, Home> homeList = new HashMap<String, Home>();
	public static Gson gson = new Gson();
	
	public static void loadHomes()
	{
		File file = new File(ScourgeCraftCore.proxy.getMinecraftDir(), "Homes");
		file.mkdir();
		System.out.print("[ScourgeCraft] Loading Homes.....");
		for (final File fileEntry : file.listFiles()) {
			loadHome(fileEntry.getName().replace(".json", ""));
	    }
		System.out.println("Loaded " + homeList.size() + " home(s)");
	}
	
	public static void createHome(Home home)
	{
		System.out.print("[ScourgeCraft] Creating new home for player " + home.ownerUsername + "......");

        try
        {
        	FileWriter writer = new FileWriter(ScourgeCraftCore.proxy.getMinecraftDir() + "/Homes/" + home.ownerUsername + ".json");
        	writer.write(gson.toJson(home));
        	writer.close();
            homeList.put(home.ownerUsername, home);
        }
        catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	public static void saveHome(Home home)
	{
		try
        {
        	FileWriter writer = new FileWriter(ScourgeCraftCore.proxy.getMinecraftDir() + "/Homes/" + home.ownerUsername + ".json");
        	String longT = gson.toJson(home);
        	System.out.println(longT);
        	writer.write(longT);
        	writer.close();
        }
        catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	public static void playerLogin(EntityPlayer player)
	{
		for (Home h : homeList.values())
		{
			PacketDispatcher.sendPacketToPlayer(new Packet1HomeInfo(h).makePacket(), (Player)player);
		}
			
	}
	
	private static void loadHome(String username)
	{
		Home home = null;
		
		try
        {
			BufferedReader br = new BufferedReader(new FileReader(ScourgeCraftCore.proxy.getMinecraftDir() + "/Homes/" + username + ".json"));
			home = gson.fromJson(br, Home.class);
			br.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        homeList.put(username, home);
	}
	
	public static int getHomeSize(int homeLevel)
	{
		switch (homeLevel)
		{
			case 1:
				return 5;
			case 2: 
				return 8;
		}
		return 1;
	}
	
	public static void distributeResource(EntityPlayer par1Player, TileEntityScourgeResource resource)
	{
		Home home = getHomeByPlayerName(par1Player.username);
		
		if (home != null)
		{
			TileEntityHomeHall teHome = (TileEntityHomeHall)par1Player.worldObj.getBlockTileEntity(home.xCoord, home.yCoord, home.zCoord);
		
			if (resource instanceof TileEntityGoldProducer)
			{
				List<TileEntityScourgeBuilding> list = teHome.getBuildingsByBlock(ScourgeCraftCore.configBlocks.goldStorageID);
				for (int i = 0; i < list.size() && resource.getGold() > 0; i++)
				{
					TileEntityGoldStorage teGold = (TileEntityGoldStorage)list.get(i);
					if (teGold.isCompleted()) // Only storages that are complete can be stored in.
					{
						resource.setGold(teGold.storeGold(resource.getGold()));
						teGold.worldObj.playSoundEffect((double)((float)teGold.xCoord + 0.5F), (double)((float)teGold.yCoord + 0.5F), (double)((float)teGold.zCoord + 0.5F), ScourgeCraftCore.modid.toLowerCase() + ":" + "goldcollect", 2.0f, 2.0f);
					}
				}
			}
		}
	}
	
	//This is called when a RAID is over or given by admin, etc.
	public static void distributeResource(EntityPlayer par1Player, double amount)
	{
		Home home = getHomeByPlayerName(par1Player.username);
		
		if (home != null)
		{
			TileEntityHomeHall teHome = (TileEntityHomeHall)par1Player.worldObj.getBlockTileEntity(home.xCoord, home.yCoord, home.zCoord);
		
			List<TileEntityScourgeBuilding> list = teHome.getBuildingsByBlock(ScourgeCraftCore.configBlocks.goldStorageID);
			for (int i = 0; i < list.size(); i++)
			{
				TileEntityGoldStorage teGold = (TileEntityGoldStorage)list.get(i);
				if (teGold.isCompleted()) // Only storages that are complete can be stored in.
				{
					teGold.storeGold(amount);
					teGold.worldObj.playSoundEffect((double)((float)teGold.xCoord + 0.5F), (double)((float)teGold.yCoord + 0.5F), (double)((float)teGold.zCoord + 0.5F), ScourgeCraftCore.modid.toLowerCase() + ":" + "goldcollect", 2.0f, 2.0f);
				}
			}
		}
	}
	
	public static Home getHomeByPlayerName(String username)
	{
		for (String h : homeList.keySet())
		{
			if (h.equals(username))
				return homeList.get(h);
		}
		return null;
	}
	
	public static boolean IsPointInHome(Home h, int x, int y, int z)
	{
		int widthOfHome = HomeManager.getHomeSize(h.level);
		
		if (h.xCoord - widthOfHome <= x && h.xCoord + widthOfHome >= x 
				&& h.yCoord - widthOfHome <= y && h.yCoord + widthOfHome >= y
				&& h.zCoord - widthOfHome <= z && h.zCoord + widthOfHome >= z )
		{
			return true;
		}
		return false;
	}
	
	public static boolean IsPointInHome(int x, int y, int z)
	{
		for (Home h : homeList.values())
		{
			if (IsPointInHome(h, x, y, z))
				return true;
		}
		return false;
	}
	
	public static Home getPointInHome(int x, int y, int z)
	{
		for (Home h : homeList.values())
		{
			if (IsPointInHome(h, x, y, z))
				return h;
		}
		return null;
	}
	
	public static List<Home> getHomesStartWith(String compared, int maxAmount, int pageNumber)
	{
		int control = (maxAmount * pageNumber) + maxAmount;
		int i = 0;
		List<Home> toReturn = Lists.newArrayList();
		for (Home h : homeList.values())
		{
			if (h.ownerUsername.toLowerCase().startsWith(compared.toLowerCase()))
			{ 
				i++;
				
				if (control >= i)
					toReturn.add(h);
			}

			if (maxAmount <= toReturn.size())
				return toReturn;
		}
		return toReturn;
	}
	
	public static boolean hasHome(String username)
	{
		return homeList.containsKey(username);
	}
	
	public static void startEndRaid(String par1Player, boolean startRaid)
	{
		Home h = HomeManager.getHomeByPlayerName(par1Player);
		TileEntityHomeHall teHome = null;
		
		if (h != null)
		{
			for (WorldServer ws : MinecraftServer.getServer().worldServers)
	    	{
				if (ws.provider.dimensionId == h.dimensionId)
				{
					teHome = (TileEntityHomeHall) ws.getBlockTileEntity(h.xCoord, h.yCoord, h.zCoord);
					break;
				}
	    	}
			
			if (teHome != null)
			{
				List<TileEntityScourgeResource> list = teHome.getAllScourgeResourceBuildings();
				for (TileEntityScourgeResource teResource : list)
				{
					if (startRaid)
						teResource.startRaid();
					else
						teResource.endRaid();
				}
			}
		}
		else
			FMLLog.log(Level.SEVERE, "User %s is starting a raid but server doesn't know about his home!", par1Player);
	}
}
