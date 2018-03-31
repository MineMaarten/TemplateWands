package com.minemaarten.templatewands.templates.ingredients;

import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.lang3.Validate;

public abstract class TemplateIngredient<TIngredient> {
    private final Class<?> ingredientClass;
    public final TIngredient ingredient;

    public TemplateIngredient(TIngredient ingredient){
        Validate.notNull(ingredient);
        this.ingredientClass = ingredient.getClass();
        this.ingredient = ingredient;
    }

    @SuppressWarnings("unchecked")
    public boolean applies(TemplateIngredient<?> ingredient){
        if(ingredientClass == ingredient.ingredientClass) {
            return appliesTo((TIngredient)ingredient.ingredient);
        } else {
            return false;
        }
    }

    public abstract boolean appliesTo(TIngredient ingredient);

    public abstract void addAmount(int amount);

    public abstract int getAmount();

    public TemplateIngredient<?> copy(){
        NBTTagCompound tag = new NBTTagCompound(); //TODO improve copy performance by not using NBT (de)serialization
        writeToNBT(tag);
        return fromTag(tag);
    }

    public void writeToNBT(NBTTagCompound tag){
        tag.setByte("ingredientType", (byte)getIngredientType().ordinal());
    }

    protected abstract EnumIngredientType getIngredientType();

    protected enum EnumIngredientType{
        ITEM_STACK, ITEM_STACK_EXACT, FLUID_STACK;

        private static EnumIngredientType[] VALUES = values();
    }

    public static TemplateIngredient<?> fromTag(NBTTagCompound tag){
        EnumIngredientType type = EnumIngredientType.VALUES[tag.getByte("ingredientType")];
        switch(type){
            case FLUID_STACK:
                return new TemplateIngredientFluidStack(tag);
            case ITEM_STACK:
                return new TemplateIngredientItemStack(tag);
            case ITEM_STACK_EXACT:
                return new TemplateIngredientItemStackExact(tag);
            default:
                throw new IllegalStateException("Unknown ingredient type: " + type);
        }
    }
}
