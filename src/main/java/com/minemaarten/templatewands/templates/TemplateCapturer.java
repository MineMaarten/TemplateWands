package com.minemaarten.templatewands.templates;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.minemaarten.templatewands.items.IAABBRenderer.ColoredAABB;
import com.minemaarten.templatewands.network.NetworkHandler;
import com.minemaarten.templatewands.network.PacketUpdateCapturing;

public class TemplateCapturer{
    public final BlockPos firstPos;

    public BlockPos lastKnownClientPos;
    public Set<BlockPos> blacklistedPositions = Collections.emptySet();

    public TemplateCapturer(BlockPos firstPos){
        this.firstPos = firstPos;
    }

    public TemplateSurvival capture(World world, BlockPos secondPos, EntityPlayer player){
        BlockPos startPos = new BlockPos(Math.min(firstPos.getX(), secondPos.getX()), Math.min(firstPos.getY(), secondPos.getY()), Math.min(firstPos.getZ(), secondPos.getZ()));
        BlockPos maxPos = new BlockPos(Math.max(firstPos.getX(), secondPos.getX()), Math.max(firstPos.getY(), secondPos.getY()), Math.max(firstPos.getZ(), secondPos.getZ()));
        BlockPos size = maxPos.subtract(startPos).add(1, 1, 1);

        TemplateSurvival template = new TemplateSurvival();
        template.setCaptureFacing(player.getHorizontalFacing());
        template.takeBlocksFromWorld(world, startPos, size, true, null, player, secondPos);
        return template;
    }

    public void onInterval(World world, EntityPlayer player){
        if(lastKnownClientPos != null) {
            TemplateSurvival template = capture(world, lastKnownClientPos, player);
            if(!template.blacklistedPositions.equals(blacklistedPositions)) {
                blacklistedPositions = template.blacklistedPositions;
                NetworkHandler.sendTo(new PacketUpdateCapturing(firstPos, blacklistedPositions), (EntityPlayerMP)player);
            }
        }
    }

    public void addBlacklistAABBs(Set<ColoredAABB> aabbs){
        for(BlockPos p : blacklistedPositions) {
            aabbs.add(new ColoredAABB(new AxisAlignedBB(p), 0xFF0000));
        }
    }
}
