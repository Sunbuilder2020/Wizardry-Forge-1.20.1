package net.sunbuilder2020.wizardry.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.SetSpellsDataC2SPacket;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientSpellsData {
    public static List<AbstractSpell> spells = new ArrayList<>();
    public static List<AbstractSpell> activeSpells = new ArrayList<>();
    public static int activeSpellSlot;

    public static void setSpells(List<AbstractSpell> spells) {
        ClientSpellsData.spells = new ArrayList<>(spells);
    }

    public static void setActiveSpells(List<AbstractSpell> activeSpellIDs) {
        ClientSpellsData.activeSpells = new ArrayList<>(activeSpellIDs);
    }

    public static void setActiveSpellSlot(int spellSlot) {
        ClientSpellsData.activeSpellSlot = spellSlot;
    }

    public static List<AbstractSpell> getSpells() {
        return spells;
    }

    public static List<AbstractSpell> getActiveSpells() {
        return activeSpells;
    }

    public static int getActiveSpellSlot() {
        return activeSpellSlot;
    }

    public static AbstractSpell getActiveSpellInSlot(int index) {
        if (index >= 0 && index < activeSpells.size()) {
            return activeSpells.get(index);
        }
        return null;
    }

    public static void setActiveSpell(AbstractSpell spell, int index) {
        if (index >= activeSpells.size()) {
            while (activeSpells.size() <= index) {
                activeSpells.add(null);
            }
        }
        ClientSpellsData.activeSpells.set(index, spell);
    }

    public static void addActiveSpell(AbstractSpell add) {
        if (!activeSpells.contains(add)) {
            ClientSpellsData.activeSpells.add(add);
        }
    }

    public static void removeActiveSpell(AbstractSpell remove) {
        ClientSpellsData.activeSpells.remove(remove);
    }

    public static void addSpell(AbstractSpell add) {
        if (!spells.contains(add)) {
            spells.add(add);
        }
    }

    public static void removeSpell(AbstractSpell remove) {
        spells.remove(remove);
    }

    public static boolean hasSpell(AbstractSpell spell) {
        return spells.contains(spell);
    }

    public static void syncData() {
        ModMessages.sendToServer(new SetSpellsDataC2SPacket(spells, activeSpells, activeSpellSlot));
    }

    public void trimActiveSpells() {
        if (activeSpells.size() > 8) {
            activeSpells = activeSpells.subList(0, 8);
        }
    }

    public static void removeInvalidSpells() {
        spells.removeIf(spell -> spell instanceof NoneSpell);
        activeSpells.removeIf(spell -> spell instanceof NoneSpell);
    }
}