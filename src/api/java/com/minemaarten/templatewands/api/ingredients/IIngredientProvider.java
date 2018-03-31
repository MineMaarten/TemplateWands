package com.minemaarten.templatewands.api.ingredients;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

public interface IIngredientProvider{
    /**
     * The priority, that determines the visit order of this provider. For 'last resort' providers, this should be LOWEST, for specials it should be HIGHEST.
     * @return
     */
    EventPriority getPriority();
}
