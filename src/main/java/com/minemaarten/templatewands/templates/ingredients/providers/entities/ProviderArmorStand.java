package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

@TemplateWands
public class ProviderArmorStand extends TypedEntityIngredientProvider<EntityArmorStand>{

    public ProviderArmorStand(){
        super(EntityArmorStand.class);
    }

    @Override
    public void addIngredients(EntityArmorStand entity, EntityContext context, IInputIngredientList ingredients){
        ingredients.addItemStackExact(new ItemStack(Items.ARMOR_STAND));
        for(ItemStack stack : entity.getArmorInventoryList()) {
            if(!stack.isEmpty()) {
                ingredients.addItemStackExact(stack);
            }
        }
        for(ItemStack stack : entity.getHeldEquipment()) {
            if(!stack.isEmpty()) {
                ingredients.addItemStackExact(stack);
            }
        }
    }

}
