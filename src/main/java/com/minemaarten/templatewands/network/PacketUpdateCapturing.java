package com.minemaarten.templatewands.network;

import io.netty.buffer.ByteBuf;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import com.minemaarten.templatewands.capabilities.CapabilityTemplateWand;

public class PacketUpdateCapturing extends AbstractPacket<PacketUpdateCapturing>{
    private BlockPos startPos;
    private Set<BlockPos> blacklistedPositions;

    public PacketUpdateCapturing(BlockPos startPos, Set<BlockPos> blacklistedPositions){
        this.startPos = startPos;
        this.blacklistedPositions = blacklistedPositions;
    }

    public PacketUpdateCapturing(){}

    @Override
    public void toBytes(ByteBuf buf){
        PacketBuffer b = new PacketBuffer(buf);
        b.writeBlockPos(startPos);
        b.writeInt(blacklistedPositions.size());
        for(BlockPos p : blacklistedPositions) {
            b.writeBlockPos(p);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf){
        PacketBuffer b = new PacketBuffer(buf);
        startPos = b.readBlockPos();
        int count = b.readInt();
        blacklistedPositions = new HashSet<>(count);
        for(int i = 0; i < count; i++) {
            blacklistedPositions.add(b.readBlockPos());
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player){
        ItemStack heldItem = player.getHeldItemMainhand();
        CapabilityTemplateWand cap = heldItem.getCapability(CapabilityTemplateWand.INSTANCE, null);
        if(cap != null) {
            cap.updateCaptureState(startPos, blacklistedPositions);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player){

    }

}
