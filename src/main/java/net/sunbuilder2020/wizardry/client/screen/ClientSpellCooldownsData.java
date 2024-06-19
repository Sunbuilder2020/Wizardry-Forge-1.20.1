package net.sunbuilder2020.wizardry.client.screen;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class ClientSpellCooldownsData {
    public static HashMap<AbstractSpell, Integer> spellCooldowns = new HashMap<>();

    public static void setSpellCooldowns(HashMap<AbstractSpell, Integer> hashMap) {
        spellCooldowns = hashMap;
    }

    public static HashMap<AbstractSpell, Integer> getCurrentSpell() {
        return spellCooldowns;
    }

    public static void setCooldown(AbstractSpell key, int cooldown) {
        spellCooldowns.replace(key, cooldown);
    }

    public static int getCooldown(AbstractSpell key) {
        return spellCooldowns.get(key);
    }

    public static void tick() {
        spellCooldowns.forEach((spell, cooldown) -> {
            spellCooldowns.replace(spell, cooldown, cooldown - 1);

            if (cooldown <= 0) spellCooldowns.remove(spell, cooldown);
        });
    }
}