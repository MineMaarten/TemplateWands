package com.minemaarten.templatewands.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.items.ItemTemplateWand;

public class PacketChangeRepeatAmount extends AbstractPacket<PacketChangeRepeatAmount>{
    private boolean increase;

    public PacketChangeRepeatAmount(boolean increase){
        this.increase = increase;
    }

    public PacketChangeRepeatAmount(){}

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeBoolean(increase);
    }

    @Override
    public void fromBytes(ByteBuf buf){
        increase = buf.readBoolean();
    }

    @Override
    public void handleClientSide(EntityPlayer player){

    }

    @Override
    public void handleServerSide(EntityPlayer player){
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof ItemTemplateWand) {
            ItemTemplateWand wand = ((ItemTemplateWand)heldItem.getItem());
            if(increase) {
                wand.incRepeatAmount(player, heldItem);
            } else {
                wand.decRepeatAmount(player, heldItem);
            }
        }
    }

}
