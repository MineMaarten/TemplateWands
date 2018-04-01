package com.minemaarten.templatewands.api.ingredients;

import com.minemaarten.templatewands.api.util.BlockContext;

/**
 * Implement this interface to override which items are required to rebuild a certain block.
 * Register it by annotating the class with {@link com.minemaarten.templatewands.api.TemplateWands}
 * @author Maarten
 *
 */
public interface IBlockIngredientProvider extends IIngredientProvider{
    /**
     * Given a certain context in the world, determine if this provider should handle this block type, and append to the item/fluid requirements list
     * 
     * Only one IIngredientProvider may handle a position. When anything is appended to the ingredients list, no other providers are invoked.
     * @param context
     * @param ingredients
     * @return
     */
    void addIngredients(BlockContext context, IIngredientList ingredients);
}
