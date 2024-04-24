package net.sunbuilder2020.wizardry.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientSpellsData {
    public static List<String> spellIDs;
    public static List<String> activeSpellIDs;
    public static int activeSpellSlot;

    public static void setSpellIDs(List<String> spellIDs) {
        ClientSpellsData.spellIDs = spellIDs;
    }

    public static void setActiveSpellIDs(List<String> activeSpellIDs) {
        ClientSpellsData.activeSpellIDs = activeSpellIDs;
    }

    public static void setActiveSpellSlot(int spellSlot) {
        ClientSpellsData.activeSpellSlot = spellSlot;
    }

    public static List<String> getSpellIDs() {
        return spellIDs;
    }

    public static List<String> getActiveSpellIDs() {
        return activeSpellIDs;
    }

    public static int getActiveSpellSlot() {
        return activeSpellSlot;
    }

    public static String getActiveSpellIDInSlot(int index) {
        String spellAtSlot = activeSpellIDs.get(index);

        return (spellAtSlot.equals("null")) ? null : spellAtSlot;
    }

    public static List<AbstractSpell> getSpells() {
        List<AbstractSpell> spells = new ArrayList<>();

        for (String spellID : spellIDs) {
            spells.add(SpellRegistry.getSpell(spellID));
        }

        return spells;
    }

    public static List<AbstractSpell> getActiveSpells() {
        List<AbstractSpell> activeSpells = new ArrayList<>();

        for (String activeSpellID : activeSpellIDs) {
            activeSpells.add(SpellRegistry.getSpell(activeSpellID));
        }

        return activeSpells;
    }

    public static AbstractSpell getActiveSpellInSlot(int index) {
        return SpellRegistry.getSpell(getActiveSpellIDInSlot(index));
    }
}