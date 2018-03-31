package com.minemaarten.templatewands.api.ingredients;

import com.minemaarten.templatewands.api.util.EntityContext;

/**
 * Implement this interface to override which items are required to rebuild a certain entity.
 * Register it by annotating the class with {@link com.minemaarten.templatewands.api.TemplateWands}
 * @author Maarten
 *
 */
public interface IEntityIngredientProvider extends IIngredientProvider{
    /**
     * Given a certain context in the world, determine if this provider should handle this entity type, and append to the item/fluid requirements list
     * 
     * Only one IIngredientProvider may handle an entity. When anything is appended to the ingredients list, no other providers are invoked.
     * When no providers handle a given entity, the entity will be ignored and won't be copied.
     * @param context
     * @param ingredients
     * @return
     */
    void addIngredients(EntityContext context, IInputIngredientList ingredients);
}
