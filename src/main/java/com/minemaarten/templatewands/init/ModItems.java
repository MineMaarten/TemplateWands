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
    public static class Names{
        public static class Short{
            public static final String WAND_IRON = "template_wand_iron";
            public static final String WAND_GOLD = "template_wand_gold";
            public static final String WAND_DIAMOND = "template_wand_diamond";
            public static final String WAND_CREATIVE = "template_wand_creative";
        }

        public static class Long{
            public static final String WAND_IRON = Constants.MOD_ID + ":" + Short.WAND_IRON;
            public static final String WAND_GOLD = Constants.MOD_ID + ":" + Short.WAND_GOLD;
            public static final String WAND_DIAMOND = Constants.MOD_ID + ":" + Short.WAND_DIAMOND;
            public static final String WAND_CREATIVE = Constants.MOD_ID + ":" + Short.WAND_CREATIVE;
        }
    }

    @ObjectHolder(Names.Long.WAND_IRON)
    public static final Item TEMPLATE_WAND_IRON = null;
    @ObjectHolder(Names.Long.WAND_GOLD)
    public static final Item TEMPLATE_WAND_GOLD = null;
    @ObjectHolder(Names.Long.WAND_DIAMOND)
    public static final Item TEMPLATE_WAND_DIAMOND = null;
    @ObjectHolder(Names.Long.WAND_CREATIVE)
    public static final Item TEMPLATE_WAND_CREATIVE = null;

    @SubscribeEvent
    public static void onItemsRegistered(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new ItemTemplateWand(3 * 3 * 3).setRegistryName(Names.Long.WAND_IRON).setUnlocalizedName(Names.Short.WAND_IRON));
        event.getRegistry().register(new ItemTemplateWand(7 * 7 * 7).setRegistryName(Names.Long.WAND_GOLD).setUnlocalizedName(Names.Short.WAND_GOLD));
        event.getRegistry().register(new ItemTemplateWand(11 * 11 * 11).setRegistryName(Names.Long.WAND_DIAMOND).setUnlocalizedName(Names.Short.WAND_DIAMOND));
        event.getRegistry().register(new ItemTemplateWand(Integer.MAX_VALUE).setRegistryName(Names.Long.WAND_CREATIVE).setUnlocalizedName(Names.Short.WAND_CREATIVE));
    }
}
