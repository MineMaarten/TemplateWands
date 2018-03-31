package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.item.EntityItem;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

@TemplateWands
public class ProviderEntityItem extends TypedEntityIngredientProvider<EntityItem>{

    public ProviderEntityItem(){
        super(EntityItem.class);
    }

    @Override
    public void addIngredients(EntityItem entity, EntityContext context, IInputIngredientList ingredients){
        if(!entity.getItem().isEmpty()) {
            ingredients.addItemStackExact(entity.getItem());
        }
    }

}
