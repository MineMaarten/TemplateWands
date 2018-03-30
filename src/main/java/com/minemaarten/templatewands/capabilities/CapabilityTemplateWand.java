package com.minemaarten.templatewands.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;

import com.minemaarten.templatewands.templates.TemplateCapturer;
import com.minemaarten.templatewands.templates.TemplateSurvival;

public class CapabilityTemplateWand implements INBTSerializable<NBTTagCompound>{
    @CapabilityInject(CapabilityTemplateWand.class)
    public static Capability<CapabilityTemplateWand> INSTANCE;

    private TemplateCapturer capturer;
    private TemplateSurvival template;

    public void setTemplate(TemplateSurvival template){
        this.template = template;
    }

    public boolean hasTemplate(){
        return template != null;
    }

    public boolean registerCoordinate(World world, BlockPos pos, EnumFacing facing){
        if(capturer == null) {
            capturer = new TemplateCapturer(pos);
            return true;
        } else {
            template = capturer.capture(world, pos, facing);
            return template != null;
        }
    }

    public void clearTemplate(){
        capturer = null;
        template = null;
    }

    public void place(World world, BlockPos pos, EnumFacing facing){
        if(hasTemplate()) {
            template.addBlocksToWorld(world, pos, facing);
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
