package net.sunbuilder2020.wizardry.spells.spells;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellType;

import java.util.List;

public class NoneSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(Wizardry.MOD_ID, "none");

    @Override
    public SpellType getType() {
        return SpellType.NONE;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public int castTime() {
        return 0;
    }

    @Override
    public int cooldown() {
        return 0;
    }
}