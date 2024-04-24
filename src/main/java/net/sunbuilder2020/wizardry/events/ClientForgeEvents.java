package net.sunbuilder2020.wizardry.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.client.ClientSpellsData;
import net.sunbuilder2020.wizardry.client.SpellGUI;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.SetSpellsDataC2SPacket;
import net.sunbuilder2020.wizardry.util.KeyBinding;

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
    }

    public static void switchSpellSlot(int amount) {
        if (ClientSpellsData.getActiveSpellIDs().size() > 1) {
            int originalSpellSlot = ClientSpellsData.getActiveSpellSlot();
            int spellSlot = originalSpellSlot;
            int attempts = 0;

            do {
                spellSlot += amount;
                if (spellSlot >= ClientSpellsData.getActiveSpellIDs().size()) {
                    spellSlot = 0;
                } else if (spellSlot < 0) {
                    spellSlot = ClientSpellsData.getActiveSpellIDs().size() - 1;
                }

                if (++attempts > ClientSpellsData.getActiveSpellIDs().size()) {
                    return;
                }
            } while (ClientSpellsData.getActiveSpellInSlot(spellSlot) == null);

            ClientSpellsData.setActiveSpellSlot(spellSlot);
            if (spellSlot != originalSpellSlot) {
                ModMessages.sendToServer(new SetSpellsDataC2SPacket(ClientSpellsData.getSpellIDs(), ClientSpellsData.getActiveSpellIDs(), ClientSpellsData.getActiveSpellSlot()));
            }
        }
    }
}