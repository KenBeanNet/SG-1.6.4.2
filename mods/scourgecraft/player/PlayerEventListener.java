package mods.scourgecraft.player;

import java.util.HashMap;

import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.RaidManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PlayerEventListener {
	public static int BASE_PLAYER_KILL = 100;
	public static int BASE_PLAYER_KILL_KILL_STREAK = 50;
	public static int BASE_NPC_KILL = 10;
	
	@ForgeSubscribe
    public void livingDies(LivingDeathEvent event)
    {
    }
	
	@ForgeSubscribe
	public void onEntityLivingDeath(LivingDeathEvent event)
	{
		if (event.source.getEntity() instanceof EntityPlayer)
		{
			ExtendedPlayer extAttacker = ExtendedPlayer.getExtendedPlayer((EntityPlayer)event.source.getEntity());
			
			if (event.entity instanceof EntityPlayer)
			{
				extAttacker.addKillStreak();
					
				extAttacker.addScore(BASE_PLAYER_KILL);
				((EntityPlayer)event.source.getEntity()).addChatMessage("[ScourgeCraft] You Killed " + ((EntityPlayer)event.entity).username + "! + " + BASE_PLAYER_KILL);

				if (extAttacker.getKillStreak() >= 3)
				{
					extAttacker.addScore(BASE_PLAYER_KILL_KILL_STREAK);
					((EntityPlayer)event.source.getEntity()).addChatMessage("[ScourgeCraft] Kill Streak! +" + BASE_PLAYER_KILL_KILL_STREAK);
				}
			}
			else if (event.entity instanceof EntityMob)
			{
				extAttacker.addScore(BASE_PLAYER_KILL);
				((EntityPlayer)event.source.getEntity()).addChatMessage("[ScourgeCraft] You Killed " + ((EntityMob)event.entity) + "! + " + BASE_NPC_KILL);
			}
		}
		
		if (event.entity instanceof EntityPlayer) // Handles the death of a EntityPlayer so they regain the data upon respawn.
		{
			ExtendedPlayer.saveProxyData((EntityPlayer) event.entity);
		}
	}
	
	@ForgeSubscribe
	public void entityAttacked(LivingAttackEvent event)
	{
		Entity attackedEnt = event.entity;
		DamageSource attackSource = event.source;
		
	}
	

	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
			ExtendedPlayer.loadProxyData((EntityPlayer) event.entity);
	}
	
	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			if (event.entity.getExtendedProperties(ExtendedPlayer.EXT_PLAYER) == null)
			{
				System.out.println("[ScourgeCraft] Registering extended properties.");
				ExtendedPlayer.register((EntityPlayer)event.entity);
				if (event.entity.getExtendedProperties(ExtendedPlayer.EXT_PLAYER) != null)
				{
					System.out.println("[ScourgeCraft] Extended properties registered successfully.");
				}
			}
			else
			{
				System.out.println("[ScourgeCraft] Extended properties already exist.");
			}
		}
	}
}