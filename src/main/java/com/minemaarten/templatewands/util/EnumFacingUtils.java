package com.minemaarten.templatewands.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class EnumFacingUtils{
    public static Rotation getRotation(EnumFacing oldFacing, EnumFacing newFacing){
        if(oldFacing.getAxis() == Axis.Y) throw new IllegalArgumentException("Only horizontals are allowed for oldFacing!");
        if(newFacing.getAxis() == Axis.Y) throw new IllegalArgumentException("Only horizontals are allowed for newFacing!");

        int index = (4 + newFacing.getHorizontalIndex() - oldFacing.getHorizontalIndex()) & 0b11;
        switch(index){
            case 0:
                return Rotation.NONE;
            case 1:
                return Rotation.CLOCKWISE_90;
            case 2:
                return Rotation.CLOCKWISE_180;
            case 3:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a new block pos, where only the axis of the given 'dir' has a value.
     * @param pos
     * @param dir
     * @return
     */
    public static BlockPos getComponent(BlockPos pos, EnumFacing dir){
        return new BlockPos(pos.getX() * dir.getDirectionVec().getX(), pos.getY() * dir.getDirectionVec().getY(), pos.getZ() * dir.getDirectionVec().getZ());
    }
}
