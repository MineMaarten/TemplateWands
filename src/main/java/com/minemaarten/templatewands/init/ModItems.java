package com.minemaarten.templatewands.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import com.minemaarten.templatewands.items.ItemTemplateWand;
import com.minemaarten.templatewands.lib.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModItems{
    @ObjectHolder("templatewands:template_wand")
    public static final Item TEMPLATE_WAND = null;

    @SubscribeEvent
    public static void onItemsRegistered(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new ItemTemplateWand());
    }
}
