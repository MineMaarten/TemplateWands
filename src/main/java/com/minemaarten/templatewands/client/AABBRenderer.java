package com.minemaarten.templatewands.client;

import java.util.Set;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;

import com.minemaarten.templatewands.items.IAABBRenderer.ColoredAABB;

public class AABBRenderer{
    public void render(Tessellator t, Set<ColoredAABB> aabbs){
        for(ColoredAABB aabb : aabbs) {
            render(t, aabb);
        }
    }

    private void render(Tessellator t, ColoredAABB aabb){
        RenderGlobal.drawSelectionBoundingBox(aabb.aabb, aabb.r, aabb.g, aabb.b, 1);
    }

}
