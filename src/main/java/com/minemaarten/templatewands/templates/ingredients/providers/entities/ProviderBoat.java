package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

@TemplateWands
public class ProviderBoat extends TypedEntityIngredientProvider<EntityBoat>{

    public ProviderBoat(){
        super(EntityBoat.class);
    }

    @Override
    public void addIngredients(EntityBoat entity, EntityContext context, IIngredientList ingredients){
        ingredients.addItemStack(new ItemStack(entity.getItemBoat()));
    }

}
