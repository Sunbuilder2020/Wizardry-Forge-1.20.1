package net.sunbuilder2020.wizardry.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.ArrayList;
import java.util.List;

public class SpellGUI extends Screen {
    private final ResourceLocation spellGUIScreen = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_gui.png");
    private int activePage = 0;

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

        List<String> spellIDs = ClientSpellsData.getSpells();
        List<AbstractSpell> spells = new ArrayList<>();
        for (String spellID : spellIDs) {
            AbstractSpell spell = SpellRegistry.getSpell(spellID);

            if (spell != null) {
                spells.add(spell);
            }
        }

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

        int startIndexLeft = activePage * spellsPerSide * 2;
        int endIndexLeft = Math.min(startIndexLeft + spellsPerSide, spells.size());
        int startIndexRight = endIndexLeft;
        int endIndexRight = Math.min(startIndexRight + spellsPerSide, spells.size());

        renderSpells(pGuiGraphics, spells, spellWidth, spellHeight, spellsDistanceX, spellsDistanceY, x, y, spellsAreaStartX, spellsAreaStartY, spellsAreaX, spellsAreaY, startIndexLeft, endIndexLeft, true);

        renderSpells(pGuiGraphics, spells, spellWidth, spellHeight, spellsDistanceX, spellsDistanceY, x, y, spellsAreaStartX, spellsAreaStartY, spellsAreaX, spellsAreaY, startIndexRight, endIndexRight, false);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void renderSpells(GuiGraphics pGuiGraphics, List<AbstractSpell> spells, int spellWidth, int spellHeight, int spellsDistanceX, int spellsDistanceY, int x, int y, int spellsAreaStartX, int spellsAreaStartY, int spellsAreaX, int spellsAreaY, int startIndex, int endIndex, boolean isLeftSide) {
        int columnCounter = 0;
        int rowCounter = 0;

        int spellX = isLeftSide ? x - spellsAreaStartX - spellsAreaX : x + spellsAreaStartX;
        int spellY = y - spellsAreaStartY;

        for (int i = startIndex; i < endIndex; i++) {
            AbstractSpell spell = spells.get(i);
            SpellRenderComponent spellRenderComponent = new SpellRenderComponent(spell.getSpellResource(), spellWidth, spellHeight, spellX, spellY);
            spellRenderComponent.render(pGuiGraphics, 0, 0, 0);

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


    public void nextPage() {
        activePage++;
    }

    public void previousPage() {
        activePage = Math.max(activePage - 1, 0);
    }
}

