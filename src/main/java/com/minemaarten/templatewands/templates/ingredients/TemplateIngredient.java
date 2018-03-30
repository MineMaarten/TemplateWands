package com.minemaarten.templatewands.templates.ingredients;

public abstract class TemplateIngredient<TIngredient> {
    private final Class<TIngredient> superClass;

    public TemplateIngredient(Class<TIngredient> superClass){
        this.superClass = superClass;
    }

    @SuppressWarnings("unchecked")
    public boolean applies(Object obj){
        if(obj != null && superClass.isAssignableFrom(obj.getClass())) { //TODO cache superclassing and do set.contains instead.
            return appliesTo((TIngredient)obj);
        } else {
            return false;
        }
    }

    public abstract boolean appliesTo(TIngredient ingredient);
}
