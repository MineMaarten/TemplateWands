package com.minemaarten.templatewands.templates;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import com.minemaarten.templatewands.util.EnumFacingUtils;

/**
 * A template with extra information about the required items to recreate this template.
 * @author Maarten
 *
 */
public class TemplateSurvival extends Template{
    /**
     * The facing at which this template was captured at, used to determine the rotation required for placement
     */
    private EnumFacing captureFacing;

    public TemplateSurvival setCaptureFacing(EnumFacing captureFacing){
        if(captureFacing.getAxis() == Axis.Y) throw new IllegalArgumentException("Only horizontals are allowed!");
        this.captureFacing = captureFacing;
        return this;
    }

    public void addBlocksToWorld(World world, BlockPos pos, EnumFacing facing){
        Rotation rotation = EnumFacingUtils.getRotation(captureFacing, facing);
        PlacementSettings settings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(rotation).setIgnoreEntities(true).setChunk(null).setReplacedBlock(null).setIgnoreStructureBlock(true);
        addBlocksToWorld(world, pos, settings);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag){
        tag.setByte("facing", (byte)captureFacing.ordinal());
        return super.writeToNBT(tag);
    }

    @Override
    public void read(NBTTagCompound tag){
        captureFacing = EnumFacing.VALUES[tag.getByte("facing")];
        super.read(tag);
    }
}
