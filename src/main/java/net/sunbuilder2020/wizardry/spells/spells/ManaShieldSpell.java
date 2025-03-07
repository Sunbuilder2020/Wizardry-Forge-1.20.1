package net.sunbuilder2020.wizardry.spells.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellType;

public class ManaShieldSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(Wizardry.MOD_ID, "mana_shield");

    @Override
    public ResourceLocation getSpellResource() {
        return spellID;
    }

    @Override
    public SpellType getType() {
        return SpellType.SELF;
    }

    @Override
    public int castTime() {
        return 15;
    }

    @Override
    public int cooldown() {
        return 20*60;
    }

    @Override
    public void onCast(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20*60, 2));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20*5, 1));

        super.onCast(player);
    }
}
