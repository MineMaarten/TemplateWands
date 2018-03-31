package com.minemaarten.templatewands.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.minemaarten.templatewands.network.NetworkHandler;
import com.minemaarten.templatewands.network.PacketUpdateTemplate;
import com.minemaarten.templatewands.templates.IngredientRequirementResult;
import com.minemaarten.templatewands.templates.TemplateCapturer;
import com.minemaarten.templatewands.templates.TemplateSurvival;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredient;

public class CapabilityTemplateWand implements INBTSerializable<NBTTagCompound>{
    @CapabilityInject(CapabilityTemplateWand.class)
    public static Capability<CapabilityTemplateWand> INSTANCE;

    private TemplateCapturer capturer;
    private TemplateSurvival template;

    public void setTemplate(TemplateSurvival template){
        this.template = template;
    }

    public TemplateSurvival getTemplate(){
        return template;
    }

    public boolean hasTemplate(){
        return template != null;
    }

    public boolean registerCoordinate(World world, BlockPos pos, EntityPlayer player){
        if(capturer == null) {
            capturer = new TemplateCapturer(pos);
            return true;
        } else {
            template = capturer.capture(world, pos, player);
            NetworkHandler.sendTo(new PacketUpdateTemplate(template), (EntityPlayerMP)player);
            return template != null;
        }
    }

    public void clearTemplate(EntityPlayer player){
        capturer = null;
        template = null;
        NetworkHandler.sendTo(new PacketUpdateTemplate(template), (EntityPlayerMP)player);
    }

    public void place(World world, BlockPos pos, EntityPlayer player, EnumFacing facing){
        if(hasTemplate()) {
            IItemHandler itemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            IngredientRequirementResult result = template.addBlocksToWorld(world, pos, facing, !player.isCreative(), itemHandler);
            if(result.hasAllRequiredItems()) {
                player.sendStatusMessage(new TextComponentString("Template placed"), false); //TODO language table
            } else {
                player.sendStatusMessage(new TextComponentString("Missing:"), false); //TODO language table
                for(TemplateIngredient<?> ingredient : result.getMissingIngredients()) {
                    player.sendStatusMessage(new TextComponentString(ingredient.toString()), false);
                }
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT(){
        NBTTagCompound tag = new NBTTagCompound();
        if(template != null) {
            tag.setBoolean("hasTemplate", true);
            template.writeToNBT(tag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt){
        if(nbt.getBoolean("hasTemplate")) {
            template = new TemplateSurvival();
            template.read(nbt);
        } else {
            template = null;
        }
    }
}
