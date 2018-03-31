package com.minemaarten.templatewands.items;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import org.apache.commons.lang3.Validate;

public interface IAABBRenderer{
    public Set<ColoredAABB> getAABBs(ItemStack stack);

    public class ColoredAABB{
        public final AxisAlignedBB aabb;
        public final int color;
        public final float r, g, b;

        public ColoredAABB(AxisAlignedBB aabb, int color){
            Validate.notNull(aabb);
            this.aabb = aabb;
            this.color = color;
            r = (color >> 16) / 256F;
            g = (color >> 8 & 255) / 256F;
            b = (color & 255) / 256F;
        }

        @Override
        public boolean equals(Object obj){
            if(obj instanceof ColoredAABB) {
                ColoredAABB other = (ColoredAABB)obj;
                return other.aabb.equals(aabb) && other.color == color;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode(){
            return aabb.hashCode() * 31 + color;
        }
    }
}
