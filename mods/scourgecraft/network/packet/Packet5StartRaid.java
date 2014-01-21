package mods.scourgecraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import mods.scourgecraft.Home;
import mods.scourgecraft.Raid;
import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.PermissionManager;
import mods.scourgecraft.data.RaidManager;
import mods.scourgecraft.network.ScourgePacket;
import mods.scourgecraft.player.ExtendedPlayer;
import mods.scourgecraft.tileentity.TileEntityHomeHall;

public class Packet5StartRaid extends ScourgePacket {

	private String attacker;
	private String defender;
   
    public Packet5StartRaid(String attack, String defend) {
    		this.attacker = attack;
    		this.defender = defend;
    }

    public Packet5StartRaid() { } // Be sure to always have the default constructor in your class, or the reflection code will fail!

    @Override
    protected void write(ByteArrayDataOutput out) {
    		out.writeUTF(attacker);
    		out.writeUTF(defender);
    }

    @Override
    protected void read(ByteArrayDataInput in) {
    	attacker = in.readUTF();
    	defender = in.readUTF();
    }

    @Override
    protected void execute(EntityPlayer player, Side side) {
            if (side.isServer()) {
            	EntityPlayer defend = null;
            	for (WorldServer ws : MinecraftServer.getServer().worldServers)
            	{
            		defend = ws.getPlayerEntityByName(defender);
            		if (defend != null)
            			break;
            	}
            	if (defend != null)
            	{
            		if (RaidManager.canRaid(player, defend))
            		{
                		Raid r = RaidManager.startRaid(player, defend);
                		PacketDispatcher.sendPacketToAllPlayers(new Packet6RaidInfo(r).makePacket());
            		}
            	}
            	else
            		player.addChatMessage("[ScourgeCraft] Player " + defender + " must be online to start a raid");
            } 
    }
}