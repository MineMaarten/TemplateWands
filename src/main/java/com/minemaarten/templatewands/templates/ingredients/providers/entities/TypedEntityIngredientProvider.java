package com.minemaarten.templatewands.templates.ingredients.providers.entities;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.ingredients.IEntityIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.EntityContext;

public abstract class TypedEntityIngredientProvider<T extends Entity> implements IEntityIngredientProvider{
    private final Class<T> entityClass;

    public TypedEntityIngredientProvider(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    @Override
    public EventPriority getPriority(){
        return EventPriority.NORMAL;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIngredients(EntityContext context, IInputIngredientList ingredients){
        if(context.entity.getClass() == entityClass) {
            addIngredients((T)context.entity, context, ingredients);
        }
    }

    public abstract void addIngredients(T entity, EntityContext context, IInputIngredientList ingredients);
}
