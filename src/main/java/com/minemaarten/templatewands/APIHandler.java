package com.minemaarten.templatewands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.blacklist.IBlacklistProvider;
import com.minemaarten.templatewands.api.ingredients.IBlockIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IEntityIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IIngredientProvider;
import com.minemaarten.templatewands.util.Log;

public class APIHandler{
    public List<IBlockIngredientProvider> blockIngredientProviders = new ArrayList<>();
    public List<IEntityIngredientProvider> entityIngredientProviders = new ArrayList<>();
    public List<IBlacklistProvider> blacklistProviders = new ArrayList<>();

    public void initializeAPIImplementors(ASMDataTable asmData){
        Set<ASMData> allAnnotatedClasses = asmData.getAll(com.minemaarten.templatewands.api.TemplateWands.class.getName());
        for(ASMData annotatedClass : allAnnotatedClasses) {
            try {
                Class<?> clazz = Class.forName(annotatedClass.getClassName());
                Log.info("Found class annotating @TemplateWands: " + annotatedClass.getClassName());

                if(IBlockIngredientProvider.class.isAssignableFrom(clazz)) {
                    IBlockIngredientProvider provider = (IBlockIngredientProvider)clazz.newInstance();
                    addPrioritizedIngredientProvider(blockIngredientProviders, provider);
                    Log.info("Successfully registered the IBlockIngredientProvider for \"" + annotatedClass.getClassName() + "\".");
                }

                if(IEntityIngredientProvider.class.isAssignableFrom(clazz)) {
                    IEntityIngredientProvider provider = (IEntityIngredientProvider)clazz.newInstance();
                    addPrioritizedIngredientProvider(entityIngredientProviders, provider);
                    Log.info("Successfully registered the IEntityIngredientProvider for \"" + annotatedClass.getClassName() + "\".");
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
        for(IBlockIngredientProvider provider : blockIngredientProviders) {
            Log.info(provider.getClass().getCanonicalName());
        }

        Log.info("Blacklist providers:");
        for(IBlacklistProvider provider : blacklistProviders) {
            Log.info(provider.getClass().getCanonicalName());
        }
    }

    private <T extends IIngredientProvider> void addPrioritizedIngredientProvider(List<T> providers, T provider){
        EventPriority priority = provider.getPriority();
        for(int i = providers.size() - 1; i >= 0; i--) {
            IIngredientProvider other = providers.get(i);
            if(isLowerOrEqualPriority(priority, other.getPriority())) {
                providers.add(i + 1, provider);
                return;
            }
        }
        providers.add(provider);
    }

    private boolean isLowerOrEqualPriority(EventPriority checkingPriority, EventPriority otherPriority){
        return checkingPriority.ordinal() >= otherPriority.ordinal();
    }
}
