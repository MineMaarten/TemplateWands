package com.minemaarten.templatewands.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.minemaarten.templatewands.TemplateWands;

public abstract class AbstractPacket<REQ extends AbstractPacket<REQ>> implements IMessage, IMessageHandler<REQ, REQ>{

    @Override
    public REQ onMessage(final REQ message, final MessageContext ctx){
        if(ctx.side == Side.SERVER) {
            TemplateWands.proxy.addScheduledTask(() -> {
                message.handleServerSide(ctx.getServerHandler().player);
            }, ctx.side == Side.SERVER);
        } else {
            TemplateWands.proxy.addScheduledTask(() -> {
                message.handleClientSide(TemplateWands.proxy.getPlayer());
            }, ctx.side == Side.SERVER);
        }
        return null;
    }

    /**
     * Handle a packet on the client side. Note this occurs after decoding has completed.
     * 
     * @param player
     *            the player reference
     */
    public abstract void handleClientSide(EntityPlayer player);

    /**
     * Handle a packet on the server side. Note this occurs after decoding has completed.
     * 
     * @param player
     *            the player reference
     */
    public abstract void handleServerSide(EntityPlayer player);
}
