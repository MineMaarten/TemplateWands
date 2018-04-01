package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.item.EntityItem;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

@TemplateWands
public class ProviderEntityItem extends TypedEntityIngredientProvider<EntityItem>{

    public ProviderEntityItem(){
        super(EntityItem.class);
    }

    @Override
    public void addIngredients(EntityItem entity, EntityContext context, IIngredientList ingredients){
        if(!entity.getItem().isEmpty()) {
            ingredients.addItemStack(entity.getItem());
        }
    }

}
