package net.sunbuilder2020.wizardry.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.client.SpellsHudOverlay;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(Wizardry.MOD_ID + "spells", SpellsHudOverlay.Hud_SPELLS);
    }
}
