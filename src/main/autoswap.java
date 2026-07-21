package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class AutoSwap implements ClientModInitializer {
    private static KeyBinding swapKey;

    @Override
    public void onInitializeClient() {
        swapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autoswap.swap",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.autoswap.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && swapKey != null && swapKey.wasPressed()) {
                swapToHand(1);
            }
        });
    }

    public static void swapToHand(int targetSlot) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return;

        PlayerInventory inventory = client.player.getInventory();
        int currentSlot = inventory.selectedSlot;

        if (currentSlot == targetSlot) return;

        int syncId = client.player.playerScreenHandler.syncId;

        client.interactionManager.clickSlot(
                syncId,
                targetSlot < 9 ? targetSlot + 36 : targetSlot,
                currentSlot,
                SlotActionType.SWAP,
                client.player
        );
    }
}
