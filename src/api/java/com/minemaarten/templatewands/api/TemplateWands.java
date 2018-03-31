package com.minemaarten.templatewands.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.minemaarten.templatewands.api.ingredients.IIngredientProvider;

/**
 * Annotation that should be used on {@link IIngredientProvider} implementations.
 * @author Maarten
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TemplateWands{

}
