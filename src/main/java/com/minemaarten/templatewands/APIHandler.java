package com.minemaarten.templatewands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.blacklist.IBlacklistProvider;
import com.minemaarten.templatewands.api.ingredients.IIngredientProvider;
import com.minemaarten.templatewands.util.Log;

public class APIHandler{
    public List<IIngredientProvider> ingredientProviders = new ArrayList<>();
    public List<IBlacklistProvider> blacklistProviders = new ArrayList<>();

    public void initializeAPIImplementors(ASMDataTable asmData){
        Set<ASMData> allAnnotatedClasses = asmData.getAll(com.minemaarten.templatewands.api.TemplateWands.class.getName());
        for(ASMData annotatedClass : allAnnotatedClasses) {
            try {
                Class<?> clazz = Class.forName(annotatedClass.getClassName());
                Log.info("Found class annotating @TemplateWands: " + annotatedClass.getClassName());

                if(IIngredientProvider.class.isAssignableFrom(clazz)) {
                    IIngredientProvider provider = (IIngredientProvider)clazz.newInstance();
                    addIngredientProvider(provider);
                    Log.info("Successfully registered the IIngredientProvider for \"" + annotatedClass.getClassName() + "\".");
                }

                if(IBlacklistProvider.class.isAssignableFrom(clazz)) {
                    IBlacklistProvider provider = (IBlacklistProvider)clazz.newInstance();
                    blacklistProviders.add(provider);
                    Log.info("Successfully registered the IBlacklistProvider for \"" + annotatedClass.getClassName() + "\".");
                }
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                Log.error("Annotated class \"" + annotatedClass.getClassName() + "\" could not be instantiated, probably because it is not marked public!");
                e.printStackTrace();
            } catch(InstantiationException e) {
                Log.error("Annotated class \"" + annotatedClass.getClassName() + "\" could not be instantiated, probably because it either does not have a constructor without arguments, or because the class is abstract!");
                e.printStackTrace();
            }
        }

        Log.info("Ingredient providers (ordered by priority):");
        for(IIngredientProvider provider : ingredientProviders) {
            Log.info(provider.getClass().getCanonicalName());
        }

        Log.info("Blacklist providers:");
        for(IBlacklistProvider provider : blacklistProviders) {
            Log.info(provider.getClass().getCanonicalName());
        }
    }

    private void addIngredientProvider(IIngredientProvider provider){
        EventPriority priority = provider.getPriority();
        for(int i = ingredientProviders.size() - 1; i >= 0; i--) {
            IIngredientProvider other = ingredientProviders.get(i);
            if(isLowerOrEqualPriority(priority, other.getPriority())) {
                ingredientProviders.add(i + 1, provider);
                return;
            }
        }
        ingredientProviders.add(provider);
    }

    private boolean isLowerOrEqualPriority(EventPriority checkingPriority, EventPriority otherPriority){
        return checkingPriority.ordinal() >= otherPriority.ordinal();
    }
}
