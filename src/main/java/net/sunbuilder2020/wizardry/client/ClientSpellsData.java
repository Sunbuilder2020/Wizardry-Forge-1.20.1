package net.sunbuilder2020.wizardry.client;

import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.List;

public class ClientSpellsData {
    public static List<String> playerSpells;

    public static void set(List<String> spells) {
        ClientSpellsData.playerSpells = spells;
    }

    public static List<String> getPlayerSpells() {
        return playerSpells;
    }

    public static AbstractSpell getActiveSpell() {
        AbstractSpell activeSpell = null;

        for (String spell : playerSpells) {
            if (SpellRegistry.isValidSpell(spell)) {
                activeSpell = SpellRegistry.getSpell(spell);

                break;
            }
        }

        return activeSpell;
    }
}