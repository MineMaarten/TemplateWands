package com.minemaarten.templatewands.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import com.minemaarten.templatewands.capabilities.CapabilityTemplateWand;

public class PacketUpdateHoveredPos extends AbstractPacket<PacketUpdateHoveredPos>{
    private BlockPos hoverPos;

    public PacketUpdateHoveredPos(BlockPos hoverPos){
        this.hoverPos = hoverPos;
    }

    public PacketUpdateHoveredPos(){}

    @Override
    public void toBytes(ByteBuf buf){
        PacketBuffer b = new PacketBuffer(buf);
        b.writeBlockPos(hoverPos);
    }

    @Override
    public void fromBytes(ByteBuf buf){
        PacketBuffer b = new PacketBuffer(buf);
        hoverPos = b.readBlockPos();
    }

    @Override
    public void handleClientSide(EntityPlayer player){

    }

    @Override
    public void handleServerSide(EntityPlayer player){
        ItemStack heldItem = player.getHeldItemMainhand();
        CapabilityTemplateWand cap = heldItem.getCapability(CapabilityTemplateWand.INSTANCE, null);
        if(cap != null) {
            cap.updateLastKnownHoverPos(hoverPos);
        }
    }

}
