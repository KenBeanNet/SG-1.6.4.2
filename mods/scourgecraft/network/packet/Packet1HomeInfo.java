package mods.scourgecraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.data.PermissionManager;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.helpers.Raid;
import mods.scourgecraft.network.ClientProxy;
import mods.scourgecraft.network.ScourgePacket;
import mods.scourgecraft.player.ExtendedPlayer;

public class Packet1HomeInfo extends ScourgePacket {

	private Home home;
   
    public Packet1HomeInfo(Home par1Home) {

    		this.home = par1Home;
    }

    public Packet1HomeInfo() { } // Be sure to always have the default constructor in your class, or the reflection code will fail!

    @Override
    protected void write(ByteArrayDataOutput out) {
    		out.writeUTF(home.name);
    		out.writeUTF(home.ownerUsername);
    		out.writeInt(home.level);
    		out.writeInt(home.xCoord);
    		out.writeInt(home.yCoord);
    		out.writeInt(home.zCoord);
    		out.writeInt(home.dimensionId);
    		out.writeLong(home.shieldTimeStamp);
    		out.writeInt(home.raidList.size());
    		for (int i = 0; i < home.raidList.size(); i++)
        	{
        		out.writeUTF(home.raidList.get(i).attackerName);
        		out.writeUTF(home.raidList.get(i).defenderName);
        		out.writeDouble(home.raidList.get(i).goldStolen);
        		out.writeLong(home.raidList.get(i).startDateTime);
        		out.writeLong(home.raidList.get(i).endDateTime);
        	}
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
    	home.dimensionId = in.readInt();
    	home.shieldTimeStamp = in.readLong();
    	int size = in.readInt();
    	for (int i = 0; i < size; i++)
    	{
    		Raid r = new Raid();
    		r.attackerName = in.readUTF();
    		r.defenderName = in.readUTF();
    		r.goldStolen = in.readDouble();
    		r.startDateTime = in.readLong();
    		r.endDateTime = in.readLong();
    		home.raidList.add(r);
    	}
    }

    @Override
    protected void execute(EntityPlayer player, Side side) {
            if (side.isClient()) {
            	HomeManager.homeList.put(home.ownerUsername, home);
            } 
    }
}