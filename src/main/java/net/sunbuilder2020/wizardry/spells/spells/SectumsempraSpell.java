package net.sunbuilder2020.wizardry.spells.spells;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sunbuilder2020.wizardry.DamageTypes.ModDamageTypes;
import net.sunbuilder2020.wizardry.DamageTypes.SpellDamageSource;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.SpellType;
import net.sunbuilder2020.wizardry.util.RayTraceUtils;

public class SectumsempraSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(Wizardry.MOD_ID, "sectumsempra");

    @Override
    public ResourceLocation getSpellResource() {
        return spellID;
    }

    @Override
    public SpellType getType() {
        return SpellType.TARGET;
    }

    @Override
    public int castTime() {
        return 20;
    }

    @Override
    public int cooldown() {
        return 20;
    }

    @Override
    public void onCast(ServerPlayer player) {
        float range = 15.0F;

        Entity targetEntity = RayTraceUtils.getTargetEntity(player, range);

        if (!(targetEntity instanceof LivingEntity)) return;

        DamageSource damageSource = new SpellDamageSource(Holder.direct(ModDamageTypes.SPELL), SpellRegistry.SECTUMSEMPRA_SPELL.get(), player, player, player.getEyePosition());
        targetEntity.hurt(damageSource, 7.0F);
        ((LivingEntity) targetEntity).setLastHurtByMob(player);

        super.onCast(player);
    }
}
