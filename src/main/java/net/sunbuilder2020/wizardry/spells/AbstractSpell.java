package net.sunbuilder2020.wizardry.spells;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSpell {
    private String spellID = null;
    private String spellName = null;

    public AbstractSpell() {
    }

    public abstract ResourceLocation getSpellResource();

    public final String getSpellName() {
        if (spellName == null) {
            var resourceLocation = Objects.requireNonNull(getSpellResource());
            spellName = resourceLocation.getPath().intern();
        }

        return spellName;
    }

    public final String getSpellId() {
        if (spellID == null) {
            var resourceLocation = Objects.requireNonNull(getSpellResource());
            spellID = resourceLocation.toString().intern();
        }

        return spellID;
    }

    public MutableComponent getDisplayName() {
        return Component.translatable(getComponentId());
    }

    public String getComponentId() {
        return String.format("spell.%s.%s", getSpellResource().getNamespace(), getSpellName());
    }

    public final ResourceLocation getSpellIconResource() {
        return new ResourceLocation(getSpellResource().getNamespace(), "textures/spell/spell_icons/" + getSpellName() + ".png");
    }

    public List<MutableComponent> getUniqueInfo() {
        return List.of();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof AbstractSpell other) {
            return this.getSpellResource().equals(other.getSpellResource());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.getSpellResource().hashCode();
    }

    public void castSpell(ServerPlayer player) {
        onCast(player);
    }

    public void onCast(ServerPlayer player) {
    }

    public boolean isLearned(Player player) {
        AtomicBoolean isLearned = new AtomicBoolean(false);

        player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> isLearned.set(playerSpells.hasSpell(this)));

        return isLearned.get();
    }

    public boolean needsLearning() {
        return true;
    }
}
