package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

@TemplateWands
public class ProviderArmorStand extends TypedEntityIngredientProvider<EntityArmorStand>{

    public ProviderArmorStand(){
        super(EntityArmorStand.class);
    }

    @Override
    public void addIngredients(EntityArmorStand entity, EntityContext context, IIngredientList ingredients){
        ingredients.addItemStack(new ItemStack(Items.ARMOR_STAND));
        for(ItemStack stack : entity.getArmorInventoryList()) {
            if(!stack.isEmpty()) {
                ingredients.addItemStack(stack);
            }
        }
        for(ItemStack stack : entity.getHeldEquipment()) {
            if(!stack.isEmpty()) {
                ingredients.addItemStack(stack);
            }
        }
    }

}
