package com.minemaarten.templatewands.items;

import java.util.Collections;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.minemaarten.templatewands.capabilities.CapabilityTemplateWand;
import com.minemaarten.templatewands.capabilities.DefaultCapabilityProvider;
import com.minemaarten.templatewands.network.NetworkHandler;
import com.minemaarten.templatewands.network.PacketCreateInMidAir;
import com.minemaarten.templatewands.templates.TemplateSurvival;

public class ItemTemplateWand extends Item implements IAABBRenderer{
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
        if(hand != EnumHand.MAIN_HAND) return EnumActionResult.PASS;
        if(!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            CapabilityTemplateWand cap = getCap(stack);
            if(!player.isSneaking()) {
                if(cap.hasTemplate()) {
                    cap.place(world, pos, player, player.getHorizontalFacing());
                    return EnumActionResult.SUCCESS;
                } else {
                    if(cap.registerCoordinate(world, pos, player)) {
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
        if(handIn != EnumHand.MAIN_HAND) return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItemMainhand());
        if(worldIn.isRemote) {
            NetworkHandler.sendToServer(new PacketCreateInMidAir(getHoveredPos(), playerIn.getHorizontalFacing()));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
    }

    @SideOnly(Side.CLIENT)
    private BlockPos getHoveredPos(){
        EntityPlayer player = Minecraft.getMinecraft().player;
        RayTraceResult hoveredObj = Minecraft.getMinecraft().objectMouseOver;
        if(hoveredObj != null && hoveredObj.getBlockPos() != null) {
            return hoveredObj.getBlockPos();
        } else {
            return getMidAirPos(player);
        }
    }

    @SideOnly(Side.CLIENT)
    private BlockPos getMidAirPos(EntityPlayer player){
        float blockReachDistance = Minecraft.getMinecraft().playerController.getBlockReachDistance();
        Vec3d eyePosition = player.getPositionEyes(1);
        Vec3d look = player.getLook(1);
        Vec3d midAirVec = eyePosition.addVector(look.x * blockReachDistance, look.y * blockReachDistance, look.z * blockReachDistance);
        return new BlockPos(midAirVec);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Set<ColoredAABB> getAABBs(ItemStack stack){
        TemplateSurvival template = getCap(stack).getTemplate();
        if(template != null) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            AxisAlignedBB aabb = template.getAABB(getHoveredPos(), player.getHorizontalFacing());
            return Collections.singleton(new ColoredAABB(aabb, 0x00AA00));
        } else {
            return Collections.emptySet();
        }
    }
}
