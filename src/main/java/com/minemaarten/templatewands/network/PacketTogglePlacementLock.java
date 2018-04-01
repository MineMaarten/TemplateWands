package com.minemaarten.templatewands.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import com.minemaarten.templatewands.items.ItemTemplateWand;

public class PacketTogglePlacementLock extends AbstractPacket<PacketTogglePlacementLock>{
    private BlockPos pos;
    private EnumFacing heading;

    public PacketTogglePlacementLock(BlockPos pos, EnumFacing heading){
        this.pos = pos;
        this.heading = heading;
    }

    public PacketTogglePlacementLock(){}

    @Override
    public void toBytes(ByteBuf buf){
        new PacketBuffer(buf).writeBlockPos(pos);
        buf.writeByte(heading.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf){
        pos = new PacketBuffer(buf).readBlockPos();
        heading = EnumFacing.VALUES[buf.readByte()];
    }

    @Override
    public void handleClientSide(EntityPlayer player){

    }

    @Override
    public void handleServerSide(EntityPlayer player){
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof ItemTemplateWand) {
            ItemTemplateWand wand = ((ItemTemplateWand)heldItem.getItem());
            wand.togglePlacementLock(player, heldItem, pos, heading);
        }
    }

}
