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

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpellGUI extends Screen {
    private static final ResourceLocation SPELL_GUI_SCREEN = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_gui.png");
    private static final int GUI_WIDTH = 280;
    private static final int GUI_HEIGHT = 214;

    private static final int SPELL_WIDTH = 24;
    private static final int SPELL_HEIGHT = 32;
    private static final int SPELLS_PER_ROW = 4;
    private static final int SPELL_ROWS_PER_PAGE = 3;
    private static final int SPELLS_DISTANCE_X = 6;
    private static final int SPELLS_DISTANCE_Y = 9;
    private static final int SPELLS_AREA_START_X = 11;
    private static final int SPELLS_AREA_START_Y = 127;

    private static final int ACTIVE_SPELL_OFFSET_Y = 42;

    private int activePage = 0;
    private SpellGuiArrowComponent arrowLeft;
    private SpellGuiArrowComponent arrowRight;

    public SpellGUI() {
        super(Component.translatable("gui.wizardry.spell_gui"));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.width = GUI_WIDTH;
        this.height = GUI_HEIGHT;
        int centerX = pGuiGraphics.guiWidth() / 2;
        int centerY = pGuiGraphics.guiHeight() / 2;

        pGuiGraphics.blit(SPELL_GUI_SCREEN, centerX - this.width / 2, centerY - this.height / 2, 0, 0, this.width, this.height, this.width, this.height);

        List<AbstractSpell> spells = ClientSpellsData.getSpells();

        renderSpellPages(pGuiGraphics, spells, centerX, centerY, pMouseX, pMouseY, pPartialTick);

        renderActiveSpells(pGuiGraphics, ClientSpellsData.getActiveSpells(), centerX, centerY, pMouseX, pMouseY, pPartialTick);

        renderGuiArrows(pGuiGraphics, pMouseX, pMouseY, pPartialTick, centerX, centerY);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void renderSpellPages(GuiGraphics pGuiGraphics, List<AbstractSpell> spells, int centerX, int centerY, int mouseX, int mouseY, float partialTick) {
        int spells_per_page = SPELLS_PER_ROW * SPELL_ROWS_PER_PAGE * 2;
        int spell_index = activePage * spells_per_page;

        for (int i = -1; i <= 1; i += 2) {
            for (int y = 0; y < SPELL_ROWS_PER_PAGE; y++) {
                for (int x = 0; x < SPELLS_PER_ROW; x++) {
                    if (spell_index > spells.size() - 1) break;

                    int spellX = centerX - SPELLS_AREA_START_X - SPELL_WIDTH * (SPELLS_PER_ROW - x) - SPELLS_DISTANCE_X * (SPELLS_PER_ROW - x - 1);

                    if (i == 1) {
                        spellX = centerX + SPELLS_AREA_START_X + SPELL_WIDTH * x + SPELLS_DISTANCE_X * x;
                    }

                    int spellY = centerY - SPELLS_AREA_START_Y + SPELL_HEIGHT * (y + 1) + SPELLS_DISTANCE_Y * y;

                    SpellRenderComponent spell = new SpellRenderComponent(spells.get(spell_index), SPELL_WIDTH, SPELL_HEIGHT, spellX, spellY);
                    spell.render(pGuiGraphics, mouseX, mouseY, partialTick);

                    spell_index++;
                }
            }
        }
    }

    private void renderActiveSpells(GuiGraphics pGuiGraphics, List<AbstractSpell> activeSpells, int centerX, int centerY, int mouseX, int mouseY, float partialTick) {
        if (activeSpells.isEmpty()) return;

        int spellY = centerY + ACTIVE_SPELL_OFFSET_Y;
        int spell_index = 0;
        int max_active_spells = 8;

        for (int i = -1; i <= 1; i += 2) {
            for (int x = 0; x < max_active_spells / 2; x++) {
                if (spell_index > activeSpells.size() - 1) break;

                int spellX = centerX - SPELLS_AREA_START_X - SPELL_WIDTH * (max_active_spells / 2 - x) - SPELLS_DISTANCE_X * (max_active_spells / 2 - x - 1);

                if (i == 1) {
                    spellX = centerX + SPELLS_AREA_START_X + SPELL_WIDTH * x + SPELLS_DISTANCE_X * x;
                }

                SpellRenderComponent spell = new SpellRenderComponent(activeSpells.get(spell_index), SPELL_WIDTH, SPELL_HEIGHT, spellX, spellY);
                spell.render(pGuiGraphics, mouseX, mouseY, partialTick);

                spell_index++;
            }
        }
    }

    private void renderGuiArrows(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, int centerX, int centerY) {
        if (this.activePage > 0) {
            this.arrowLeft = new SpellGuiArrowComponent(true, 18, 18, centerX - this.width / 2 + 14, centerY + this.height / 2 - 18 - 10);
            arrowLeft.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        this.arrowRight = new SpellGuiArrowComponent(false, 18, 18, centerX + this.width / 2 - 18 - 14, centerY + this.height / 2 - 18 -10);
        arrowRight.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            Player player = Minecraft.getInstance().player;
            if (arrowLeft != null && arrowLeft.isMouseOver(mouseX, mouseY) && this.activePage > 0) {
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

    @Override
    public void onClose() {
        ClientSpellsData.syncData();

        super.onClose();
    }

    public void nextPage() {
        activePage += 1;
    }

    public void previousPage() {
        activePage = Math.max(activePage - 1, 0);
    }
}