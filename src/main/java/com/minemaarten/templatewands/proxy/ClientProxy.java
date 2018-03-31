package com.minemaarten.templatewands.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.minemaarten.templatewands.init.ModItems;
import com.minemaarten.templatewands.lib.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID, value = {Side.CLIENT})
public class ClientProxy extends CommonProxy{

    @SubscribeEvent
    public static void onModelRegistration(ModelRegistryEvent event){
        ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation("templatewands:template_wand"), "inventory");
        ModelLoader.setCustomModelResourceLocation(ModItems.TEMPLATE_WAND, 0, location);
    }

    @Override
    public void addScheduledTask(Runnable runnable, boolean serverSide){
        if(serverSide) {
            super.addScheduledTask(runnable, serverSide);
        } else {
            Minecraft.getMinecraft().addScheduledTask(runnable);
        }
    }

    @Override
    public EntityPlayer getPlayer(){
        return FMLClientHandler.instance().getClientPlayerEntity();
    }
}
