package net.sunbuilder2020.wizardry.entity.spellProjectiles.hurtProjectile;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.sunbuilder2020.wizardry.DamageTypes.ModDamageTypes;
import net.sunbuilder2020.wizardry.DamageTypes.SpellDamageSource;
import net.sunbuilder2020.wizardry.entity.AbstractSpellProjectile;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.awt.*;

public class HurtProjectile extends AbstractSpellProjectile {
    public HurtProjectile(EntityType<HurtProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        this.setDamage(4.0F);
        this.setTrailColor(new Color(0xBEA90A34, true));
    }

    public HurtProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel, Player player) {
        super(pEntityType, pLevel);
        this.setOwner(player);
        this.setNoGravity(true);
        this.setDamage(4.0F);
        this.setTrailColor(new Color(0xBEA90A34, true));
    }

    @Override
    public float getSpeed() {
        return 1.75F;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();

        if (entity instanceof LivingEntity) {
            DamageSource damageSource = new SpellDamageSource(Holder.direct(ModDamageTypes.SPELL), SpellRegistry.HARM_SPELL.get(), this, this.getOwner(), this.getOwner().getEyePosition());
            entity.hurt(damageSource, this.getDamage());
            ((LivingEntity) entity).setLastHurtByMob((LivingEntity) this.getOwner());
        }

        discard();
        super.onHitEntity(pResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);

        discard();
    }
}
