package mods.scourgecraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.scourgecraft.ScourgeCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends BlockContainer {

	public BlockWall(int par1, Material par2Material) {
		super(par1, par2Material);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	* When this method is called, your block should register all the icons it needs with the given IconRegister. This
	* * is the only chance you get to register icons.
	*/
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("planks_oak");
	}

	 @SideOnly(Side.CLIENT)

	    /**
	     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	     * coordinates.  Args: blockAccess, x, y, z, side
	     */
	    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	    {
	        return true;
	    }
	 


	    /**
	     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	     */
	    public boolean isOpaqueCube()
	    {
	        return false;
	    }

	    /**
	     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	     */
	    public boolean renderAsNormalBlock()
	    {
	        return false;
	    }

	    public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	    {
	        return false;
	    }

	    /**
	     * Returns true if the specified block can be connected by a fence
	     */
	    public boolean canConnectFenceTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	    {
	        int l = par1IBlockAccess.getBlockId(par2, par3, par4);

	        if (l != this.blockID && l != Block.fenceGate.blockID)
	        {
	            Block block = Block.blocksList[l];
	            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
	        }
	        else
	        {
	            return true;
	        }
	    }
}
