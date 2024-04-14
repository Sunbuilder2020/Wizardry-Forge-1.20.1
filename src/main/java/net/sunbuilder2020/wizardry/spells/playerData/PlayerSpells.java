package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoRegisterCapability
public class PlayerSpells {
    private List<String> spells = new ArrayList<>();
    private List<String> activeSpells = new ArrayList<>();
    private int activeSpellSlot = 0;
    private int activeSpellsAmount = 10;

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

    public List<String> getActiveSpells() {
        return activeSpells;
    }

    public String getActiveSpell(int index) {
        return activeSpells.get(index);
    }

    public void setActiveSpells(List<String> activeSpells) {
        this.activeSpells = new ArrayList<>(activeSpells);
    }

    public void setActiveSpell(String spell, int index) {
        this.activeSpells.set(index, spell);
    }

    public void addActiveSpell(String add) {
        if (!activeSpells.contains(add)) this.activeSpells.add(add);
    }

    public void removeActiveSpell(String remove) {
        this.activeSpells.remove(remove);
    }

    public int getActiveSpellSlot() {
        return this.activeSpellSlot;
    }

    public void setActiveSpellSlot(int slot) {
        this.activeSpellSlot = slot;
    }

    public int getActiveSpellsAmount() {
        return this.activeSpellsAmount;
    }

    public void setActiveSpellsAmount(int amount) {
        this.activeSpellsAmount = amount;
    }

    public void copyFrom(PlayerSpells source) {
        this.spells = new ArrayList<>(source.getSpells());
        this.activeSpells = new ArrayList<>(source.getSpells());
    }

    public void saveNBTData(CompoundTag nbt) {
        String spellsAsString = String.join(";", this.spells);
        String activeSpellsAsString = String.join(";", this.activeSpells);

        nbt.putString(Wizardry.MOD_ID + "spells", spellsAsString);
        nbt.putString(Wizardry.MOD_ID + "active_spells", activeSpellsAsString);
    }

    public void loadNBTData(CompoundTag nbt) {
        String spellsAsString = nbt.getString(Wizardry.MOD_ID + "spells");
        String activeSpellsAsString = nbt.getString(Wizardry.MOD_ID + "active_spells");

        this.activeSpells = new ArrayList<>(Arrays.asList(activeSpellsAsString.split(";")));
        this.spells = new ArrayList<>(Arrays.asList(spellsAsString.split(";")));

        while (this.activeSpells.size() < this.activeSpellsAmount) {
            this.activeSpells.add("null");
        }
        if (this.activeSpells.size() > this.activeSpellsAmount) {
            this.activeSpells = new ArrayList<>(this.activeSpells.subList(0, this.activeSpellsAmount));
        }
    }
}
