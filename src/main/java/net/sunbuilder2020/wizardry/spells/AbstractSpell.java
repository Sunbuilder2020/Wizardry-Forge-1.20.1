package net.sunbuilder2020.wizardry.spells;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.CastingDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerCasting;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerCastingProvider;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellCooldownsProvider;

import java.util.List;
import java.util.Objects;

public abstract class AbstractSpell {
    private String spellID = null;
    private String spellName = null;

    public AbstractSpell() {
    }

    public abstract ResourceLocation getSpellResource();

    public abstract SpellType getType();

    public abstract int castTime();

    public abstract int cooldown();

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
        // Ensure the format string uses the correct placeholder
        String translationKey = String.format("text.wizardry.%s.info", getSpellName());
        return List.of(Component.translatable(translationKey));
    }

    public void startCast(ServerPlayer player) {
        player.getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).ifPresent(spellCooldowns -> {
            if (spellCooldowns.isOnCooldown(this)) return;

            player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(playerCasting -> {
                if (this.castTime() > 0) {
                    playerCasting.startCasting(this);

                    ModMessages.sendToClient(new CastingDataSyncS2CPacket(this, this.castTime()), player);
                } else {
                    this.onCast(player);
                }
            });
        });
    }

    public void stopCast(ServerPlayer player) {
        player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(PlayerCasting::stopCasting);

        ModMessages.sendToClient(new CastingDataSyncS2CPacket(null, 0), player);
    }

    public void onCast(ServerPlayer player) {
        this.addCooldown(player);
    }

    public void addCooldown(Player player) {
        player.getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).ifPresent(spellCooldowns -> {
            spellCooldowns.addSpellCooldown(this, this.cooldown());
        });
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
}