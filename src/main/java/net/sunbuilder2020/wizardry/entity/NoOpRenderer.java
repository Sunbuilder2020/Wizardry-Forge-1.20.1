package net.sunbuilder2020.wizardry.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NoOpRenderer<T extends Entity> extends EntityRenderer<T> {
    public NoOpRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {
    }
}
