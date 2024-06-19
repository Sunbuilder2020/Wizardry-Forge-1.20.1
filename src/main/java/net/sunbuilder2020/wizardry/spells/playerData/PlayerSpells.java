package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

import java.util.*;
import java.util.stream.Collectors;

@AutoRegisterCapability
public class PlayerSpells {
    private List<AbstractSpell> spells = new ArrayList<>();
    private List<AbstractSpell> activeSpells = new ArrayList<>();
    private int activeSpellSlot = 0;

    public List<AbstractSpell> getSpells() {
        return spells;
    }

    public void setSpells(List<AbstractSpell> spells) {
        this.spells = new ArrayList<>(spells);
    }

    public void addSpell(AbstractSpell add) {
        if (!spells.contains(add)) {
            this.spells.add(add);
        }
    }

    public void removeSpell(AbstractSpell remove) {
        this.spells.remove(remove);
    }

    public boolean hasSpell(AbstractSpell spell) {
        return spells.contains(spell);
    }

    public List<AbstractSpell> getActiveSpells() {
        return activeSpells;
    }

    public AbstractSpell getActiveSpell(int index) {
        return index >= 0 && index < activeSpells.size() ? activeSpells.get(index) : null;
    }

    public void setActiveSpells(List<AbstractSpell> activeSpells) {
        this.activeSpells = new ArrayList<>(activeSpells);
    }

    public void setActiveSpell(AbstractSpell spell, int index) {
        if (index >= activeSpells.size()) {
            while (activeSpells.size() <= index) {
                activeSpells.add(null);
            }
        }
        this.activeSpells.set(index, spell);
    }

    public void removeActiveSpell(AbstractSpell remove) {
        this.activeSpells.remove(remove);
    }

    public int getActiveSpellSlot() {
        return this.activeSpellSlot;
    }

    public void setActiveSpellSlot(int slot) {
        this.activeSpellSlot = slot;
    }

    public void copyFrom(PlayerSpells source) {
        this.spells = new ArrayList<>(source.getSpells());
        this.activeSpells = new ArrayList<>(source.getActiveSpells());
        this.activeSpellSlot = source.getActiveSpellSlot();
    }

    public void saveNBTData(CompoundTag nbt) {
        List<String> spellIDs = spells.stream()
                .map(AbstractSpell::getSpellId)
                .collect(Collectors.toList());
        List<String> activeSpellIDs = activeSpells.stream()
                .map(AbstractSpell::getSpellId)
                .collect(Collectors.toList());

        String spellsAsString = String.join(";", spellIDs);
        String activeSpellsAsString = String.join(";", activeSpellIDs);

        nbt.putString(Wizardry.MOD_ID + "spells", spellsAsString);
        nbt.putString(Wizardry.MOD_ID + "active_spells", activeSpellsAsString);
        nbt.putInt(Wizardry.MOD_ID + "active_spell_slot", this.activeSpellSlot);
    }


    public void loadNBTData(CompoundTag nbt) {
        String spellsAsString = nbt.getString(Wizardry.MOD_ID + "spells");
        String activeSpellsAsString = nbt.getString(Wizardry.MOD_ID + "active_spells");

        List<String> spellIDs = new ArrayList<>(Arrays.asList(spellsAsString.split(";")));
        List<String> activeSpellIDs = new ArrayList<>(Arrays.asList(activeSpellsAsString.split(";")));

        this.spells = spellIDs.stream()
                .map(SpellRegistry::getSpell)
                .collect(Collectors.toList());
        this.activeSpells = activeSpellIDs.stream()
                .map(SpellRegistry::getSpell)
                .collect(Collectors.toList());
        this.activeSpellSlot = nbt.getInt(Wizardry.MOD_ID + "active_spell_slot");
    }

    public void removeInvalidSpells() {
        spells.removeIf(spell -> spell instanceof NoneSpell);

        Set<AbstractSpell> uniqueSpells = new HashSet<>();
        spells.removeIf(spell -> !uniqueSpells.add(spell));

        for (int i = 0; i < activeSpells.size(); i++) {
            AbstractSpell activeSpell = activeSpells.get(i);
            if (!spells.contains(activeSpell)) {
                activeSpells.set(i, SpellRegistry.noneSpell());
            }
        }
    }

    public void trimActiveSpells() {
        if (activeSpells.size() > 8) {
            activeSpells = activeSpells.subList(0, 8);
        }
    }

    public void switchToValidSpellSlot() {
        int originalSlot = activeSpellSlot;

        if (getActiveSpell(getActiveSpellSlot()) == null || getActiveSpell(getActiveSpellSlot()) instanceof NoneSpell) {
            if (!activeSpells.stream().filter(spell -> !(spell instanceof NoneSpell)).collect(Collectors.toList()).isEmpty()) {
                do {
                    activeSpellSlot += 1;

                    if (activeSpellSlot < 0) {
                        activeSpellSlot = activeSpells.size() - 1;
                    } else if (activeSpellSlot >= activeSpells.size()) {
                        activeSpellSlot = 0;
                    }

                    if (activeSpellSlot == originalSlot) {
                        break;
                    }
                } while (SpellRegistry.isNoneSpell(activeSpells.get(activeSpellSlot)));
            }
        }
    }

    public void syncData(ServerPlayer player) {
        ModMessages.sendToClient(new SpellsDataSyncS2CPacket(spells, activeSpells, activeSpellSlot), player);
    }
}