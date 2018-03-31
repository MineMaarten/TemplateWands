package com.minemaarten.templatewands.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockContext{
    public final World world;
    public final BlockPos pos;
    public final IBlockState state;
    public final Block block;
    public final TileEntity te;
    public final EntityPlayer player;

    public BlockContext(BlockPos pos, IBlockState state, TileEntity te, EntityPlayer player){
        this.world = player.getEntityWorld();
        this.pos = pos;
        this.state = state;
        this.block = state.getBlock();
        this.te = te;
        this.player = player;
    }
}
