package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

@TemplateWands
public class ProviderBoat extends TypedEntityIngredientProvider<EntityBoat>{

    public ProviderBoat(){
        super(EntityBoat.class);
    }

    @Override
    public void addIngredients(EntityBoat entity, EntityContext context, IInputIngredientList ingredients){
        ingredients.addItemStackExact(new ItemStack(entity.getItemBoat()));
    }

}
