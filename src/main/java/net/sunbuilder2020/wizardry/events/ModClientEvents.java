package net.sunbuilder2020.wizardry.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.client.screen.SpellsHudOverlay;
import net.sunbuilder2020.wizardry.entity.ModEntities;
import net.sunbuilder2020.wizardry.entity.NoOpRenderer;
import net.sunbuilder2020.wizardry.util.KeyBinding;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("spells", SpellsHudOverlay.Hud_SPELLS);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.KEY_MAPPING_OPEN_SPELL_GUI);
        event.register(KeyBinding.KEY_MAPPING_SWITCH_SPELL_LEFT);
        event.register(KeyBinding.KEY_MAPPING_SWITCH_SPELL_RIGHT);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HURT_PROJECTILE.get(), NoOpRenderer::new);
    }
}
