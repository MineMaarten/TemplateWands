package com.minemaarten.templatewands.templates.ingredients.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredient;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredientFluidStack;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredientItemStack;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredientItemStackExact;

public class IngredientList implements IInputIngredientList, Iterable<TemplateIngredient<?>>{

    public List<TemplateIngredient<?>> ingredients = new ArrayList<>();
    private boolean hasChanged;

    @Override
    public void addItemStack(ItemStack stack){
        if(stack.isEmpty()) throw new IllegalArgumentException("Stack may not be empty!");
        add(new TemplateIngredientItemStack(stack));
    }

    @Override
    public void addItemStackExact(ItemStack stack){
        if(stack.isEmpty()) throw new IllegalArgumentException("Stack may not be empty!");
        add(new TemplateIngredientItemStackExact(stack));
    }

    @Override
    public void addFluidStack(FluidStack fluid){
        add(new TemplateIngredientFluidStack(fluid));
    }

    private void add(TemplateIngredient<?> ingredient){
        hasChanged = true;
        for(TemplateIngredient<?> existing : ingredients) {
            if(existing.getClass() == ingredient.getClass() && existing.applies(ingredient)) {
                existing.addAmount(ingredient.getAmount());
                return;
            }
        }

        ingredients.add(ingredient);
    }

    public void resetChanged(){
        hasChanged = false;
    }

    public boolean hasChanged(){
        return hasChanged;
    }

    public void markChanged(){
        hasChanged = true;
    }

    public IngredientList copy(){
        IngredientList copy = new IngredientList();
        for(TemplateIngredient<?> ingredient : ingredients) {
            copy.ingredients.add(ingredient.copy());
        }
        return copy;
    }

    public void subtract(IngredientList other){
        for(TemplateIngredient<?> toSubtract : other.ingredients) {
            subtract(toSubtract);
        }
    }

    private void subtract(TemplateIngredient<?> other){
        Iterator<TemplateIngredient<?>> iterator = ingredients.iterator();
        while(iterator.hasNext()) {
            TemplateIngredient<?> ingredient = iterator.next();
            if(ingredient.applies(other)) {
                if(ingredient.getAmount() > other.getAmount()) {
                    ingredient.addAmount(-other.getAmount());
                } else {
                    iterator.remove();
                }
                break; //Because similar ingredients are merged, we are done looking.
            }
        }
    }

    public void writeToNBT(NBTTagCompound tag){
        NBTTagList tagList = new NBTTagList();
        for(TemplateIngredient<?> ingredient : ingredients) {
            NBTTagCompound t = new NBTTagCompound();
            ingredient.writeToNBT(t);
            tagList.appendTag(t);
        }
        tag.setTag("ingredients", tagList);
    }

    public static IngredientList fromNBT(NBTTagCompound tag){
        IngredientList ingredients = new IngredientList();
        NBTTagList tagList = tag.getTagList("ingredients", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound t = tagList.getCompoundTagAt(i);
            ingredients.ingredients.add(TemplateIngredient.fromTag(t));
        }
        return ingredients;
    }

    @Override
    public Iterator<TemplateIngredient<?>> iterator(){
        return ingredients.iterator();
    }
}
