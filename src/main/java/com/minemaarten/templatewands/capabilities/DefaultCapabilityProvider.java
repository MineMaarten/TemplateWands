package com.minemaarten.templatewands.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class DefaultCapabilityProvider<TCap> implements ICapabilitySerializable<NBTBase>{
    private final Capability<TCap> cap;
    private final TCap capInstance;

    public DefaultCapabilityProvider(Capability<TCap> cap){
        this.cap = cap;
        capInstance = cap.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        return capability == cap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        if(hasCapability(capability, facing)) {
            return (T)capInstance;
        } else {
            return null;
        }
    }

    @Override
    public NBTBase serializeNBT(){
        return cap.getStorage().writeNBT(cap, capInstance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt){
        cap.getStorage().readNBT(cap, capInstance, null, nbt);
    }
}
