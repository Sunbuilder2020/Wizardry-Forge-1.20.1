package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import org.jetbrains.annotations.Nullable;

public class PlayerCastingProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<PlayerCasting> PLAYER_CASTING = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerCasting playerCasting = null;
    private final LazyOptional<PlayerCasting> optional = LazyOptional.of(this::createPlayerCasting);

    private PlayerCasting createPlayerCasting() {
        if (this.playerCasting == null) {
            this.playerCasting = new PlayerCasting();
        }
        return this.playerCasting;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_CASTING ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (playerCasting != null) {
            tag.put("spell_casting", playerCasting.serializeNBT());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (playerCasting != null) {
            playerCasting.deserializeNBT(nbt.getCompound("spell_casting"));
        }
    }
}