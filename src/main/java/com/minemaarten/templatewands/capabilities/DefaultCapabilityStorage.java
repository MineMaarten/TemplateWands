package com.minemaarten.templatewands.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.INBTSerializable;

public class DefaultCapabilityStorage<TCap extends INBTSerializable<NBTTagCompound>> implements IStorage<TCap>{

    @Override
    public NBTBase writeNBT(Capability<TCap> capability, TCap instance, EnumFacing side){
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<TCap> capability, TCap instance, EnumFacing side, NBTBase nbt){
        instance.deserializeNBT((NBTTagCompound)nbt);
    }

}
