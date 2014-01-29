package mods.scourgecraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.PermissionManager;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.network.ScourgePacket;
import mods.scourgecraft.player.ExtendedPlayer;
import mods.scourgecraft.tileentity.TileEntityHomeHall;

public class Packet2CreateHome extends ScourgePacket {

	private Home home;
   
    public Packet2CreateHome(Home par1Home) {
    		this.home = par1Home;
    }

    public Packet2CreateHome() { } // Be sure to always have the default constructor in your class, or the reflection code will fail!

    @Override
    protected void write(ByteArrayDataOutput out) {
    		out.writeUTF(home.name);
    		out.writeUTF(home.ownerUsername);
    		out.writeInt(home.level);
    		out.writeInt(home.xCoord);
    		out.writeInt(home.yCoord);
    		out.writeInt(home.zCoord);
    }

    @Override
    protected void read(ByteArrayDataInput in) {
    	home = new Home();
    	home.name = in.readUTF();
    	home.ownerUsername = in.readUTF();
    	home.level = in.readInt();
    	home.xCoord = in.readInt();
    	home.yCoord = in.readInt();
    	home.zCoord = in.readInt();
    }

    @Override
    protected void execute(EntityPlayer player, Side side) {
            if (side.isServer()) {
            	if (!HomeManager.homeList.containsKey(home.ownerUsername)) //Ensure player doesn't already have home.  Hack?
            	{
            		home.dimensionId = player.worldObj.provider.dimensionId;
            		EntityPlayer remotePlayer = player.worldObj.getPlayerEntityByName(home.ownerUsername);
                    TileEntity te = remotePlayer.worldObj.getBlockTileEntity(home.xCoord, home.yCoord, home.zCoord);
                    if (te instanceof TileEntityHomeHall)
                    {
                    	HomeManager.createHome(home);
                    	TileEntityHomeHall teHome = (TileEntityHomeHall)te;
                    	teHome.setHomeName(home.name);
                    	teHome.setOwner(home.ownerUsername);
                    	player.worldObj.markBlockForUpdate(home.xCoord, home.yCoord, home.zCoord);
                    	teHome.build();

                    	PacketDispatcher.sendPacketToAllPlayers(new Packet1HomeInfo(home).makePacket());
                    }
                    else
                    	System.out.println("[ScourgeCraft] The coords of a Home Creation have Invalid TE : " + home.xCoord + " " + home.yCoord + " " + home.zCoord);
            	}
            	else
            		System.out.println("[ScourgeCraft] Player already has home and attemption 2nd creation!");
            } 
    }
}