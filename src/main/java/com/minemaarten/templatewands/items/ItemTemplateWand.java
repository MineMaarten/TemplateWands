package com.minemaarten.templatewands.items;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import com.minemaarten.templatewands.network.PacketInteractInMidAir;
import com.minemaarten.templatewands.network.PacketUpdateHoveredPos;
import com.minemaarten.templatewands.templates.TemplateCapturer;
import com.minemaarten.templatewands.templates.TemplateSurvival;

public class ItemTemplateWand extends Item implements IAABBRenderer{
    private final int maxBlocks;

    public ItemTemplateWand(int maxBlocks){
        this.maxBlocks = maxBlocks;
        setCreativeTab(CreativeTabs.TOOLS); //TODO custom tab
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
            return interactWand(player, world, pos, stack, player.getHorizontalFacing());
        } else {
            return EnumActionResult.SUCCESS;
        }
    }

    private EnumActionResult interactWand(EntityPlayer player, World world, BlockPos pos, ItemStack stack, EnumFacing facing){
        CapabilityTemplateWand cap = getCap(stack);
        if(!player.isSneaking()) {
            if(cap.hasTemplate()) {
                cap.place(world, pos, player, facing, getRepeatAmount(stack));
                return EnumActionResult.SUCCESS;
            } else {
                if(cap.registerCoordinate(world, pos, player, maxBlocks)) {
                    player.sendStatusMessage(new TextComponentString("Coordinate registered"), false); //TODO language table
                    return EnumActionResult.SUCCESS;
                } else {
                    player.sendStatusMessage(new TextComponentString("Area too big"), false); //TODO language table
                    return EnumActionResult.FAIL;
                }
            }
        } else {
            cap.clearTemplate(player);
            player.sendStatusMessage(new TextComponentString("Wand cleared"), false); //TODO language table
            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        if(handIn != EnumHand.MAIN_HAND) return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItemMainhand());
        if(worldIn.isRemote) {
            NetworkHandler.sendToServer(new PacketInteractInMidAir(getHoveredPos(), playerIn.getHorizontalFacing()));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
    }

    public void onItemRightClick(EntityPlayer player, BlockPos posInMidAir, EnumFacing facing){
        ItemStack heldItem = player.getHeldItemMainhand();
        interactWand(player, player.world, posInMidAir, heldItem, facing);
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
        CapabilityTemplateWand cap = getCap(stack);
        TemplateSurvival template = cap.getTemplate();
        TemplateCapturer capturer = cap.getCapturer();
        if(template != null) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            BlockPos curPos = getHoveredPos();
            Set<ColoredAABB> aabbs = new HashSet<>();
            int repeatAmount = getRepeatAmount(stack);
            for(int i = 0; i < repeatAmount; i++) {
                AxisAlignedBB aabb = template.getAABB(curPos, player.getHorizontalFacing());
                aabbs.add(new ColoredAABB(aabb, 0x00AA00));
                curPos = template.calculateConnectedPos(curPos, player.getHorizontalFacing());
            }
            return aabbs;
        } else if(capturer != null) {
            Set<ColoredAABB> aabbs = new HashSet<>();

            BlockPos hoveredPos = getHoveredPos();
            AxisAlignedBB curCaptureAABB = new AxisAlignedBB(hoveredPos, capturer.firstPos).expand(1, 1, 1);
            AxisAlignedBB a = curCaptureAABB;
            boolean tooBig = (int)(a.maxX - a.minX) * (int)(a.maxY - a.minY) * (int)(a.maxZ - a.minZ) > maxBlocks; //FIXME bit hacky with floating point calculations.
            aabbs.add(new ColoredAABB(curCaptureAABB, tooBig ? 0xFF0000 : 0xFFFF00));

            capturer.addBlacklistAABBs(aabbs);
            return aabbs;
        } else {
            return Collections.singleton(new ColoredAABB(new AxisAlignedBB(getHoveredPos()), 0x0000FF));
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        if(!world.isRemote && world.getTotalWorldTime() % 20 == 0 && entity instanceof EntityPlayer) {
            getCap(stack).onInterval(world, (EntityPlayer)entity);
        }
        if(world.isRemote) {
            if(getCap(stack).getCapturer() != null) { //If capturing
                NetworkHandler.sendToServer(new PacketUpdateHoveredPos(getHoveredPos())); //Sync the hovered pos.
            }
        }
    }

    private int getRepeatAmount(ItemStack stack){
        NBTTagCompound tag = stack.getOrCreateSubCompound("wand");
        if(tag.hasKey("repeatAmount")) {
            return tag.getInteger("repeatAmount");
        } else {
            return 1;
        }
    }

    private void setRepeatAmount(EntityPlayer player, ItemStack stack, int amount){
        stack.getOrCreateSubCompound("wand").setInteger("repeatAmount", amount);
        player.sendStatusMessage(new TextComponentString("Repeating: x" + amount), false); //TODO language table
    }

    public void incRepeatAmount(EntityPlayer player, ItemStack stack){
        setRepeatAmount(player, stack, getRepeatAmount(stack) + 1);
    }

    public void decRepeatAmount(EntityPlayer player, ItemStack stack){
        int repeatAmount = getRepeatAmount(stack);
        if(repeatAmount > 1) {
            setRepeatAmount(player, stack, repeatAmount - 1);
        }
    }
}
