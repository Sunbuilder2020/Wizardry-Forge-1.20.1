package net.sunbuilder2020.wizardry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

public class SpellsHudOverlay {
    public static final IGuiOverlay Hud_SPELLS = (SpellsHudOverlay::render);

    private static final ResourceLocation arrowLeft = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/arrow_left.png");
    private static final ResourceLocation arrowRight = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/arrow_right.png");

    private static void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        Player player = Minecraft.getInstance().player;
        AbstractSpell activeSpell = ClientSpellsData.getActiveSpell();

        if (player == null || player.isSpectator() || activeSpell == null) {
            return;
        }

        ResourceLocation activeSpellResource = activeSpell.getSpellIconResource();

        int x = width / 2;
        int y = height;

        int textureWidth = 15;
        int textureHeight = 15;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, arrowLeft);
        guiGraphics.blit(arrowLeft, x - 132, y - textureHeight - 3, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        RenderSystem.setShaderTexture(0, arrowRight);
        guiGraphics.blit(arrowRight, x - 108, y - textureHeight - 3, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        RenderSystem.setShaderTexture(0, activeSpellResource);
        guiGraphics.blit(activeSpellResource, x - 120, y - textureHeight - 3, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
}
