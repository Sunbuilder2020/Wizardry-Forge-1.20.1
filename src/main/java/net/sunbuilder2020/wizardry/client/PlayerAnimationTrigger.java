/*package net.sunbuilder2020.wizardry.client;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.AnimationContainer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class PlayerAnimationTrigger {

    private final AnimationContainer animContainer = new AnimationContainer();

    public void playAnimation(Player player) {
        // Assuming you have your animation loaded as a KeyframeAnimation
        IAnimation myAnimation = loadAnimation(); // You need to implement this

        // Add the animation to the container
        animContainer.setAnim(myAnimation);
    }

    public void tickPlayerAnimation(Player player) {
        if (animContainer.isActive()) {
            animContainer.tick(); // Advance animation frames
        }
    }

    // This should be called from render code
    public void renderPlayerAnimations(Player player) {
        AnimationStack stack = new AnimationStack();
        if (stack != null) {
            stack.addAnimLayer(1, ...);
        }
    }

    private IAnimation loadAnimation() {
        // You can load animations from JSON files or create them programmatically
        // For now, return a dummy animation object
        return new IAnimation() {
        };
    }
}*/
