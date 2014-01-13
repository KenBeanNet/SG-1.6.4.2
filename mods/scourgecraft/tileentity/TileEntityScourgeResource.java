package mods.scourgecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityScourgeResource extends TileEntityScourgeBuilding {


	public static final String NBT_RESOURCE_COUNT = "ResourceCount";
	protected double goldCount;
	

	public TileEntityScourgeResource(int par1BuildTime) {
		super(par1BuildTime);
		// TODO Auto-generated constructor stub
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		goldCount = nbt.getInteger(NBT_RESOURCE_COUNT);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger(NBT_RESOURCE_COUNT, (int)goldCount);
	}

	public int getGold()
	{
		return (int)goldCount;
	}
	
	public void setGold(int amount)
	{
		goldCount = amount;
	}
	
	public double getMaxStorage(int par1Level)
	{
		return 0.00;
	}
	
	public double getRate(int par1Level)
	{		
		return 0.00;
	}
	
	public double getRate()
	{
		return getRate(level);
	}
	
	public double getMaxStorage()
	{
		return getMaxStorage(level);
	}
	
}
