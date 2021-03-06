package com.minemaarten.templatewands.items;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.minemaarten.templatewands.TemplateWands;
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
        setCreativeTab(TemplateWands.creativeTab);
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
        BlockPos lockPos = getPlacementLockPos(stack);
        EnumFacing lockFacing = lockPos != null ? getPlacementLockFacing(stack) : null;
        clearPlacementLock(stack);

        CapabilityTemplateWand cap = getCap(stack);
        if(!player.isSneaking()) {
            if(cap.hasTemplate()) {
                if(lockPos != null) {
                    pos = lockPos;
                    facing = lockFacing;
                }
                cap.place(world, pos, player, facing, getRepeatAmount(stack));
                return EnumActionResult.SUCCESS;
            } else {
                if(cap.registerCoordinate(world, pos, player, maxBlocks)) {
                    player.sendStatusMessage(new TextComponentString("Coordinate registered"), true); //TODO language table
                    return EnumActionResult.SUCCESS;
                } else {
                    player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "Area too big"), true); //TODO language table
                    return EnumActionResult.FAIL;
                }
            }
        } else {
            cap.clearTemplate(player);
            player.sendStatusMessage(new TextComponentString("Wand cleared"), true); //TODO language table
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
    public static BlockPos getHoveredPos(){
        EntityPlayer player = Minecraft.getMinecraft().player;
        RayTraceResult hoveredObj = Minecraft.getMinecraft().objectMouseOver;
        if(hoveredObj != null && hoveredObj.getBlockPos() != null) {
            return hoveredObj.getBlockPos();
        } else {
            return getMidAirPos(player);
        }
    }

    @SideOnly(Side.CLIENT)
    private static BlockPos getMidAirPos(EntityPlayer player){
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
            BlockPos curPos = getPlacementLockPos(stack);
            EnumFacing facing;
            if(curPos != null) {
                facing = getPlacementLockFacing(stack);
            } else {
                EntityPlayer player = Minecraft.getMinecraft().player;
                curPos = getHoveredPos();
                facing = player.getHorizontalFacing();
            }

            Set<ColoredAABB> aabbs = new HashSet<>();
            int repeatAmount = getRepeatAmount(stack);
            for(int i = 0; i < repeatAmount; i++) {
                AxisAlignedBB aabb = template.getAABB(curPos, facing);
                aabbs.add(new ColoredAABB(aabb, 0x00AA00));
                curPos = template.calculateConnectedPos(curPos, facing);
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

    public String getToBeRenderedText(ItemStack stack){
        CapabilityTemplateWand cap = getCap(stack);
        TemplateSurvival template = cap.getTemplate();
        TemplateCapturer capturer = cap.getCapturer();
        if(template != null) {
            return TextFormatting.GREEN + "Right click to place, sneak right click to clear"; //TODO language table
        } else if(capturer != null) {
            BlockPos hoveredPos = getHoveredPos();
            AxisAlignedBB curCaptureAABB = new AxisAlignedBB(hoveredPos, capturer.firstPos).expand(1, 1, 1);
            AxisAlignedBB a = curCaptureAABB;
            int blocks = (int)(a.maxX - a.minX) * (int)(a.maxY - a.minY) * (int)(a.maxZ - a.minZ); //FIXME bit hacky with floating point calculations.
            return (blocks > maxBlocks ? TextFormatting.RED : TextFormatting.YELLOW) + "Right click to finish. Blocks: " + blocks + "/" + (maxBlocks == Integer.MAX_VALUE ? "-" : maxBlocks); //TODO language table
        } else {
            return TextFormatting.BLUE + "Right click to start capturing area"; //TODO language table
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

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Max blocks: " + (maxBlocks == Integer.MAX_VALUE ? "Unlimited" : maxBlocks)); //TODO language table
        tooltip.add("While holding:");
        tooltip.add("Press [+] to increase repeat placement");
        tooltip.add("Press [-] to decrease repeat placement");
        tooltip.add("Press [L] to (un)lock the placement position");
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
        player.sendStatusMessage(new TextComponentString("Repeating: x" + amount), true); //TODO language table
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

    public void togglePlacementLock(EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing facing){
        CapabilityTemplateWand cap = getCap(stack);
        TemplateSurvival template = cap.getTemplate();
        if(template == null) {
            player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "First set a template."), true); //TODO language table
        } else {
            if(getPlacementLockPos(stack) == null) {
                setPlacementLock(stack, pos, facing);
            } else {
                clearPlacementLock(stack);
            }
        }
    }

    private void clearPlacementLock(ItemStack stack){
        setPlacementLock(stack, null, null);
    }

    private void setPlacementLock(ItemStack stack, BlockPos pos, EnumFacing facing){
        NBTTagCompound tag = stack.getOrCreateSubCompound("wand");
        tag.setBoolean("locked", pos != null);
        if(pos != null) {
            tag.setInteger("x", pos.getX());
            tag.setInteger("y", pos.getY());
            tag.setInteger("z", pos.getZ());
            tag.setByte("facing", (byte)facing.ordinal());
        }
    }

    private BlockPos getPlacementLockPos(ItemStack stack){
        NBTTagCompound tag = stack.getOrCreateSubCompound("wand");
        if(tag.getBoolean("locked")) {
            return new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
        } else {
            return null;
        }
    }

    private EnumFacing getPlacementLockFacing(ItemStack stack){
        byte b = stack.getOrCreateSubCompound("wand").getByte("facing");
        return EnumFacing.VALUES[b];
    }
}
