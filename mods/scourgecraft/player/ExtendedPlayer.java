package mods.scourgecraft.player;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.PermissionManager;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.helpers.Raid;
import mods.scourgecraft.network.CommonProxy;
import mods.scourgecraft.network.packet.Packet1HomeInfo;
import mods.scourgecraft.network.packet.Packet3ExtendedInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties
{
	public static final String EXT_PLAYER = "SCExtended";
	
	// I always include the entity to which the properties belong for easy access
	// It's final because we won't be changing which player it is
	private final EntityPlayer player;
	
	private int vitality;
	private int score;
	private int killStreak;
	

	public ExtendedPlayer(EntityPlayer player)
	{
		this.player = player;
	}

	public static ExtendedPlayer getExtendedPlayer(EntityPlayer player)
	{
		return (ExtendedPlayer) player.getExtendedProperties(EXT_PLAYER);
	}
	
	public final boolean consumeVitality(int amount)
	{
		// This variable makes it easier to write the rest of the method
		int vit = vitality;

		// These two lines are the same as before
		boolean sufficient = amount <= vit;
		vit -= (amount < vit ? amount : vit);

		vitality = vit;
		
		update();

		return sufficient;
	}

	public final void addVitality(int amount)
	{
		vitality += amount;
		update();
	}
	
	public final int getVitality()
	{
		return vitality;
	}
	
	public void addScore(int amount)
	{
		score += amount;
		if (score >= 1000)
		{
			score -= 1000;
			addVitality(1);
		}
	}
	
	public void addKillStreak()
	{
		killStreak++;
	}
	public int getKillStreak()
	{
		return killStreak;
	}
	public void setKillStreak(int par1KillStreak)
	{
		killStreak = par1KillStreak;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setScore(int par1Score)
	{
		score = par1Score;
	}
	
	public final void setVitality(int vit)
	{
		vitality = vit;
	}
	
	private void update() // Only to be called on Add or Remove, not during gets/sets.
	{
		PacketDispatcher.sendPacketToPlayer(new Packet3ExtendedInfo(ExtendedPlayer.getExtendedPlayer(player)).makePacket(), (Player)player);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("Vitality", this.vitality);
		compound.setInteger("Score", this.score);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.vitality = (compound.getInteger("Vitality"));
		this.score = (compound.getInteger("Score"));
	}

	@Override
	public void init(Entity entity, World world) {
		System.out.println("[ScourgeCraft] Initializing extended properties.");
		
	}
	
	public static void playerLogin(EntityPlayer player)
	{
		PacketDispatcher.sendPacketToPlayer(new Packet3ExtendedInfo(ExtendedPlayer.getExtendedPlayer(player)).makePacket(), (Player)player);
	}
	

	public static void saveProxyData(EntityPlayer player) 
	{
		ExtendedPlayer playerData = ExtendedPlayer.getExtendedPlayer(player);

		CommonProxy.storeEntityData(player.username, playerData);
	}
	
	public static void loadProxyData(EntityPlayer player) 
	{
		ExtendedPlayer savedData = CommonProxy.getEntityData(player.username);
		
		if(savedData != null) {
			player.registerExtendedProperties(ExtendedPlayer.EXT_PLAYER, savedData);
		}
	}
	
	public static void register(EntityPlayer player)
	{
		player.registerExtendedProperties(ExtendedPlayer.EXT_PLAYER, new ExtendedPlayer(player));
	}

}