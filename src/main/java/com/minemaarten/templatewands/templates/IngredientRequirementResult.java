package com.minemaarten.templatewands.templates;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import com.minemaarten.templatewands.templates.ingredients.TemplateIngredient;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredientFluidStack;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredientItemStack;
import com.minemaarten.templatewands.templates.ingredients.providers.IngredientList;
import com.minemaarten.templatewands.util.Log;

public class IngredientRequirementResult{

    private final IItemHandler inventory;
    private final IngredientList ingredients;
    private final IngredientList missingIngredients;

    public static final IngredientRequirementResult EMPTY = new IngredientRequirementResult(new ItemStackHandler(0), new IngredientList());

    /**
     * @param inventory An inventory that is known to have lean extraction rules, so that any ItemStack from getStackInSlot can be extracted
     * @param ingredients
     */
    public IngredientRequirementResult(IItemHandler inventory, IngredientList ingredients){
        this.inventory = inventory;
        this.ingredients = ingredients;
        IngredientList inventoryIngredients = toIngredientList(inventory);
        missingIngredients = ingredients.copy();
        missingIngredients.subtract(inventoryIngredients);
    }

    private static IngredientList toIngredientList(IItemHandler inventory){
        IngredientList ingredients = new IngredientList();
        for(int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if(!stack.isEmpty()) {
                FluidStack fluid = FluidUtil.getFluidContained(stack);
                if(fluid != null) { //When the item has fluid (like buckets, tanks), provide it as fluid
                    ingredients.addFluidStack(fluid);
                } else { //Else as an item
                    ingredients.addItemStack(stack);
                }
            }
        }
        return ingredients;
    }

    public boolean hasAllRequiredItems(){
        return missingIngredients.ingredients.isEmpty();
    }

    public IngredientList getMissingIngredients(){
        return missingIngredients;
    }

    public void takeItems(){
        for(TemplateIngredient<?> ingredient : ingredients) {
            if(ingredient instanceof TemplateIngredientItemStack) {
                TemplateIngredientItemStack itemIngredient = (TemplateIngredientItemStack)ingredient;
                if(!extractItems(inventory, itemIngredient::appliesTo, itemIngredient.getAmount())) {
                    Log.warning("Could not extract the ingredient " + ingredient + " (dupe bug!)");
                }
            }
            if(ingredient instanceof TemplateIngredientFluidStack) {
                TemplateIngredientFluidStack fluidIngredient = (TemplateIngredientFluidStack)ingredient;
                if(!extractFluids(inventory, fluidIngredient.ingredient.getFluid(), fluidIngredient.getAmount())) {
                    Log.warning("Could not extract the ingredient " + ingredient + " (dupe bug!)");
                }
            }
        }
    }

    private static boolean extractItems(IItemHandler handler, Predicate<ItemStack> matcher, int amount){
        for(int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if(!stack.isEmpty() && matcher.test(stack)) {
                ItemStack extracted = handler.extractItem(slot, amount, false);
                amount -= extracted.getCount();
                if(amount <= 0) return true;
            }
        }
        return false;
    }

    private static boolean extractFluids(IItemHandler handler, Fluid fluid, int amount){
        for(int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if(!stack.isEmpty()) {
                ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(stack, 1); // do not modify the input
                IFluidHandlerItem containerFluidHandler = FluidUtil.getFluidHandler(containerCopy);
                if(containerFluidHandler != null) {
                    FluidStack toExtract = new FluidStack(fluid, amount);
                    FluidStack extracted = containerFluidHandler.drain(toExtract, true);
                    if(extracted != null) {
                        if(handler.extractItem(slot, stack.getCount(), false).getCount() != stack.getCount()) {
                            Log.warning("Could not extract tank item " + stack);
                        }

                        if(!containerFluidHandler.getContainer().isEmpty()) {
                            if(!handler.insertItem(slot, containerFluidHandler.getContainer(), false).isEmpty()) {
                                Log.warning("Could not insert emtpied tank item " + containerFluidHandler.getContainer());
                            }
                        }

                        amount -= extracted.amount;
                        if(amount <= 0) return true;
                    }

                }
            }
        }
        return false;
    }

}
