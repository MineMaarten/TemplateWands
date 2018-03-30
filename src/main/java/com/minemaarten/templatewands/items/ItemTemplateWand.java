package com.minemaarten.templatewands.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.minemaarten.templatewands.capabilities.CapabilityTemplateWand;
import com.minemaarten.templatewands.capabilities.DefaultCapabilityProvider;

public class ItemTemplateWand extends Item{
    public ItemTemplateWand(){
        setCreativeTab(CreativeTabs.TOOLS); //TODO custom tab
        setRegistryName("template_wand");
        setUnlocalizedName("template_wand");
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
        return new DefaultCapabilityProvider<>(CapabilityTemplateWand.INSTANCE);
    }

    private CapabilityTemplateWand getCap(ItemStack stack){
        return stack.getCapability(CapabilityTemplateWand.INSTANCE, null);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand){
        if(!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            CapabilityTemplateWand cap = getCap(stack);
            if(!player.isSneaking()) {
                if(cap.hasTemplate()) {
                    player.sendStatusMessage(new TextComponentString("Template placed"), false); //TODO language table
                    cap.place(world, pos);
                    return EnumActionResult.SUCCESS;
                } else {
                    if(cap.registerCoordinate(world, pos)) {
                        player.sendStatusMessage(new TextComponentString("Coordinate registered"), false); //TODO language table
                        return EnumActionResult.SUCCESS;
                    } else {
                        player.sendStatusMessage(new TextComponentString("Area too big"), false); //TODO language table
                        return EnumActionResult.FAIL;
                    }
                }
            } else {
                cap.clearTemplate();
                player.sendStatusMessage(new TextComponentString("Wand cleared"), false); //TODO language table
                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        //TODO capture by clicking in mid-air
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
