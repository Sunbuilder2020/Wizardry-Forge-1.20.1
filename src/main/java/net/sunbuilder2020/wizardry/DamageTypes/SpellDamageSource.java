package net.sunbuilder2020.wizardry.DamageTypes;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellDamageSource extends DamageSource {
    private final AbstractSpell causingSpell;

    public SpellDamageSource(Holder<DamageType> damageType, AbstractSpell causingSpell, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 damageSourcePosition) {
        super(damageType, directEntity, causingEntity, damageSourcePosition);
        this.causingSpell = causingSpell;
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity entity) {
        Entity causingEntity = this.getEntity();
        String translationKey = "death.attack." + this.getMsgId();

        if (causingEntity != null) {
            return Component.translatable(translationKey, entity.getDisplayName(), causingEntity.getDisplayName(), this.causingSpell.getDisplayName());
        } else {
            return Component.translatable(translationKey, entity.getDisplayName(), "unknown", this.causingSpell.getDisplayName());
        }
    }

    public AbstractSpell getCausingSpell() {
        return this.causingSpell;
    }
}
