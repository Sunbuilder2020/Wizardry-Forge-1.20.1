package net.sunbuilder2020.wizardry.spells.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.entity.ModEntities;
import net.sunbuilder2020.wizardry.entity.spellProjectiles.hurtProjectile.HurtProjectile;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellType;

public class HarmSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(Wizardry.MOD_ID, "harm");

    @Override
    public ResourceLocation getSpellResource() {
        return spellID;
    }

    @Override
    public SpellType getType() {
        return SpellType.PROJECTILE;
    }

    @Override
    public int castTime() {
        return 10;
    }

    @Override
    public int cooldown() {
        return 5;
    }

    @Override
    public void onCast(ServerPlayer player) {
        HurtProjectile projectile = new HurtProjectile(ModEntities.HURT_PROJECTILE.get(), player.level(), player);
        projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        Vec3 shootDirection = player.getLookAngle();
        projectile.shoot(shootDirection);

        player.level().addFreshEntity(projectile);

        super.onCast(player);
    }
}
