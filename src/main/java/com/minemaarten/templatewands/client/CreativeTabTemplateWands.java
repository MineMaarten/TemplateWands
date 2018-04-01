package com.minemaarten.templatewands.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.init.ModItems;

public class CreativeTabTemplateWands extends CreativeTabs{

    public CreativeTabTemplateWands(String par2Str){
        super(par2Str);
    }

    @Override
    public ItemStack getTabIconItem(){
        return new ItemStack(ModItems.TEMPLATE_WAND_DIAMOND);
    }

}
