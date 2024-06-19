package net.sunbuilder2020.wizardry.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

@OnlyIn(Dist.CLIENT)
public class ClientCastingData {
    public static AbstractSpell currentSpell = SpellRegistry.noneSpell();
    public static int castTimeRemaining = 0;

    public static void setCurrentSpell(AbstractSpell currentSpell) {
        ClientCastingData.currentSpell = currentSpell;
    }

    public static AbstractSpell getCurrentSpell() {
        return currentSpell;
    }

    public static void setCastTimeRemaining(int castTimeRemaining) {
        ClientCastingData.castTimeRemaining = castTimeRemaining;
    }

    public static int getCastTimeRemaining() {
        return castTimeRemaining;
    }

    public static void tick() {
        if (currentSpell != null && !(currentSpell instanceof NoneSpell)) {
            if (castTimeRemaining > 0) {
                castTimeRemaining--;
            } else {
                stopCasting();
            }
        }
    }

    public static void stopCasting() {
        currentSpell = SpellRegistry.noneSpell();
        castTimeRemaining = 0;
    }

    public static boolean isCasting() {
        return currentSpell != null && !(currentSpell instanceof NoneSpell) && castTimeRemaining > 0;
    }
}