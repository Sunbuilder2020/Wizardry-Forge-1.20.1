package net.sunbuilder2020.wizardry.events;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;
import net.sunbuilder2020.wizardry.util.KeyBinding;

import java.awt.event.InputEvent;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (KeyBinding.KEY_MAPPING_OPEN_SPELL_GUI.consumeClick()) {
            Minecraft.getInstance().setScreen(new SpellGUI());
        }

        if (KeyBinding.KEY_MAPPING_SWITCH_SPELL_LEFT.consumeClick()) {
            switchSpellSlot(-1, Minecraft.getInstance().player);
        }

        if (KeyBinding.KEY_MAPPING_SWITCH_SPELL_RIGHT.consumeClick()) {
            switchSpellSlot(1, Minecraft.getInstance().player);
        }
    }

    public static void switchSpellSlot(int amount, Player player) {
        if (!ClientSpellsData.getActiveSpells().isEmpty()) {
            int originalSpellSlot = ClientSpellsData.getActiveSpellSlot();
            int spellSlot = originalSpellSlot;
            int attempts = 0;

            do {
                spellSlot += amount;
                if (spellSlot >= ClientSpellsData.getActiveSpells().size()) {
                    spellSlot = 0;
                } else if (spellSlot < 0) {
                    spellSlot = ClientSpellsData.getActiveSpells().size() - 1;
                }

                if (++attempts > ClientSpellsData.getActiveSpells().size()) {
                    return;
                }
            } while (ClientSpellsData.getActiveSpellInSlot(spellSlot) == null);

            ClientSpellsData.setActiveSpellSlot(spellSlot);
            if (spellSlot != originalSpellSlot) {
                ModMessages.sendToServer(new SetSpellsDataC2SPacket(ClientSpellsData.getSpells(), ClientSpellsData.getActiveSpells(), ClientSpellsData.getActiveSpellSlot()));
            }
        }
    }
}