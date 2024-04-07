package net.sunbuilder2020.wizardry.spells.spells;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;

import java.util.List;
import java.util.logging.Level;

public class AcupunctureSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(Wizardry.MOD_ID, "acupuncture");

    @Override
    public List<MutableComponent> getUniqueInfo() {
        return List.of(Component.translatable("ui.wizardry.acupuncture.info"));
    }

    public AcupunctureSpell() {
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
}