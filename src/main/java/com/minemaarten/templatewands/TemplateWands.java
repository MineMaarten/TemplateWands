package com.minemaarten.templatewands;

import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.minemaarten.templatewands.capabilities.CapabilityTemplateWand;
import com.minemaarten.templatewands.capabilities.DefaultCapabilityStorage;
import com.minemaarten.templatewands.items.ItemTemplateWand;
import com.minemaarten.templatewands.lib.Constants;
import com.minemaarten.templatewands.proxy.CommonProxy;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MODVERSION, dependencies = "required-after:forge@[14.23.0.2551,)", acceptedMinecraftVersions = "1.12")
@EventBusSubscriber(modid = Constants.MOD_ID)
public class TemplateWands{

    @SidedProxy(clientSide = "com.minemaarten.templatewands.proxy.ClientProxy", serverSide = "com.minemaarten.templatewands.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(Constants.MOD_ID)
    public static TemplateWands instance;

    @SubscribeEvent
    public static void onItemsRegistered(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new ItemTemplateWand());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        CapabilityManager.INSTANCE.register(CapabilityTemplateWand.class, new DefaultCapabilityStorage<>(), CapabilityTemplateWand::new);
    }
}
