package com.minemaarten.templatewands.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
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

    public boolean registerCoordinate(World world, BlockPos pos){
        if(capturer == null) {
            capturer = new TemplateCapturer(pos);
            return true;
        } else {
            template = capturer.capture(world, pos);
            return template != null;
        }
    }

    public void clearTemplate(){
        capturer = null;
        template = null;
    }

    public void place(World world, BlockPos pos){
        if(hasTemplate()) {
            PlacementSettings settings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true).setChunk(null).setReplacedBlock(null).setIgnoreStructureBlock(true);
            template.addBlocksToWorld(world, pos, settings);
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
