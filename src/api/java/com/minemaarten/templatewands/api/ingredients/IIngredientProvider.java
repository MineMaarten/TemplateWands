package com.minemaarten.templatewands.api.ingredients;

import com.minemaarten.templatewands.api.util.BlockContext;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

/**
 * Implement this interface to override which items are required to rebuild a certain block.
 * Register it by annotating the class with {@link com.minemaarten.templatewands.api.TemplateWands}
 * @author Maarten
 *
 */
public interface IIngredientProvider{

    /**
     * The priority, that determines the visit order of this provider. For 'last resort' providers, this should be LOWEST, for specials it should be HIGHEST.
     * @return
     */
    EventPriority getPriority();

    /**
     * Given a certain context in the world, determine if this provider should handle this block type, and append to the item/fluid requirements list
     * 
     * Only one IIngredientProvider may handle a position. When anything is appended to the ingredients list, no other providers are invoked.
     * @param context
     * @param ingredients
     * @return
     */
    void addIngredients(BlockContext context, IInputIngredientList ingredients);
}
