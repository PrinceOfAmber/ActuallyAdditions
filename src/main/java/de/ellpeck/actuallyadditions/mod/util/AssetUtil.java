/*
 * This file ("AssetUtil.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.util;

import de.ellpeck.actuallyadditions.mod.misc.ParticleColored;
import de.ellpeck.actuallyadditions.mod.network.PacketHandler;
import de.ellpeck.actuallyadditions.mod.network.PacketServerToClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public final class AssetUtil{

    public static final ResourceLocation GUI_INVENTORY_LOCATION = getGuiLocation("guiInventory");

    public static ResourceLocation getGuiLocation(String file){
        return new ResourceLocation(ModUtil.MOD_ID, "textures/gui/"+file+".png");
    }

    public static ResourceLocation getBookletGuiLocation(String file){
        return new ResourceLocation(ModUtil.MOD_ID, "textures/gui/booklet/"+file+".png");
    }

    @SideOnly(Side.CLIENT)
    public static void displayNameString(FontRenderer font, int xSize, int yPositionOfMachineText, String text){
        font.drawString(text, xSize/2-font.getStringWidth(text)/2, yPositionOfMachineText, StringUtil.DECIMAL_COLOR_WHITE);
    }

    @SideOnly(Side.CLIENT)
    public static void displayNameString(FontRenderer font, int xSize, int yPositionOfMachineText, TileEntity tile){
        displayNameString(font, xSize, yPositionOfMachineText, tile.getDisplayName().getFormattedText());
    }

    @SideOnly(Side.CLIENT)
    public static void renderBlockInWorld(Block block, int meta){
        renderItemInWorld(new ItemStack(block, 1, meta));
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemInWorld(ItemStack stack){
        if(stack != null && stack.getItem() != null){
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    @SideOnly(Side.CLIENT)
    public static void renderStackToGui(ItemStack stack, int x, int y, float scale){
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);

        Minecraft mc = Minecraft.getMinecraft();
        boolean flagBefore = mc.fontRendererObj.getUnicodeFlag();
        mc.fontRendererObj.setUnicodeFlag(false);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);
        mc.fontRendererObj.setUnicodeFlag(flagBefore);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    //Copied from Gui.class and changed
    @SideOnly(Side.CLIENT)
    public static void drawHorizontalGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, float zLevel){
        float f = (float)(startColor >> 24 & 255)/255.0F;
        float f1 = (float)(startColor >> 16 & 255)/255.0F;
        float f2 = (float)(startColor >> 8 & 255)/255.0F;
        float f3 = (float)(startColor & 255)/255.0F;
        float f4 = (float)(endColor >> 24 & 255)/255.0F;
        float f5 = (float)(endColor >> 16 & 255)/255.0F;
        float f6 = (float)(endColor >> 8 & 255)/255.0F;
        float f7 = (float)(endColor & 255)/255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double)left, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        renderer.pos((double)left, (double)bottom, (double)zLevel).color(f1, f2, f3, f).endVertex();
        renderer.pos((double)right, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        renderer.pos((double)right, (double)top, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @SideOnly(Side.CLIENT)
    public static void renderNameTag(String tag, double x, double y, double z){
        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        float f = 1.6F;
        float f1 = 0.016666668F*f;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();
        int i = 0;
        int j = fontrenderer.getStringWidth(tag)/2;
        GlStateManager.disableTexture2D();
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double)(-j-1), (double)(-1+i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        renderer.pos((double)(-j-1), (double)(8+i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        renderer.pos((double)(j+1), (double)(8+i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        renderer.pos((double)(j+1), (double)(-1+i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(tag, -fontrenderer.getStringWidth(tag)/2, i, 553648127);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontrenderer.drawString(tag, -fontrenderer.getStringWidth(tag)/2, i, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void shootParticles(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, float[] color, int particleAmount, float particleSize, float ageMultiplier){
        if(!world.isRemote){
            NBTTagCompound data = new NBTTagCompound();
            data.setDouble("StartX", startX);
            data.setDouble("StartY", startY);
            data.setDouble("StartZ", startZ);
            data.setDouble("EndX", endX);
            data.setDouble("EndY", endY);
            data.setDouble("EndZ", endZ);
            data.setFloat("Color1", color[0]);
            data.setFloat("Color2", color[1]);
            data.setFloat("Color3", color[2]);
            data.setInteger("ParticleAmount", particleAmount);
            data.setFloat("ParticleSize", particleSize);
            data.setFloat("AgeMultiplier", ageMultiplier);
            PacketHandler.theNetwork.sendToAllAround(new PacketServerToClient(data, PacketHandler.PARTICLE_HANDLER), new NetworkRegistry.TargetPoint(world.provider.getDimension(), startX, startY, startZ, 96));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void renderParticlesFromAToB(double startX, double startY, double startZ, double endX, double endY, double endZ, int particleAmount, float particleSize, float[] color, float ageMultiplier){
        World world = Minecraft.getMinecraft().theWorld;

        if(Minecraft.getMinecraft().thePlayer.getDistance(startX, startY, startZ) <= 64 || Minecraft.getMinecraft().thePlayer.getDistance(endX, endY, endZ) <= 64){
            double difX = startX-endX;
            double difY = startY-endY;
            double difZ = startZ-endZ;
            double distance = new Vec3d(startX, startY, startZ).distanceTo(new Vec3d(endX, endY, endZ));

            for(int times = 0; times < Math.max(particleAmount/2, 1); times++){
                for(double i = 0; i <= 1; i += 1/(distance*particleAmount)){
                    ParticleColored fx = new ParticleColored(world, (difX*i)+endX+0.5, (difY*i)+endY+0.5, (difZ*i)+endZ+0.5, particleSize, color[0], color[1], color[2], ageMultiplier);
                    Minecraft.getMinecraft().effectRenderer.addEffect(fx);
                }
            }
        }
    }
}
