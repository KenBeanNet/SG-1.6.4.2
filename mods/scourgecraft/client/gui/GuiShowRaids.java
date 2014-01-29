package mods.scourgecraft.client.gui;

import java.util.Date;

import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.network.packet.Packet2CreateHome;
import mods.scourgecraft.player.ExtendedPlayer;
import mods.scourgecraft.tileentity.TileEntityGoldProducer;
import mods.scourgecraft.tileentity.TileEntityGoldStorage;
import mods.scourgecraft.tileentity.TileEntityHomeHall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiShowRaids extends GuiScreen {
	
	EntityPlayer player;
	Home home;
	
	private GuiButton btnUpgradeLevel;
	
	public GuiShowRaids(EntityPlayer par1Player) {
		player = par1Player;
		home = HomeManager.getHomeByPlayerName(par1Player.username);
	}

	@Override 
	public void initGui()
	{
		this.buttonList.add(new GuiButton(10, this.width - 50, 5, 45, 20, "Close"));
	}
	
	@Override
	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
		
		GL11.glColor4f(1F, 1F, 1F, 1F);

		if (home != null)
		{
			drawCenteredString(this.fontRenderer, "Recent Raids on " + home.name, this.width / 2, 10, 0x6699FF);
			drawCenteredString(this.fontRenderer, "Owned by " + home.ownerUsername, this.width / 2, 20, 0x6699FF);
			
			drawString(this.fontRenderer, "Attacker Name", 10, 50, 0xFFFFFF);
			drawString(this.fontRenderer, "Gold Stolen", 100, 50, 0xFFFFFF);
			drawString(this.fontRenderer, "End Time", 200, 50, 0xFFFFFF);
			for (int i = 0; i < home.raidList.size(); i++)
			{
				drawString(this.fontRenderer, home.raidList.get(i).attackerName, 10, (i * 20) + 70, 0xFFCC00);
				drawString(this.fontRenderer, home.raidList.get(i).goldStolen + "", 100, (i * 20) + 70, 0x66CC66);
				drawString(this.fontRenderer, new Date(home.raidList.get(i).endDateTime).toGMTString() + "", 200, (i * 20) + 70, 0x66CC66);
			}
		}
		
		super.drawScreen(x, y, f);
	}
	
	public void actionPerformed(GuiButton button)
	{
		if (button.id == 10)
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
	}
}
