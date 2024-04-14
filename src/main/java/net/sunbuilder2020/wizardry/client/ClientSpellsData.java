package net.sunbuilder2020.wizardry.client;

import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.List;

public class ClientSpellsData {
    public static List<String> spells;
    public static List<String> activeSpells;
    public static int activeSpellSlot;

    public static void setSpells(List<String> spells) {
        ClientSpellsData.spells = spells;
    }

    public static void setActiveSpells(List<String> activeSpells) {
        ClientSpellsData.activeSpells = activeSpells;
    }

    public static void setActiveSpellSlot(int spellSlot) {
        ClientSpellsData.activeSpellSlot = spellSlot;
    }

    public static List<String> getSpells() {
        return spells;
    }

    public static List<String> getActiveSpells() {
        return activeSpells;
    }

    public static int getActiveSpellSlot() {
        return activeSpellSlot;
    }

    public static String getActiveSpellInSlot(int index) {
        String spellAtSlot = activeSpells.get(index);

        return (spellAtSlot.equals("null")) ? null : spellAtSlot;
    }
}