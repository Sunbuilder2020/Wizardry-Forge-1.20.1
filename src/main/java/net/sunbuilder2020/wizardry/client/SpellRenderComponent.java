package net.sunbuilder2020.wizardry.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

public class SpellRenderComponent implements Renderable {
    private ResourceLocation spellLocation;
    private final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_background.png");
    private int width;
    private int height;
    private int posX;
    private int posY;

    public SpellRenderComponent(ResourceLocation spellLocation, int width, int height, int posX, int posY) {
        this.spellLocation = spellLocation;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        AbstractSpell spell = SpellRegistry.getSpell(this.spellLocation);
        if (spell != null) {
            ResourceLocation spellIcon = spell.getSpellIconResource();
            MutableComponent spellName = spell.getDisplayName();

            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

            pGuiGraphics.blit(BACKGROUND_LOCATION, this.posX, this.posY, 0, 0, this.width, this.height, this.width, this.height);

            float scaleX = (float)this.width / 24.0f;
            float scaleY = (float)this.height / 32.0f;

            int iconWidth = (int)(14 * scaleX);
            int iconHeight = (int)(14 * scaleY);
            int iconX = (int) (this.posX + (5 * scaleX));
            int iconY = (int) (this.posY + (5 * scaleY));

            // Draw the spell icon
            pGuiGraphics.blit(spellIcon, iconX, iconY, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);

            float desiredTextHeight = this.height / 15f;
            int textHeight = Minecraft.getInstance().font.lineHeight;
            float scale = desiredTextHeight / textHeight;

            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(scale, scale, scale);

            int adjustedX = (int) ((this.posX + this.width * 0.2) / scale);
            int adjustedY = (int) ((this.posY + this.height * 0.8) / scale);

            write(pGuiGraphics, spellName, adjustedX, adjustedY);

            pGuiGraphics.pose().popPose();
        }
        pGuiGraphics.pose().popPose();
    }

    public void write(GuiGraphics pGuiGraphics, MutableComponent text, int x, int y) {
        Font fontRenderer = Minecraft.getInstance().font;
        pGuiGraphics.drawString(fontRenderer, text.getVisualOrderText(), x, y, 0xFFFFFF);
    }
}
