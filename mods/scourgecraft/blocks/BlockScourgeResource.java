package mods.scourgecraft.blocks;

import java.text.DecimalFormat;
import java.util.Random;

import mods.scourgecraft.data.RaidManager;
import mods.scourgecraft.helpers.Raid;
import mods.scourgecraft.tileentity.TileEntityGoldProducer;
import mods.scourgecraft.tileentity.TileEntityScourgeResource;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockScourgeResource  extends BlockScourge {

	protected BlockScourgeResource(int par1, Material par2Material) {
		super(par1, par2Material);

		setBlockUnbreakable();
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) 
	{
		TileEntityScourgeResource teGold = (TileEntityScourgeResource)par1World.getBlockTileEntity(par2, par3, par4);
		
		Raid r = RaidManager.getRaidByUsername(par5EntityPlayer.username);
		
		if (teGold != null && r != null)
		{
			if (r.defenderName.equals(teGold.getOwner())) 
			{
				r.goldStolen += teGold.steal(0.20); //TODO Change this percentage by what item the attacker has?
				if(!par1World.isRemote)
				{
					par1World.markBlockForUpdate(par2, par3, par4);
					DecimalFormat df = new DecimalFormat("#.##");
					par5EntityPlayer.addChatMessage("[ScourgeCraft] You have stolen " + df.format(r.goldStolen) + " gold.");
				}
			}
		}
	}
	

	
	//You don't want the normal render type, or it wont render properly.
    @Override
    public int getRenderType() 
    {
            return -1;
    }
    
    //It's not an opaque cube, so you need this.
    @Override
    public boolean isOpaqueCube() {
            return false;
    }
   
    //It's not a normal block, so you need this too.
    public boolean renderAsNormalBlock() {
            return false;
    }
	
	@Override
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion)
	{
		return false;
	}
	
	public int getMobilityFlag()
    {
		return 2; //Do not allow to be moved by Pistons or such.
    }

}
