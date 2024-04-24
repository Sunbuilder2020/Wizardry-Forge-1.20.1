package net.sunbuilder2020.wizardry.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpellGUI extends Screen {
    private final ResourceLocation spellGUIScreen = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_gui.png");
    private int activePage = 0;
    private SpellGuiArrowComponent arrowLeft;
    private SpellGuiArrowComponent arrowRight;

    public SpellGUI() {
        super(Component.translatable("gui.wizardry.spell_gui"));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.width = 331;
        this.height = 246;

        int x = pGuiGraphics.guiWidth() / 2;
        int y = pGuiGraphics.guiHeight() / 2;

        pGuiGraphics.blit(spellGUIScreen, x - this.width / 2, y - this.height / 2, 0, 0, this.width, this.height, this.width, this.height);

        List<AbstractSpell> spells = ClientSpellsData.getSpells();

        int spellsAmountX = 4;
        int spellsAmountY = 3;

        int spellWidth = 24;
        int spellHeight = 32;

        int spellsDistanceX = 6;
        int spellsDistanceY = 9;

        int spellsAreaX = (spellWidth + spellsDistanceX) * spellsAmountX - spellsDistanceX;
        int spellsAreaY = (spellHeight + spellsDistanceY) * spellsAmountY - spellsDistanceY;

        int spellsAreaStartX = 11;
        int spellsAreaStartY = 85;

        int spellsPerSide = 4 * 3;
        int pages = (int) Math.ceil(spells.size() / (spellsPerSide * 2));

        int startIndexLeft = activePage * spellsPerSide * 2;
        int endIndexLeft = Math.min(startIndexLeft + spellsPerSide, spells.size());
        int startIndexRight = endIndexLeft;
        int endIndexRight = Math.min(startIndexRight + spellsPerSide, spells.size());

        renderSpells(pGuiGraphics, spells, spellWidth, spellHeight, spellsDistanceX, spellsDistanceY, x, y, spellsAreaStartX, spellsAreaStartY, spellsAreaX, spellsAreaY, startIndexLeft, endIndexLeft, true, pMouseX, pMouseY, pPartialTick);

        renderSpells(pGuiGraphics, spells, spellWidth, spellHeight, spellsDistanceX, spellsDistanceY, x, y, spellsAreaStartX, spellsAreaStartY, spellsAreaX, spellsAreaY, startIndexRight, endIndexRight, false, pMouseX, pMouseY, pPartialTick);

        List<AbstractSpell> activeSpells = ClientSpellsData.getActiveSpells();

        int activeSpellAreaX = this.width - 80;
        int activeSpellAreaStartX = x - this.width / 2 + 40;
        int activeSpellY = this.height - 40 - spellHeight;

        renderActiveSpells(pGuiGraphics, activeSpells, spellWidth, spellHeight, activeSpellAreaX, activeSpellY, activeSpellAreaStartX, pMouseX, pMouseY, pPartialTick);

        if (this.activePage > 0) {
            this.arrowLeft = new SpellGuiArrowComponent(true, 18, 18, x - this.width / 2 + 55 - 18, y + this.height / 2 - 50);

            arrowLeft.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        if (this.activePage < pages)  {
            this.arrowRight = new SpellGuiArrowComponent(false, 18, 18, x + this.width / 2 - 55, y + this.height / 2 - 50);

            arrowRight.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void renderSpells(GuiGraphics pGuiGraphics, List<AbstractSpell> spells, int spellWidth, int spellHeight, int spellsDistanceX, int spellsDistanceY, int x, int y, int spellsAreaStartX, int spellsAreaStartY, int spellsAreaX, int spellsAreaY, int startIndex, int endIndex, boolean isLeftSide, int mouseX, int mouseY, float partialTick) {
        int columnCounter = 0;
        int rowCounter = 0;

        int spellX = isLeftSide ? x - spellsAreaStartX - spellsAreaX : x + spellsAreaStartX;
        int spellY = y - spellsAreaStartY;

        for (int i = startIndex; i < endIndex; i++) {
            AbstractSpell spell = spells.get(i);
            SpellRenderComponent spellRenderComponent = new SpellRenderComponent(spell.getSpellResource(), spellWidth, spellHeight, spellX, spellY);
            spellRenderComponent.render(pGuiGraphics, mouseX, mouseY, partialTick);

            spellX += spellWidth + spellsDistanceX;
            columnCounter++;

            if (columnCounter >= 4) {
                columnCounter = 0;
                rowCounter++;
                spellX = isLeftSide ? x - spellsAreaStartX - spellsAreaX : x + spellsAreaStartX;
                spellY += spellHeight + spellsDistanceY;
            }

            if (rowCounter >= 3) {
                break;
            }
        }
    }

    private void renderActiveSpells(GuiGraphics pGuiGraphics, List<AbstractSpell> activeSpells, int spellWidth, int spellHeight, int spellsAreaX, int spellY, int spellsAreaStartX, int mouseX, int mouseY, float partialTick) {
        for (int i = 0; i < 10; i++) {
            int spellX = spellsAreaX / 10 * i + spellsAreaStartX;
            AbstractSpell spell = activeSpells.get(i);

            ActiveSpellRenderComponent activeSpellRenderComponent = new ActiveSpellRenderComponent(spell, spellWidth, spellHeight, spellX, spellY);
            activeSpellRenderComponent.render(pGuiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // 0 is the button ID for the left mouse click
            Player player = Minecraft.getInstance().player;

            if (arrowLeft != null && arrowLeft.isMouseOver(mouseX, mouseY)) {
                previousPage();
                player.playSound(SoundEvents.BOOK_PAGE_TURN);

                return true;
            } else if (arrowRight != null && arrowRight.isMouseOver(mouseX, mouseY)) {
                nextPage();
                player.playSound(SoundEvents.BOOK_PAGE_TURN);

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void nextPage() {
        activePage++;
    }

    public void previousPage() {
        activePage = Math.max(activePage - 1, 0);
    }
}

