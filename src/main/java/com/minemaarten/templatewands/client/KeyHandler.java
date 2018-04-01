package com.minemaarten.templatewands.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import org.lwjgl.input.Keyboard;

import com.minemaarten.templatewands.items.ItemTemplateWand;
import com.minemaarten.templatewands.network.NetworkHandler;
import com.minemaarten.templatewands.network.PacketChangeRepeatAmount;
import com.minemaarten.templatewands.network.PacketTogglePlacementLock;

public class KeyHandler{
    private static final String DESCRIPTION_INC_WAND_REPEAT = "templatewands.template_wand.inc_repeat";
    private static final String DESCRIPTION_DEC_WAND_REPEAT = "templatewands.template_wand.dec_repeat";
    private static final String DESCRIPTION_LOCK_WAND_PLACEMENT = "templatewands.template_wand.lock_placement";
    private static final String CATEGORY = "key.templatewands.category";

    private static KeyHandler INSTANCE = new KeyHandler();

    public KeyBinding keybindIncRepeat;
    public KeyBinding keybindDecRepeat;
    public KeyBinding keybindLockPlacement;
    private final List<KeyBinding> keys = new ArrayList<>();

    public static KeyHandler getInstance(){
        return INSTANCE;
    }

    private KeyHandler(){
        keybindIncRepeat = registerKeyBinding(new KeyBinding(KeyHandler.DESCRIPTION_INC_WAND_REPEAT, Keyboard.KEY_ADD, CATEGORY));
        keybindDecRepeat = registerKeyBinding(new KeyBinding(KeyHandler.DESCRIPTION_DEC_WAND_REPEAT, Keyboard.KEY_SUBTRACT, CATEGORY));
        keybindLockPlacement = registerKeyBinding(new KeyBinding(KeyHandler.DESCRIPTION_LOCK_WAND_PLACEMENT, Keyboard.KEY_L, CATEGORY));
    }

    private KeyBinding registerKeyBinding(KeyBinding keyBinding){
        ClientRegistry.registerKeyBinding(keyBinding);
        keys.add(keyBinding);
        return keyBinding;
    }

    @SubscribeEvent
    public void onKey(KeyInputEvent event){
        for(KeyBinding key : keys) {
            if(key.isPressed()) {
                onKey(key);
            }
        }
    }

    public void onKey(KeyBinding keybinding){
        if(keybinding == keybindIncRepeat) {
            NetworkHandler.sendToServer(new PacketChangeRepeatAmount(true));
        } else if(keybinding == keybindDecRepeat) {
            NetworkHandler.sendToServer(new PacketChangeRepeatAmount(false));
        } else if(keybinding == keybindLockPlacement) {
            NetworkHandler.sendToServer(new PacketTogglePlacementLock(ItemTemplateWand.getHoveredPos(), Minecraft.getMinecraft().player.getHorizontalFacing()));
        }
    }

}
