package com.minemaarten.templatewands.templates;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import com.google.common.collect.Lists;
import com.minemaarten.templatewands.templates.ingredients.TemplateIngredient;
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
    private List<TemplateIngredient<?>> ingredients = new ArrayList<>();

    //TODO private List<TemplateIngredient<?>> returned = new ArrayList<>(); // things like empty buckets after emptying a water bucket

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

    /**
     * takes blocks from the world and puts the data them into this template
     * MineMaarten: Copied from super to add item requirement capturing
     */
    @Override
    public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos endPos, boolean takeEntities, @Nullable Block toIgnore){
        if(endPos.getX() >= 1 && endPos.getY() >= 1 && endPos.getZ() >= 1) {
            BlockPos blockpos = startPos.add(endPos).add(-1, -1, -1);
            List<Template.BlockInfo> list = Lists.<Template.BlockInfo> newArrayList();
            List<Template.BlockInfo> list1 = Lists.<Template.BlockInfo> newArrayList();
            List<Template.BlockInfo> list2 = Lists.<Template.BlockInfo> newArrayList();
            BlockPos blockpos1 = new BlockPos(Math.min(startPos.getX(), blockpos.getX()), Math.min(startPos.getY(), blockpos.getY()), Math.min(startPos.getZ(), blockpos.getZ()));
            BlockPos blockpos2 = new BlockPos(Math.max(startPos.getX(), blockpos.getX()), Math.max(startPos.getY(), blockpos.getY()), Math.max(startPos.getZ(), blockpos.getZ()));
            this.size = endPos;

            for(BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos1, blockpos2)) {
                BlockPos blockpos3 = blockpos$mutableblockpos.subtract(blockpos1);
                IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);

                if(toIgnore == null || toIgnore != iblockstate.getBlock()) {
                    TileEntity tileentity = worldIn.getTileEntity(blockpos$mutableblockpos);

                    if(tileentity != null) {
                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                        nbttagcompound.removeTag("x");
                        nbttagcompound.removeTag("y");
                        nbttagcompound.removeTag("z");
                        list1.add(new Template.BlockInfo(blockpos3, iblockstate, nbttagcompound));
                    } else if(!iblockstate.isFullBlock() && !iblockstate.isFullCube()) {
                        list2.add(new Template.BlockInfo(blockpos3, iblockstate, (NBTTagCompound)null));
                    } else {
                        list.add(new Template.BlockInfo(blockpos3, iblockstate, (NBTTagCompound)null));
                    }
                }
            }

            this.blocks.clear();
            this.blocks.addAll(list);
            this.blocks.addAll(list1);
            this.blocks.addAll(list2);

            if(takeEntities) {
                //TODO this.takeEntitiesFromWorld(worldIn, blockpos1, blockpos2.add(1, 1, 1));
            } else {
                this.entities.clear();
            }
        }
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
