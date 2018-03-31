package com.minemaarten.templatewands.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityContext{
    public final World world;
    public final Entity entity;
    public final EntityPlayer player;

    public EntityContext(Entity entity, EntityPlayer player){
        this.world = player.getEntityWorld();
        this.entity = entity;
        this.player = player;
    }
}
