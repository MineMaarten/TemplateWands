package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IEntityIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;
import com.minemaarten.templatewands.templates.ingredients.providers.blocks.ProviderInventoryContents;

@TemplateWands
public class ProviderEntitySpecials implements IEntityIngredientProvider{

    private final Map<Class<? extends Entity>, ItemStack[]> entityToStacks = new HashMap<>();

    public ProviderEntitySpecials(){
        ItemStack minecart = new ItemStack(Items.MINECART);
        register(EntityMinecartEmpty.class, minecart);
        register(EntityMinecartFurnace.class, minecart, new ItemStack(Blocks.FURNACE));
        register(EntityMinecartTNT.class, minecart, new ItemStack(Blocks.TNT));
        register(EntityMinecartChest.class, minecart, new ItemStack(Blocks.CHEST));
        register(EntityMinecartHopper.class, minecart, new ItemStack(Blocks.HOPPER));
    }

    private void register(Class<? extends Entity> clazz, ItemStack... stacks){
        entityToStacks.put(clazz, stacks);
    }

    @Override
    public EventPriority getPriority(){
        return EventPriority.NORMAL;
    }

    @Override
    public void addIngredients(EntityContext context, IIngredientList ingredients){
        ItemStack[] stacks = entityToStacks.get(context.entity.getClass());
        if(stacks != null) {
            for(ItemStack stack : stacks) {
                ingredients.addItemStack(stack);
            }

            if(context.entity instanceof EntityMinecartContainer) {
                IItemHandler handler = context.entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                ProviderInventoryContents.appendItemHandler(handler, ingredients);
            }
        }
    }

}
