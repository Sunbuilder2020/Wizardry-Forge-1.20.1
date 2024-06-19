package net.sunbuilder2020.wizardry.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
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
import net.sunbuilder2020.wizardry.client.ClientSpellsData;
import net.sunbuilder2020.wizardry.client.screen.renderComponent.SpellGuiArrowComponent;
import net.sunbuilder2020.wizardry.client.screen.renderComponent.SpellRenderComponent;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

import java.util.ArrayList;
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
    private List<SpellRenderComponent> spellRenderComponents = new ArrayList<>();
    private List<SpellRenderComponent> activeSpellRenderComponents = new ArrayList<>();
    private SpellRenderComponent draggingSpell = null;

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

        List<AbstractSpell> activeSpells = ClientSpellsData.getActiveSpells();
        renderActiveSpells(pGuiGraphics, activeSpells, centerX, centerY, pMouseX, pMouseY, pPartialTick);

        renderGuiArrows(pGuiGraphics, pMouseX, pMouseY, pPartialTick, centerX, centerY);

        renderDraggingSpell(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void renderSpellPages(GuiGraphics pGuiGraphics, List<AbstractSpell> spells, int centerX, int centerY, int mouseX, int mouseY, float partialTick) {
        if (spellRenderComponents != null && !spellRenderComponents.isEmpty()) spellRenderComponents.clear();

        int spells_per_page = SPELLS_PER_ROW * SPELL_ROWS_PER_PAGE * 2;
        int spell_index = activePage * spells_per_page;

        for (int i = -1; i <= 1; i += 2) {
            for (int y = 0; y < SPELL_ROWS_PER_PAGE; y++) {
                for (int x = 0; x < SPELLS_PER_ROW; x++) {
                    if (spell_index > spells.size() - 1) break;
                    if (spells.get(spell_index) == null || spells.get(spell_index) instanceof NoneSpell) continue;

                    int spellX = centerX - SPELLS_AREA_START_X - SPELL_WIDTH * (SPELLS_PER_ROW - x) - SPELLS_DISTANCE_X * (SPELLS_PER_ROW - x - 1);

                    if (i == 1) {
                        spellX = centerX + SPELLS_AREA_START_X + SPELL_WIDTH * x + SPELLS_DISTANCE_X * x;
                    }

                    int spellY = centerY - SPELLS_AREA_START_Y + SPELL_HEIGHT * (y + 1) + SPELLS_DISTANCE_Y * y;

                    AbstractSpell playerSpell = spells.get(spell_index);
                    float opacity = ClientSpellsData.getActiveSpells().contains(playerSpell) ? 0.8F : 1.0F;
                    if (draggingSpell != null && draggingSpell.getSpell().equals(playerSpell)) {
                        opacity = 0.8F;
                    }

                    SpellRenderComponent spell = new SpellRenderComponent(playerSpell, SPELL_WIDTH, SPELL_HEIGHT, spellX, spellY, opacity);
                    spellRenderComponents.add(spell);
                    spell_index++;
                }
            }
        }

        for (SpellRenderComponent component : spellRenderComponents) {
            component.render(pGuiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void renderActiveSpells(GuiGraphics pGuiGraphics, List<AbstractSpell> activeSpells, int centerX, int centerY, int mouseX, int mouseY, float partialTick) {
        if (activeSpells.isEmpty()) return;
        if (activeSpellRenderComponents != null && !activeSpellRenderComponents.isEmpty()) activeSpellRenderComponents.clear();

        int spellY = centerY + ACTIVE_SPELL_OFFSET_Y;
        int max_active_spells = 8;

        for (int i = 0; i < max_active_spells; i++) {
            int spellX = centerX - SPELLS_AREA_START_X - SPELL_WIDTH * (max_active_spells / 2 - i) - SPELLS_DISTANCE_X * (max_active_spells / 2 - i - 1);
            if (i >= max_active_spells / 2) {
                spellX = centerX + SPELLS_AREA_START_X + SPELL_WIDTH * (i - max_active_spells / 2) + SPELLS_DISTANCE_X * (i - max_active_spells / 2);
            }

            AbstractSpell spell = i < activeSpells.size() ? activeSpells.get(i) : SpellRegistry.noneSpell();
            SpellRenderComponent spellComponent = new SpellRenderComponent(spell, SPELL_WIDTH, SPELL_HEIGHT, spellX, spellY, 1.0f);
            activeSpellRenderComponents.add(spellComponent);
            spellComponent.render(pGuiGraphics, mouseX, mouseY, partialTick);

            if (spellComponent.isMouseOver(mouseX, mouseY) && draggingSpell != null && draggingSpell.isDragging() && spell == null) {
                ClientSpellsData.setActiveSpell(draggingSpell.getSpell(), i);
                ClientSpellsData.trimActiveSpells();
                ClientSpellsData.switchToValidSpellSlot();
                ClientSpellsData.syncData();

                draggingSpell.stopDragging();
                draggingSpell = null;
            }
        }
    }

    private void renderDraggingSpell(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (draggingSpell != null) {
            draggingSpell.setPosition(mouseX, mouseY);
            draggingSpell.render(guiGraphics, mouseX, mouseY, partialTick);
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

            if (draggingSpell == null) {
                boolean isShiftDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT);

                for (SpellRenderComponent component : spellRenderComponents) {
                    if (component == null || component.getSpell() == null || component.getSpell() instanceof NoneSpell)
                        continue;

                    if (component.isMouseOver((int) mouseX, (int) mouseY) && !ClientSpellsData.getActiveSpells().contains(component.getSpell())) {
                        if (isShiftDown) {
                            for (SpellRenderComponent activeComponent : activeSpellRenderComponents) {
                                if (activeComponent.getSpell() == null || activeComponent.getSpell() instanceof NoneSpell) {
                                    int index =  activeSpellRenderComponents.indexOf(activeComponent);
                                    activeSpellRenderComponents.set(index, component);

                                    ClientSpellsData.setActiveSpell(component.getSpell(), index);
                                    ClientSpellsData.trimActiveSpells();
                                    ClientSpellsData.switchToValidSpellSlot();
                                    ClientSpellsData.syncData();

                                    break;
                                }
                            }

                            return true;
                        }

                        draggingSpell = component;
                        draggingSpell.startDragging((int) mouseX, (int) mouseY);

                        return true;
                    }
                }

                for (SpellRenderComponent component : activeSpellRenderComponents) {
                    if (component == null || component.getSpell() == null || component.getSpell() instanceof NoneSpell)
                        continue;

                    if (component.isMouseOver((int) mouseX, (int) mouseY)) {
                        if (isShiftDown) {
                            int index = activeSpellRenderComponents.indexOf(component);
                            activeSpellRenderComponents.set(index, null);

                            ClientSpellsData.setActiveSpell(SpellRegistry.noneSpell(), index);
                            ClientSpellsData.trimActiveSpells();
                            ClientSpellsData.switchToValidSpellSlot();
                            ClientSpellsData.syncData();

                            return true;
                        }

                        draggingSpell = component;
                        draggingSpell.startDragging((int) mouseX, (int) mouseY);

                        int index = activeSpellRenderComponents.indexOf(component);
                        activeSpellRenderComponents.set(index, null);

                        ClientSpellsData.setActiveSpell(SpellRegistry.noneSpell(), index);
                        ClientSpellsData.trimActiveSpells();
                        ClientSpellsData.switchToValidSpellSlot();
                        ClientSpellsData.syncData();

                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingSpell != null) {
            SpellRenderComponent overActiveSlot = isMouseOverActiveSlots((int) mouseX, (int) mouseY);
            if (overActiveSlot != null) {
                int index = activeSpellRenderComponents.indexOf(overActiveSlot);
                activeSpellRenderComponents.set(index, draggingSpell);

                ClientSpellsData.setActiveSpell(draggingSpell.getSpell(), index);
                ClientSpellsData.trimActiveSpells();
                ClientSpellsData.switchToValidSpellSlot();
                ClientSpellsData.syncData();
            }

            draggingSpell.stopDragging();
            draggingSpell = null;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private SpellRenderComponent isMouseOverActiveSlots(int mouseX, int mouseY) {
        // Check if the mouse is over any of the active spell slots
        for (SpellRenderComponent component : activeSpellRenderComponents) {
            if (component != null && component.isMouseOver(mouseX, mouseY)) {
                return component;
            }
        }
        return null;
    }

    public void nextPage() {
        activePage += 1;
    }

    public void previousPage() {
        activePage = Math.max(activePage - 1, 0);
    }
}
