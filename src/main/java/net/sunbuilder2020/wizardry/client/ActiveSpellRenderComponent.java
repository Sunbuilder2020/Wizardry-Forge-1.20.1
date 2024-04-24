package net.sunbuilder2020.wizardry.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;

@OnlyIn(Dist.CLIENT)
public class ActiveSpellRenderComponent implements Renderable {
    private AbstractSpell spell;
    private final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/active_spell_background.png");
    private int width;
    private int height;
    private int posX;
    private int posY;

    public ActiveSpellRenderComponent(AbstractSpell spell, int width, int height, int posX, int posY) {
        this.spell = spell;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

        pGuiGraphics.blit(BACKGROUND_LOCATION, this.posX, this.posY, 0, 0, this.width, this.height, this.width, this.height);

        if (spell != null) {
            SpellRenderComponent spellRenderComponent = new SpellRenderComponent(spell.getSpellResource(), this.width, this.height, this.posX, this.posY);
            spellRenderComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        pGuiGraphics.pose().popPose();
    }
}
