package mods.scourgecraft.helpers;

import mods.scourgecraft.helpers.point.Point;
import mods.scourgecraft.helpers.point.WarpPoint;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class FunctionHelper 
{
	/**
     * instWarp a player to a point. Please use TeleportCenter!
     * @param player
     * @param p
     */
    public static void setPlayer(EntityPlayerMP player, WarpPoint p)
    {
    	if (player == null)
    		return;
    	
            if (player.dimension != p.dim)
            {
                    MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, p.dim);
            }
            player.playerNetServerHandler.setPlayerLocation(p.xd, p.yd, p.zd, p.yaw, p.pitch);
            player.prevPosX = player.posX = p.xd;
            player.prevPosY = player.posY = p.yd;
            player.prevPosZ = player.posZ = p.zd;
    }

    /**
     * instWarp a player to a point. Please use TeleportCenter!
     * @param player
     * @param world
     * @param p
     */
    public static void setPlayer(EntityPlayerMP player, Point point, World world)
    {
    	if (player == null)
    		return;
    	
            if (player.dimension != world.provider.dimensionId)
            {
                    MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, world.provider.dimensionId);
            }
            double x = point.x, y = point.y, z = point.z;
            x = x < 0 ? x - 0.5 : x + 0.5;
            z = z < 0 ? z - 0.5 : z + 0.5;
            player.playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
    }
    
    /**
     * Gets a nice string with only needed elements.
     * Max time is weeks
     * @param timeInSec
     * @return Time in string format
     */
    public static String parseTimeFromTicks(int timeInSec)
    {
    		timeInSec /= 20;
            String uptime = "";
            int weeks = timeInSec / (86400 * 7);
            int remainder = timeInSec % (86400 * 7);
            int days = remainder / 86400;
            remainder = timeInSec % 86400;
            int hours = remainder / 3600;
            remainder = timeInSec % 3600;
            int minutes = remainder / 60;
            int seconds = remainder % 60;

            if (weeks != 0)
            {
                    uptime += weeks + " weeks ";
            }

            if (days != 0)
            {
                    uptime += (days < 10 ? "0" : "") + days + " days ";
            }

            if (hours != 0)
            {
                    uptime += (hours < 10 ? "0" : "") + hours + " h ";
            }

            if (minutes != 0)
            {
                    uptime += (minutes < 10 ? "0" : "") + minutes + " min ";
            }

            uptime += (seconds < 10 ? "0" : "") + seconds + " sec.";

            return uptime;
    }
}
