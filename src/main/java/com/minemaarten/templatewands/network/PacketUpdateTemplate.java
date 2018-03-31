package com.minemaarten.templatewands.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.minemaarten.templatewands.capabilities.CapabilityTemplateWand;
import com.minemaarten.templatewands.templates.TemplateSurvival;

public class PacketUpdateTemplate extends AbstractPacket<PacketUpdateTemplate>{
    private TemplateSurvival template;

    public PacketUpdateTemplate(TemplateSurvival template){
        this.template = template;
    }

    public PacketUpdateTemplate(){}

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeBoolean(template != null);
        if(template != null) template.writeToBuf(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf){
        template = buf.readBoolean() ? TemplateSurvival.fromByteBuf(buf) : null;
    }

    @Override
    public void handleClientSide(EntityPlayer player){
        ItemStack heldItem = player.getHeldItemMainhand();
        CapabilityTemplateWand cap = heldItem.getCapability(CapabilityTemplateWand.INSTANCE, null);
        if(cap != null) {
            cap.setTemplate(template);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player){

    }

}
