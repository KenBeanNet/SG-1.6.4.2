package mods.scourgecraft.client.gui;

import mods.scourgecraft.inventory.ContainerHomeBank;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class GuiHomeBank extends GuiContainer
{
    public GuiHomeBank(InventoryPlayer var1, TileEntity var2)
    {
        super(new ContainerHomeBank(var1, var2));
    }

    protected void drawGuiContainerForegroundLayer() {}

    /**
* Draw the background layer for the GuiContainer (everything behind the items)
*/
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        drawCenteredString(this.fontRenderer, "Bank", this.width / 2, 10, 0x6699FF);
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiResourceFile.guiHomeBank);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}