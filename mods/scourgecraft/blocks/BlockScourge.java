package mods.scourgecraft.blocks;

import mods.scourgecraft.tileentity.TileEntityScourgeBuilding;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockScourge extends BlockContainer 
{

	protected BlockScourge(int par1, Material par2Material) {
		super(par1, par2Material);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	* Called when the block is placed in the world.
	*/
	@Override
	    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	    {
	        byte var7 = 0;
	        int var8 = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

	        if (var8 == 0)
	        {
	            var7 = 2;
	        }

	        if (var8 == 1)
	        {
	            var7 = 5;
	        }

	        if (var8 == 2)
	        {
	            var7 = 3;
	        }

	        if (var8 == 3)
	        {
	            var7 = 4;
	        }

	        TileEntity var10 = par1World.getBlockTileEntity(par2, par3, par4);
	        
	        if (var10 instanceof TileEntityScourgeBuilding)
	        	((TileEntityScourgeBuilding)var10).setDirection(var7);
	    }

}
