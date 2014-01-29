package mods.scourgecraft.tileentity;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLLog;
import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.tileentity.TileEntityScourgeBuilding.BuildingTask;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileEntityHomeHall extends TileEntityScourgeBuilding
{
	public static final String HOME_NAME = "HomeName";
	private String homeName = "";
	
	private Ticket chunkTicket;
	
	public TileEntityHomeHall()
	{
		super(500);
	}
	
	@Override
	public void updateEntity()
	{
		if (sentBuildCommands)
		{
			if (buildingTasks.size() > 0)
			{
				if (timeLeft % delay == 0)
				{
					BuildingTask task = buildingTasks.remove(0);
					worldObj.setBlock(task.xCoord, task.yCoord, task.zCoord, task.blockId);
				}
			}
			if (timeLeft > 0)
			{
				timeLeft -= 1;
				if (timeLeft % 60 == 0 && timeLeft != 0) // We don't want to run at 0, because we want the done sound to play.
					worldObj.playSoundEffect((double)((float)xCoord + 0.5F), (double)((float)yCoord + 0.5F), (double)((float)zCoord + 0.5F), ScourgeCraftCore.modid.toLowerCase() + ":" + "construction", 2.0f, 2.0f);
				else if (timeLeft == 0)
					worldObj.playSoundEffect((double)((float)xCoord + 0.5F), (double)((float)yCoord + 0.5F), (double)((float)zCoord + 0.5F), ScourgeCraftCore.modid.toLowerCase() + ":" + "constructiondone", 2.0f, 2.0f);
			}
			else 
			{
				timeLeft = 0;
				for (BuildingTask t : buildingTasks)
				{
					worldObj.setBlock(t.xCoord, t.yCoord, t.zCoord, t.blockId);
				}
				buildingTasks.clear();
			}
		}
	}
	
	@Override
	public boolean isCompleted()
    {
		return timeLeft == 0 || !sentBuildCommands;
    }
	
	public int getBuildingCount(int blockId)
	{
		int widthOfHome = HomeManager.getHomeSize(level);
		int totalCount = 0;
		for (int x = xCoord - widthOfHome; x <= xCoord + widthOfHome; x++)
		{
			for (int y = yCoord - widthOfHome; y <= yCoord + widthOfHome; y++)
			{
				for (int z = zCoord - widthOfHome; z <= zCoord + widthOfHome; z++)
				{
					if (worldObj.getBlockId(x, y, z) == blockId)
						totalCount++;
				}
			}
		}
		return totalCount;
	}
	
	public int getTotalResourceCount(int blockId)
	{
		int widthOfHome = HomeManager.getHomeSize(level);
		int totalCount = 0;
		for (int x = xCoord - widthOfHome; x <= xCoord + widthOfHome; x++)
		{
			for (int y = yCoord - widthOfHome; y <= yCoord + widthOfHome; y++)
			{
				for (int z = zCoord - widthOfHome; z <= zCoord + widthOfHome; z++)
				{
					if (worldObj.getBlockId(x, y, z) == blockId)
					{
						TileEntity te = worldObj.getBlockTileEntity(x, y, z);
						if (te instanceof TileEntityScourgeResource)
						{
							TileEntityScourgeResource teResource = (TileEntityScourgeResource)te;
							totalCount += teResource.getGold();
						}
					}
				}
			}
		}
		return totalCount;
	}
	
	//This should only be called from the TE of the HomeBlock
	public List<TileEntityScourgeBuilding> getBuildingsByBlock(int blockId)
	{
		List<TileEntityScourgeBuilding> toReturn = Lists.newArrayList();
		int widthOfHome = HomeManager.getHomeSize(level);
		for (int x = xCoord - widthOfHome; x <= xCoord + widthOfHome; x++)
		{
			for (int y = yCoord - widthOfHome; y <= yCoord + widthOfHome; y++)
			{
				for (int z = zCoord - widthOfHome; z <= zCoord + widthOfHome; z++)
				{
					if (worldObj.getBlockId(x, y, z) == blockId)
						toReturn.add((TileEntityScourgeBuilding)worldObj.getBlockTileEntity(x, y, z));
				}
			}
		}
		return toReturn;
	}
	
	//This should only be called from the TE of the HomeBlock
	public List<TileEntityScourgeResource> getAllScourgeResourceBuildings()
	{
		List<TileEntityScourgeResource> toReturn = Lists.newArrayList();
		int widthOfHome = HomeManager.getHomeSize(level);
		for (int x = xCoord - widthOfHome; x <= xCoord + widthOfHome; x++)
		{
			for (int y = yCoord - widthOfHome; y <= yCoord + widthOfHome; y++)
			{
				for (int z = zCoord - widthOfHome; z <= zCoord + widthOfHome; z++)
				{
					TileEntity te = worldObj.getBlockTileEntity(x, y , z);
					if (te instanceof TileEntityScourgeResource)
						toReturn.add((TileEntityScourgeResource)te);
				}
			}
		}
		return toReturn;
	}
	
	public void build()
	{
		if (!sentBuildCommands)
		{
			int widthOfHome = HomeManager.getHomeSize(1);
			sentBuildCommands = true;
			for (int x = this.xCoord - widthOfHome; x <= xCoord + widthOfHome; x++)
			{
				for (int z = zCoord - widthOfHome; z <= zCoord + widthOfHome; z++)
				{
					addBuildingTask(new BuildingTask(x, yCoord - 2, z, Block.bedrock.blockID));
					if (x == xCoord - widthOfHome)
						addBuildingTask(new BuildingTask(x, yCoord - 1, z, Block.stone.blockID));
					if (x == xCoord + widthOfHome)
						addBuildingTask(new BuildingTask(x, yCoord - 1, z, Block.stone.blockID));
				}
				addBuildingTask(new BuildingTask(x, yCoord - 1, zCoord + widthOfHome, Block.stone.blockID));
				addBuildingTask(new BuildingTask(x, yCoord - 1, zCoord - widthOfHome, Block.stone.blockID));
			}
			
			super.build(); //Always call Super.Build at end  This way if its alrdy completed, we clear the list. Avoiding server load.
		}
	}
	
	protected void addBuildingTask(BuildingTask task)
	{
		buildingTasks.add(task);
	}
	
	
	@Override
	public boolean hasNextLevel()
	{
		return TileEntityGoldStorage.getTotalMaxByTownLevel(level + 1) != 0 && TileEntityGoldProducer.getTotalMaxByTownLevel(level + 1) != 0
				&& upgradeTime(level + 1) != 0;
	}
	
	@Override
	public int upgradeTime(int par1Level)
	{
		switch (par1Level)
		{
			case 2:
				return 3000;
		}
		return 0;
	}

	@Override
	public boolean canUpdate()
	{
		if (isCompleted() && sentBuildCommands)
			return false;
		return true;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (nbt.hasKey(HOME_NAME))
			homeName = nbt.getString(HOME_NAME);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if (!homeName.isEmpty())
			nbt.setString(HOME_NAME, homeName);
	}
	
	public void setHomeName(String par1Name)
	{
		homeName = par1Name;
	}

	public String getHomeName() {
		return homeName;
	}
	
	@Override
    public void validate() {
            super.validate();
            if(!worldObj.isRemote && chunkTicket == null && !this.getOwner().isEmpty()) {
                    Ticket ticket = ForgeChunkManager.requestTicket(ScourgeCraftCore.instance, worldObj, Type.NORMAL);
                    if(ticket != null) {
                            forceChunkLoading(ticket);
                    }
            }
    }

    @Override
    public void invalidate() {
            super.invalidate();
            stopChunkLoading();
    }
    
    public void forceChunkLoading(Ticket ticket) {
        stopChunkLoading();
        chunkTicket = ticket;
        for(ChunkCoordIntPair coord : getLoadArea()) {
        	FMLLog.log(Level.INFO, String.format("Force loading chunk %s in %s", coord, worldObj.provider.getClass()));
                ForgeChunkManager.forceChunk(chunkTicket, coord);
        }
	}
	
	public void unforceChunkLoading() {
	        for(Object obj : chunkTicket.getChunkList()) {
	                ChunkCoordIntPair coord = (ChunkCoordIntPair) obj;
	                ForgeChunkManager.unforceChunk(chunkTicket, coord);
	        }
	}
	
	public void stopChunkLoading() {
	        if(chunkTicket != null) {
	                ForgeChunkManager.releaseTicket(chunkTicket);
	                chunkTicket = null;
	        }
	}
	
	public List<ChunkCoordIntPair> getLoadArea() {
        List<ChunkCoordIntPair> loadArea = new LinkedList<ChunkCoordIntPair>();

        for(int x = -1; x < 1 + 1; x++) {
                for(int z = -1; z < 1 + 1; z++) {
                        ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair((xCoord >> 4) + x, (zCoord >> 4) + z);

                        loadArea.add(chunkCoords);
                }
        }

        return loadArea;
	}

}