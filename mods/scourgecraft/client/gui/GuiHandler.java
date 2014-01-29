package mods.scourgecraft.client.gui;

import java.util.HashMap;
import java.util.Map;

import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.inventory.ContainerDefault;
import mods.scourgecraft.inventory.ContainerHomeBank;
import mods.scourgecraft.tileentity.TileEntityGoldProducer;
import mods.scourgecraft.tileentity.TileEntityGoldStorage;
import mods.scourgecraft.tileentity.TileEntityHomeHall;
import mods.scourgecraft.tileentity.TileEntityRaidCenter;
import mods.scourgecraft.tileentity.TileEntityScourgeBuilding;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler
{
	public GuiHandler()
	{
		NetworkRegistry.instance().registerGuiHandler(ScourgeCraftCore.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		switch (ID)
		{
			default: return null;
			case 0: return new ContainerDefault();
			case 1: return new ContainerDefault();
			case 2: return new ContainerDefault();
			case 3: return new ContainerDefault();
			case 4: return new ContainerDefault();
			case 5: return new ContainerDefault();
			case 6: return new ContainerHomeBank(player.inventory, world.getBlockTileEntity(x, y, z));
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		switch (ID)
		{
			default: return null;
			case 0: return new GuiHomeHall(player, (TileEntityHomeHall)world.getBlockTileEntity(x, y, z), x, y, z);
			case 1: return new GuiBuildingInProgress((TileEntityScourgeBuilding)world.getBlockTileEntity(x, y, z), x, y, z);
			case 2: return new GuiGoldProducer((TileEntityGoldProducer)world.getBlockTileEntity(x, y, z), x, y, z);
			case 3: return new GuiGoldStorage((TileEntityGoldStorage)world.getBlockTileEntity(x, y, z), x, y, z);
			case 4: return new GuiRaidCenter(player, (TileEntityRaidCenter)world.getBlockTileEntity(x, y, z), x, y, z);
			case 5: return new GuiShowRaids(player);
			case 6: return new GuiHomeBank(player.inventory, world.getBlockTileEntity(x, y, z));
		}
	}
 
}
