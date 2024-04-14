package net.sunbuilder2020.wizardry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

public class SpellsHudOverlay {
    public static final IGuiOverlay Hud_SPELLS = (SpellsHudOverlay::render);

    private static final ResourceLocation ARROW_LEFT = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/arrow_left.png");
    private static final ResourceLocation ARROW_RIGHT = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/arrow_right.png");
    private static final ResourceLocation ICON_OVERLAY = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_hud_icon_overlay.png");

    private static void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        Player player = Minecraft.getInstance().player;
        String activeSpellID = ClientSpellsData.getActiveSpells().get(ClientSpellsData.getActiveSpellSlot());
        AbstractSpell activeSpell = SpellRegistry.getSpell(activeSpellID);

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

        RenderSystem.setShaderTexture(0, ARROW_LEFT);
        guiGraphics.blit(ARROW_LEFT, x - 136, y - textureHeight - 3, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        RenderSystem.setShaderTexture(0, ARROW_RIGHT);
        guiGraphics.blit(ARROW_RIGHT, x - 108, y - textureHeight - 3, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        RenderSystem.setShaderTexture(0, activeSpellResource);
        guiGraphics.blit(activeSpellResource, x - 122, y - textureHeight - 3, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        RenderSystem.setShaderTexture(0, ICON_OVERLAY);
        guiGraphics.blit(ICON_OVERLAY, x - 124, y - textureHeight - 5, 0, 0, textureWidth + 4, textureHeight + 4, textureWidth + 4, textureHeight + 4);
    }
}
