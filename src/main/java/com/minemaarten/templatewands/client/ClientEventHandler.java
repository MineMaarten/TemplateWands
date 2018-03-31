package com.minemaarten.templatewands.client;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

import com.minemaarten.templatewands.items.IAABBRenderer;
import com.minemaarten.templatewands.items.IAABBRenderer.ColoredAABB;
import com.minemaarten.templatewands.lib.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID, value = {Side.CLIENT})
public class ClientEventHandler{

    public static final ClientEventHandler INSTANCE = new ClientEventHandler();
    public static final AABBRenderer aabbRenderer = new AABBRenderer();

    private ClientEventHandler(){

    }

    @SubscribeEvent
    public static void onWorldRender(RenderWorldLastEvent event){
        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!(heldItem.getItem() instanceof IAABBRenderer)) return;

        Set<ColoredAABB> aabbs = ((IAABBRenderer)heldItem.getItem()).getAABBs(heldItem);

        double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks();
        double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
        double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks();
        GL11.glPushMatrix();
        GL11.glTranslated(-playerX, -playerY, -playerZ);

        GL11.glPointSize(10);

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        b.setTranslation(0, 0, 0);

        aabbRenderer.render(t, aabbs);

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }
}
