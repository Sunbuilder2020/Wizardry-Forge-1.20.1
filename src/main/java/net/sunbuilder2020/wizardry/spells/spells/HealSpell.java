package net.sunbuilder2020.wizardry.spells.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.entity.ModEntities;
import net.sunbuilder2020.wizardry.entity.spellProjectiles.hurtProjectile.HurtProjectile;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellType;

public class HealSpell extends AbstractSpell {
    private final ResourceLocation spellID = new ResourceLocation(Wizardry.MOD_ID, "heal");

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
        return 4;
    }

    @Override
    public int cooldown() {
        return 20;
    }

    @Override
    public void onCast(ServerPlayer player) {
        player.heal(6.0F);

        super.onCast(player);
    }
}
