package com.minemaarten.templatewands.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy{

    public void init(){

    }

    public void addScheduledTask(Runnable runnable, boolean serverSide){
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
    }

    public EntityPlayer getPlayer(){
        throw new IllegalStateException("Cannot call server side!");
    }
}
