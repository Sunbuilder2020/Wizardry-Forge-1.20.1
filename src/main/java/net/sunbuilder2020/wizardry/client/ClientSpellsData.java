package net.sunbuilder2020.wizardry.client;

import java.util.List;

public class ClientSpellsData {
    public static List<String> playerSpells;

    public static void set(List<String> spells) {
        ClientSpellsData.playerSpells = spells;
    }

    public static List<String> getPlayerSpells() {
        return playerSpells;
    }
}
