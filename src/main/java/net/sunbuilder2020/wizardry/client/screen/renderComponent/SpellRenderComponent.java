package net.sunbuilder2020.wizardry.client.screen.renderComponent;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpellRenderComponent implements Renderable {
    private AbstractSpell spell;
    private final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_background.png");
    private int width;
    private int height;
    private int posX;
    private int posY;
    private float opacity;

    private boolean isDragging = false;
    private int dragOffsetX;
    private int dragOffsetY;

    public SpellRenderComponent(AbstractSpell spell, int width, int height, int posX, int posY, float opacity) {
        this.spell = spell;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.opacity = opacity;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (spell != null && !(spell instanceof NoneSpell)) {
            ResourceLocation spellIcon = spell.getSpellIconResource();
            MutableComponent spellName = spell.getDisplayName();

            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

            // Set opacity for background texture
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);

            RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
            pGuiGraphics.blit(BACKGROUND_LOCATION, this.posX, this.posY, 0, 0, this.width, this.height, this.width, this.height);

            float scaleX = (float) this.width / 24.0f;
            float scaleY = (float) this.height / 32.0f;

            int iconWidth = (int) (14 * scaleX);
            int iconHeight = (int) (14 * scaleY);
            int iconX = (int) (this.posX + (5 * scaleX));
            int iconY = (int) (this.posY + (5 * scaleY));

            // Set opacity for spell icon
            RenderSystem.setShaderTexture(0, spellIcon);
            pGuiGraphics.blit(spellIcon, iconX, iconY, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);

            RenderSystem.disableBlend();

            if (!isDragging) {
                displayTooltip(pGuiGraphics, spell, pMouseX, pMouseY);
            }

            float desiredTextHeight = this.height / 15f;
            int textHeight = Minecraft.getInstance().font.lineHeight;
            float scale = desiredTextHeight / textHeight;

            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(scale, scale, scale);

            int adjustedX = (int) ((this.posX + this.width * 0.5) / scale);
            int adjustedY = (int) ((this.posY + this.height * 0.8) / scale);

            write(pGuiGraphics, spellName, adjustedX, adjustedY);

            pGuiGraphics.pose().popPose();
            pGuiGraphics.pose().popPose();
        }
    }

    public void displayTooltip(GuiGraphics pGuiGraphics, AbstractSpell spell, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            Font fontRenderer = Minecraft.getInstance().font;

            MutableComponent spellName = spell.getDisplayName();
            List<MutableComponent> spellUniqueInfo = spell.getUniqueInfo();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(spellName);
            tooltip.add(Component.empty());
            tooltip.addAll(spellUniqueInfo);

            pGuiGraphics.renderComponentTooltip(fontRenderer, tooltip, mouseX, mouseY);
        }
    }

    public void write(GuiGraphics pGuiGraphics, MutableComponent text, int x, int y) {
        Font fontRenderer = Minecraft.getInstance().font;
        pGuiGraphics.drawCenteredString(fontRenderer, text.getVisualOrderText(), x, y, 0xFFFFFF);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.posX && mouseX <= this.posX + this.width &&
                mouseY >= this.posY && mouseY <= this.posY + this.height;
    }

    public void startDragging(int mouseX, int mouseY) {
        this.isDragging = true;
        this.dragOffsetX = mouseX - this.posX;
        this.dragOffsetY = mouseY - this.posY;
    }

    public void stopDragging() {
        this.isDragging = false;
    }

    public void setPosition(int mouseX, int mouseY) {
        if (isDragging) {
            this.posX = mouseX - this.dragOffsetX;
            this.posY = mouseY - this.dragOffsetY;
        }
    }

    public boolean isDragging() {
        return isDragging;
    }

    public AbstractSpell getSpell() {
        return spell;
    }
}
