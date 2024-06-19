package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class PlayerSpellCooldownsProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<PlayerSpellCooldowns> PLAYER_SPELL_COOLDOWNS = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerSpellCooldowns playerSpellCooldowns = null;
    private final LazyOptional<PlayerSpellCooldowns> optional = LazyOptional.of(this::createPlayerSpellCooldowns);

    private PlayerSpellCooldowns createPlayerSpellCooldowns() {
        if (this.playerSpellCooldowns == null) {
            this.playerSpellCooldowns = new PlayerSpellCooldowns();
        }
        return this.playerSpellCooldowns;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_SPELL_COOLDOWNS ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (playerSpellCooldowns != null) {
            tag.put("spell_cooldowns", playerSpellCooldowns.serializeNBT());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (playerSpellCooldowns != null) {
            playerSpellCooldowns.deserializeNBT(nbt.getCompound("spell_cooldowns"));
        }
    }
}