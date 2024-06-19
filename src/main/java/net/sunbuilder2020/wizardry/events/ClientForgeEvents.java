package net.sunbuilder2020.wizardry.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.client.ClientCastingData;
import net.sunbuilder2020.wizardry.client.ClientSpellsData;
import net.sunbuilder2020.wizardry.client.screen.ClientSpellCooldownsData;
import net.sunbuilder2020.wizardry.client.screen.SpellGUI;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.util.KeyBinding;

import java.util.List;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (KeyBinding.KEY_MAPPING_OPEN_SPELL_GUI.consumeClick()) {
            Minecraft.getInstance().setScreen(new SpellGUI());
        }

        if (KeyBinding.KEY_MAPPING_SWITCH_SPELL_LEFT.consumeClick()) {
            switchSpellSlot(-1);
        }

        if (KeyBinding.KEY_MAPPING_SWITCH_SPELL_RIGHT.consumeClick()) {
            switchSpellSlot(1);
        }

        ClientCastingData.tick();
        ClientSpellCooldownsData.tick();
    }

    /**
     * @param amount should only be a value of either -1 or 1
     */
    public static void switchSpellSlot(int amount) {
        if (!(amount == 1 || amount == -1)) return;

        int activeSpellSlot = ClientSpellsData.getActiveSpellSlot();
        List<AbstractSpell> activeSpells = ClientSpellsData.getActiveSpells();

        if (activeSpells.isEmpty()) {
            return;
        }

        int originalSlot = activeSpellSlot;
        do {
            activeSpellSlot += amount;

            if (activeSpellSlot < 0) {
                activeSpellSlot = activeSpells.size() - 1;
            } else if (activeSpellSlot >= activeSpells.size()) {
                activeSpellSlot = 0;
            }

            if (activeSpellSlot == originalSlot) {
                break;
            }
        } while (SpellRegistry.isNoneSpell(activeSpells.get(activeSpellSlot)));

        ClientSpellsData.setActiveSpellSlot(activeSpellSlot);
        ClientSpellsData.syncData();
    }
}