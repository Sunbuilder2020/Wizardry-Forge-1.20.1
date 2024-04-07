package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoRegisterCapability
public class PlayerSpells {
    private List<String> spells = new ArrayList<>();

    public List<String> getSpells() {
        return spells;
    }

    public void setSpells(List<String> spells) {
        this.spells = new ArrayList<>(spells);
    }

    public void addSpell(String add) {
        if (!spells.contains(add)) this.spells.add(add);
    }

    public void removeSpell(String remove) {
        this.spells.remove(remove);
    }

    public boolean hasSpell(String spell) {
        return spells.contains(spell);
    }

    public void copyFrom(PlayerSpells source) {
        this.spells = new ArrayList<>(source.getSpells());
    }

    public void saveNBTData(CompoundTag nbt) {
        String spellsAsString = String.join(";", this.spells);
        nbt.putString("spells", spellsAsString);
    }

    public void loadNBTData(CompoundTag nbt) {
        String spellsAsString = nbt.getString("spells");
        this.spells = new ArrayList<>(Arrays.asList(spellsAsString.split(";")));
    }
}