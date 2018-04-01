package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

@TemplateWands
public class ProviderItemFrame extends TypedEntityIngredientProvider<EntityItemFrame>{

    public ProviderItemFrame(){
        super(EntityItemFrame.class);
    }

    @Override
    public void addIngredients(EntityItemFrame entity, EntityContext context, IIngredientList ingredients){
        ingredients.addItemStack(new ItemStack(Items.ITEM_FRAME));
        if(!entity.getDisplayedItem().isEmpty()) {
            ingredients.addItemStack(entity.getDisplayedItem());
        }
    }

}
