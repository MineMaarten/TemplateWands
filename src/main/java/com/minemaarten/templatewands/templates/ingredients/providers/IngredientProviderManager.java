package com.minemaarten.templatewands.templates.ingredients.providers;

import java.util.List;

import com.minemaarten.templatewands.APIHandler;
import com.minemaarten.templatewands.api.blacklist.IBlacklistProvider;
import com.minemaarten.templatewands.api.ingredients.IBlockIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IEntityIngredientProvider;
import com.minemaarten.templatewands.api.util.BlockContext;
import com.minemaarten.templatewands.api.util.EntityContext;

public class IngredientProviderManager{
    private final List<IBlockIngredientProvider> blockIngredientProviders;
    private final List<IEntityIngredientProvider> entityIngredientProviders;
    private final List<IBlacklistProvider> blacklistProviders;

    public enum EnumCaptureStatus{
        ALLOWED, BLACKLISTED
    }

    public IngredientProviderManager(APIHandler apiHandler){
        this.blockIngredientProviders = apiHandler.blockIngredientProviders;
        this.entityIngredientProviders = apiHandler.entityIngredientProviders;
        this.blacklistProviders = apiHandler.blacklistProviders;
    }

    public EnumCaptureStatus addIngredients(BlockContext context, IngredientList ingredients){
        if(isBlacklisted(context)) return EnumCaptureStatus.BLACKLISTED;

        ingredients.resetChanged();
        for(IBlockIngredientProvider provider : blockIngredientProviders) {
            provider.addIngredients(context, ingredients);
            if(ingredients.hasChanged()) {
                return EnumCaptureStatus.ALLOWED;
            }
        }
        return EnumCaptureStatus.BLACKLISTED; //Blocks that don't require ingredients won't be placed either (like fluid flowing blocks)
    }

    public EnumCaptureStatus addIngredients(EntityContext context, IngredientList ingredients){
        ingredients.resetChanged();
        for(IEntityIngredientProvider provider : entityIngredientProviders) {
            provider.addIngredients(context, ingredients);
            if(ingredients.hasChanged()) {
                return EnumCaptureStatus.ALLOWED;
            }
        }
        return EnumCaptureStatus.BLACKLISTED; //Entities that don't require ingredients won't be placed either (like fluid flowing blocks)
    }

    private boolean isBlacklisted(BlockContext context){
        for(IBlacklistProvider provider : blacklistProviders) {
            if(provider.shouldBlacklist(context)) return true;
        }
        return false;
    }
}
