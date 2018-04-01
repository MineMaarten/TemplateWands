package com.minemaarten.templatewands.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler{

    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("TemplateWands");
    private static int discriminant;

    public static void init(){
        INSTANCE.registerMessage(PacketUpdateTemplate.class, PacketUpdateTemplate.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketUpdateCapturing.class, PacketUpdateCapturing.class, discriminant++, Side.CLIENT);

        INSTANCE.registerMessage(PacketInteractInMidAir.class, PacketInteractInMidAir.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(PacketUpdateHoveredPos.class, PacketUpdateHoveredPos.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(PacketChangeRepeatAmount.class, PacketChangeRepeatAmount.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(PacketTogglePlacementLock.class, PacketTogglePlacementLock.class, discriminant++, Side.SERVER);
    }

    public static void sendToAll(IMessage message){

        INSTANCE.sendToAll(message);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player){

        INSTANCE.sendTo(message, player);
    }

    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point){

        INSTANCE.sendToAllAround(message, point);
    }

    public static void sendToDimension(IMessage message, int dimensionId){

        INSTANCE.sendToDimension(message, dimensionId);
    }

    public static void sendToServer(IMessage message){

        INSTANCE.sendToServer(message);
    }
}
