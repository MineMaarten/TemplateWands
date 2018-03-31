package com.minemaarten.templatewands.templates.ingredients.providers;

import java.util.ArrayList;
import java.util.List;

import com.minemaarten.templatewands.APIHandler;
import com.minemaarten.templatewands.api.blacklist.IBlacklistProvider;
import com.minemaarten.templatewands.api.ingredients.IIngredientProvider;
import com.minemaarten.templatewands.api.util.BlockContext;

public class IngredientProviderManager{
    private List<IIngredientProvider> ingredientProviders = new ArrayList<>();
    private List<IBlacklistProvider> blacklistProviders = new ArrayList<>();

    public enum EnumCaptureStatus{
        ALLOWED, BLACKLISTED
    }

    public IngredientProviderManager(APIHandler apiHandler){
        this.ingredientProviders = apiHandler.ingredientProviders;
        this.blacklistProviders = apiHandler.blacklistProviders;
    }

    public EnumCaptureStatus addIngredients(BlockContext context, IngredientList ingredients){
        if(isBlacklisted(context)) return EnumCaptureStatus.BLACKLISTED;

        ingredients.resetChanged();
        for(IIngredientProvider provider : ingredientProviders) {
            provider.addIngredients(context, ingredients);
            if(ingredients.hasChanged()) {
                return EnumCaptureStatus.ALLOWED;
            }
        }
        return EnumCaptureStatus.BLACKLISTED; //Blocks that don't require ingredients won't be placed either (like fluid flowing blocks)
    }

    private boolean isBlacklisted(BlockContext context){
        for(IBlacklistProvider provider : blacklistProviders) {
            if(provider.shouldBlacklist(context)) return true;
        }
        return false;
    }
}
