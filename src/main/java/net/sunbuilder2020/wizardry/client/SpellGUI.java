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

        int spellWidth = 24;
        int spellHeight = 32;

        int spellsDistanceX = 6;
        int spellsDistanceY = 9;

        int spellsAreaX = (spellWidth + spellsDistanceX) * 4 - spellsDistanceX;
        int spellsAreaY = (spellHeight + spellsDistanceY) * 3 - spellsDistanceY;

        int spellsAreaStartX = 11;
        int spellsAreaStartY = 85;

        int spellX = x - spellsAreaStartX - spellsAreaX;
        int spellY = y - spellsAreaStartY;

        int spellsPage = -1;

        for (AbstractSpell spell : spells) {
            SpellRenderComponent spellRenderComponent = new SpellRenderComponent(spell.getSpellResource(), spellWidth, spellHeight, spellX, spellY);
            spellRenderComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

            spellX += spellWidth + spellsDistanceX;
            if (spellsPage == -1) {
                if (spellX + spellWidth > x - spellsAreaStartX) {
                    spellX = x - spellsAreaStartX - spellsAreaX;
                    spellY += spellHeight + spellsDistanceY;
                    if (spellY + spellHeight > y - spellsAreaStartY + spellsAreaY) {
                        spellsPage = 1;
                        spellX = x + spellsAreaStartX;
                        spellY = y - spellsAreaStartY;
                    }
                }
            }else if (spellsPage == 1) {
                if (spellX + spellWidth > x + spellsAreaStartX + spellsAreaX) {
                    spellX = x + spellsAreaStartX;
                    spellY += spellHeight + spellsDistanceY;
                    if (spellY + spellHeight > y + spellsAreaStartY + spellsAreaY) {
                        break;
                    }
                }
            }
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}

