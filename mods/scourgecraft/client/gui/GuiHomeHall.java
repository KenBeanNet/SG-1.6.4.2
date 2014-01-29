package mods.scourgecraft.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.logging.Level;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import mods.scourgecraft.ScourgeCraftCore;
import mods.scourgecraft.data.HomeManager;
import mods.scourgecraft.helpers.Home;
import mods.scourgecraft.network.ClientProxy;
import mods.scourgecraft.network.packet.Packet2CreateHome;
import mods.scourgecraft.network.packet.Packet5StartRaid;
import mods.scourgecraft.player.ExtendedPlayer;
import mods.scourgecraft.tileentity.TileEntityGoldProducer;
import mods.scourgecraft.tileentity.TileEntityGoldStorage;
import mods.scourgecraft.tileentity.TileEntityHomeHall;
import mods.scourgecraft.tileentity.TileEntityScourgeBuilding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class GuiHomeHall extends GuiScreen {
	
	private EntityPlayer playerPar1;
	private ExtendedPlayer extPlayer;
	private GuiTextField txtTownName;
	private String townName;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private TileEntityHomeHall tileHome;
	
	private GuiButton btnUpgradeLevel;
	private GuiButton btnBuildHome;
	private GuiButton btnShowRaids;
	
	public GuiHomeHall(EntityPlayer player, TileEntityHomeHall tile, int x, int y, int z) {
		playerPar1 = player;
		xCoord = x;
		yCoord = y;
		zCoord = z;
		tileHome = tile;
	}

	@Override 
	public void initGui()
	{
		txtTownName = new GuiTextField(fontRenderer, 180 , this.height - 75, 100, 20);
		txtTownName.setMaxStringLength(16);
		extPlayer = ExtendedPlayer.getExtendedPlayer(playerPar1);
		
		btnUpgradeLevel = new GuiButton(1, this.width - 124, 60, 115, 20, "Upgrade Level " + (tileHome.getLevel() + 1));
		btnUpgradeLevel.drawButton = false;
		this.buttonList.add(btnUpgradeLevel);
		
		btnBuildHome = new GuiButton(0, 195, this.height - 50, 65, 20, "Build");
		btnBuildHome.drawButton = false;
		this.buttonList.add(btnBuildHome);
		
		btnShowRaids = new GuiButton(5, this.width - 124, 38, 115, 20, "Raids");
		btnShowRaids.drawButton = false;
		this.buttonList.add(btnShowRaids);
		this.buttonList.add(new GuiButton(10, this.width - 50, 5, 45, 20, "Close"));
	}
	
	@Override
	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		if (MyTownHall())
		{
			drawCenteredString(this.fontRenderer, "Welcome Home!", this.width / 2, 10, 0x6699FF);
			
			drawString(this.fontRenderer, "Home Stats", 10, 50, 0xFFCC00);
			drawString(this.fontRenderer, "Level " + tileHome.getLevel(), 10, 60, 0x66CC66);
			drawString(this.fontRenderer, "Gold Producers " + tileHome.getBuildingCount(ScourgeCraftCore.configBlocks.goldProducerID) + "/" + TileEntityGoldProducer.getTotalMaxByTownLevel(tileHome.getLevel()), 10, 70, 0x66CC66);
			drawString(this.fontRenderer, "Gold Storages " + tileHome.getBuildingCount(ScourgeCraftCore.configBlocks.goldStorageID) + "/" + TileEntityGoldStorage.getTotalMaxByTownLevel(tileHome.getLevel()), 10, 80, 0x66CC66);
			drawString(this.fontRenderer, "Total Gold " + tileHome.getTotalResourceCount(ScourgeCraftCore.configBlocks.goldStorageID), 10, 90, 0x66CC66);
			btnShowRaids.drawButton = true; 
			
			if (tileHome.hasNextLevel())
			{
				drawString(this.fontRenderer, "Next Level Stats", 10, 100, 0xFFCC00);
				drawString(this.fontRenderer, "Level " + (tileHome.getLevel() + 1), 10, 110, 0x66CC66);
				drawString(this.fontRenderer, "Max Gold Producers " + TileEntityGoldProducer.getTotalMaxByTownLevel(tileHome.getLevel() + 1), 10, 120, 0x66CC66);
				drawString(this.fontRenderer, "Max Gold Storages " + TileEntityGoldStorage.getTotalMaxByTownLevel(tileHome.getLevel() + 1), 10, 130, 0x66CC66);
				drawString(this.fontRenderer, "Upgrade Requirements", 10, 140, 0xFFCC00);
				btnUpgradeLevel.drawButton = true;
			}
		}
		else if (OtherPlayerTownHall())
		{
			drawCenteredString(this.fontRenderer, tileHome.getHomeName(), this.width / 2, 10, 0x6699FF);
			
			
		}
		else if (AlreadyHaveTownHall())
		{
			drawCenteredString(this.fontRenderer, "You already have a home!", this.width / 2, 10, 0xFF0000);
		}
		else 
		{
			drawCenteredString(this.fontRenderer, "Building a Home", this.width / 2, this.height / 25, 0x6699FF);
			
			drawString(this.fontRenderer, "How to Create a Home", 20, 40, 0xFF0033);
			
			drawString(this.fontRenderer, "Creating a home is simple.  First you need to name your town below.", 30, 50, 0xFFCC66);
			drawString(this.fontRenderer, "Then hit the create button.  Your home will start constructing!", 30, 60, 0xFFCC66);
			drawString(this.fontRenderer, "After it is fully built, you can upgrade your home to increase", 30, 70, 0xFFCC66);
			drawString(this.fontRenderer, "your total overall space.", 30, 80, 0xFFCC66);
			
			fontRenderer.drawString("Name Of your Home : ", 65, this.height - 69, 0xFFFFFF, false);
		
			txtTownName.drawTextBox();
			
			btnBuildHome.drawButton = true;
		}
		
		super.drawScreen(x, y, f);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	public void keyTyped(char c, int i)
	{
		super.keyTyped(c, i);
		txtTownName.textboxKeyTyped(c, i);
	}
	
	public void mouseClicked(int i, int j, int k)
	{
		super.mouseClicked(i, j, k);
		txtTownName.mouseClicked(i, j, k);
	}
	
	public void updateScreen()
	{
		townName = txtTownName.getText();
	}
	
	public void actionPerformed(GuiButton button)
	{
		if (button.id == 0)
		{
			Home h = HomeManager.getHomeByPlayerName(playerPar1.username);
			if (h == null)
			{
				if (townName.length() < 4)
				{
					playerPar1.addChatMessage("Your town must have more than 4 letters.");
					return;
				}
				
				Home home = new Home();
				home.level = 1;
				home.xCoord = xCoord;
				home.yCoord = yCoord;
				home.zCoord = zCoord;
				home.ownerUsername = playerPar1.username;
				home.name = townName;
			
				tileHome.build();
				PacketDispatcher.sendPacketToServer(new Packet2CreateHome(home).makePacket());
				
				playerPar1.openGui(ScourgeCraftCore.instance, 1, tileHome.worldObj, xCoord, yCoord, zCoord);
			}
		}
		else if (button.id == 5) //Show Raids
		{
			playerPar1.openGui(ScourgeCraftCore.instance, 5, tileHome.worldObj, xCoord, yCoord, zCoord);
		}
		else if (button.id == 10)
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
	}
	
	private boolean MyTownHall()
	{
		return AlreadyHaveTownHall() && tileHome != null && tileHome.getOwner().equals(playerPar1.username);
	}
	
	private boolean OtherPlayerTownHall()
	{
		return tileHome != null && !tileHome.getOwner().isEmpty();
	}
	
	private boolean AlreadyHaveTownHall()
	{
		return HomeManager.hasHome(playerPar1.username);
	}
}
