package com.minemaarten.templatewands.templates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TemplateCapturer{
    private final BlockPos firstPos;

    public TemplateCapturer(BlockPos firstPos){
        this.firstPos = firstPos;
    }

    public TemplateSurvival capture(World world, BlockPos secondPos, EntityPlayer player){
        BlockPos startPos = new BlockPos(Math.min(firstPos.getX(), secondPos.getX()), Math.min(firstPos.getY(), secondPos.getY()), Math.min(firstPos.getZ(), secondPos.getZ()));
        BlockPos maxPos = new BlockPos(Math.max(firstPos.getX(), secondPos.getX()), Math.max(firstPos.getY(), secondPos.getY()), Math.max(firstPos.getZ(), secondPos.getZ()));
        BlockPos size = maxPos.subtract(startPos).add(1, 1, 1);

        TemplateSurvival template = new TemplateSurvival();
        template.setCaptureFacing(player.getHorizontalFacing());
        template.takeBlocksFromWorld(world, startPos, size, true, null, player);
        return template;
    }
}
