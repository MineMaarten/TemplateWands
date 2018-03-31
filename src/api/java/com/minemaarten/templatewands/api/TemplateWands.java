package com.minemaarten.templatewands.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.minemaarten.templatewands.api.blacklist.IBlacklistProvider;
import com.minemaarten.templatewands.api.ingredients.IBlockIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IEntityIngredientProvider;

/**
 * Annotation that should be used on {@link IBlockIngredientProvider}, {@link IEntityIngredientProvider} and {@link IBlacklistProvider} implementations.
 * @author Maarten
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TemplateWands{

}
