package mods.scourgecraft.blocks;

import java.util.Random;

import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.PermissionManager;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.tileentity.TileEntityGoldStorage;
import mods.scourgecraft.tileentity.TileEntityHomeHall;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockGoldStorage extends BlockScourgeResource {

	public BlockGoldStorage(int par1, Material par2Material) {
		super(par1, par2Material);
		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return new TileEntityGoldStorage();
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);
		
		TileEntityGoldStorage teGold = (TileEntityGoldStorage)par1World.getBlockTileEntity(par2, par3, par4);
		if (par5EntityLivingBase instanceof EntityPlayer)
			teGold.setOwner(((EntityPlayer)par5EntityLivingBase).username);
		
		if (par5EntityLivingBase instanceof EntityPlayer)
		{
	    	  Home home = HomeManager.getHomeByPlayerName(((EntityPlayer)par5EntityLivingBase).username);
	    	  if (home != null)
	    	  {
	    		  TileEntityHomeHall teHome = (TileEntityHomeHall)par1World.getBlockTileEntity(home.xCoord, home.yCoord, home.zCoord);
	    		  if (teHome != null)
	    		  {
	    			  if (teHome.getBuildingCount(this.blockID) > TileEntityGoldStorage.getTotalMaxByTownLevel(teHome.getLevel()))
	    			  {
	    				  par1World.setBlockToAir(par2, par3, par4);
	    				  if (!par5EntityLivingBase.worldObj.isRemote)
	    					  ((EntityPlayer)par5EntityLivingBase).addChatMessage("You are at the max amount of Gold Storage for your town.");
	    				  ((EntityPlayer) par5EntityLivingBase).inventory.addItemStackToInventory(new ItemStack(this)); //Give the item back to the player since it consumes it *TODO Fine a better way?
	    			  }
	    		  }
	    	  }
		}
	}
	
	/**
	* Called upon block activation (right click on the block.)
	*/
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
	    if (par1World.isRemote)
	    {
	        return true;
	    }
	    else
	    {
	    	TileEntityGoldStorage teHome = (TileEntityGoldStorage)par1World.getBlockTileEntity(par2, par3, par4);
	    	
	    	if (teHome.isCompleted())
	    	{
	    		par5EntityPlayer.openGui(ScourgeCraftCore.instance, 3, par1World, par2, par3, par4);
	    	}
	    	else
	    		par5EntityPlayer.openGui(ScourgeCraftCore.instance, 1, par1World, par2, par3, par4);
	    	
	        return true;
	    }
	}

}
