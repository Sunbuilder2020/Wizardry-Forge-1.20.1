package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class PlayerSpellsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerSpells> PLAYER_SPELLS = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerSpells playerSpells = null;
    private final LazyOptional<PlayerSpells> optional = LazyOptional.of(this::createPlayerSpells);

    private PlayerSpells createPlayerSpells() {
        if (this.playerSpells == null) {
            this.playerSpells = new PlayerSpells();
        }
        return this.playerSpells;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_SPELLS ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSpells().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSpells().loadNBTData(nbt);
    }
}
