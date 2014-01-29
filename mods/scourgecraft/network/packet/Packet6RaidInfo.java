package mods.scourgecraft.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.PermissionManager;
import mods.scourgecraft.data.RaidManager;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.helpers.Raid;
import mods.scourgecraft.network.ClientProxy;
import mods.scourgecraft.network.ScourgePacket;
import mods.scourgecraft.player.ExtendedPlayer;

public class Packet6RaidInfo extends ScourgePacket {

	private Raid raid;
   
    public Packet6RaidInfo(Raid par1Raid) {
    		this.raid = par1Raid;
    }

    public Packet6RaidInfo() { } // Be sure to always have the default constructor in your class, or the reflection code will fail!

    @Override
    protected void write(ByteArrayDataOutput out) {
    		out.writeUTF(raid.attackerName);
    		out.writeUTF(raid.defenderName);
    		out.writeInt(raid.timeLeft);
    		out.writeShort(raid.roundType);
    		out.writeDouble(raid.goldStolen);
    		out.writeBoolean(raid.isEnded);
    }

    @Override
    protected void read(ByteArrayDataInput in) {
    	raid = new Raid();
    	raid.attackerName = in.readUTF();
    	raid.defenderName = in.readUTF();
    	raid.timeLeft = in.readInt();
    	raid.roundType = in.readShort();
    	raid.goldStolen = in.readDouble();
    	raid.isEnded = in.readBoolean();
    }

    @Override
    protected void execute(EntityPlayer player, Side side) {
            if (side.isClient()) {
            	if (raid.isEnded)
            	{
            		RaidManager.raidList.remove(raid.attackerName);
            		if (raid.defenderName.equals(player.username))
                		HomeManager.startEndRaid(player.username, false);
            		else if (raid.attackerName.equals(player.username))
            			HomeManager.distributeResource(player, raid.goldStolen);
            	}
            	else
            	{
            		if (!RaidManager.raidList.containsKey(raid.attackerName) &&
            				raid.defenderName.equals(player.username)) //Make sure its the first packet for this raid && the defender knows to display the correct new steal amount
            			HomeManager.startEndRaid(player.username, true);
            		RaidManager.raidList.put(raid.attackerName, raid);
            		
            	}
            } 
    }
}