package net.sunbuilder2020.wizardry.client.screen.renderComponent;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.Wizardry;

@OnlyIn(Dist.CLIENT)
public class SpellGuiArrowComponent implements Renderable {
    private boolean isLeft;
    private final ResourceLocation TEXTURE_LOCATION_LEFT = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_gui_arrow_left.png");
    private final ResourceLocation TEXTURE_LOCATION_RIGHT = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_gui_arrow_right.png");
    private final ResourceLocation TEXTURE_LOCATION_LEFT_CLICKED = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_gui_arrow_left_clicked.png");
    private final ResourceLocation TEXTURE_LOCATION_RIGHT_CLICKED = new ResourceLocation(Wizardry.MOD_ID, "textures/gui/spell_gui_arrow_right_clicked.png");
    private int width;
    private int height;
    private int posX;
    private int posY;

    public SpellGuiArrowComponent(boolean isLeft, int width, int height, int posX, int posY) {
        this.isLeft = isLeft;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        ResourceLocation textureLoc = this.isMouseOver(pMouseX, pMouseY) ?
                (this.isLeft ? this.TEXTURE_LOCATION_LEFT_CLICKED : this.TEXTURE_LOCATION_RIGHT_CLICKED) :
                (this.isLeft ? this.TEXTURE_LOCATION_LEFT : this.TEXTURE_LOCATION_RIGHT);

        pGuiGraphics.blit(textureLoc, this.posX, this.posY, 0, 0, this.width, this.height, this.width, this.height);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.posX && mouseX < this.posX + this.width &&
                mouseY >= this.posY && mouseY < this.posY + this.height;
    }
}
