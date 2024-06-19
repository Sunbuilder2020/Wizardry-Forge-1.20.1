package net.sunbuilder2020.wizardry.DamageTypes;

import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public class ModDamageTypes {
    public static final DamageType SPELL = new DamageType(
            "damage_type.wizardry.spell",
            DamageScaling.ALWAYS,
            0.1F,
            DamageEffects.HURT
    );
}
