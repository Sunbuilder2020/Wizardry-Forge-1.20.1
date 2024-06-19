package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.SpellCooldownsSyncS2CPacket;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

import java.util.*;
import java.util.stream.Collectors;

@AutoRegisterCapability
public class PlayerSpellCooldowns {
    private HashMap<AbstractSpell, Integer> spellCooldowns = new HashMap<>();

    public void addSpellCooldown(AbstractSpell spell, int cooldown) {
        spellCooldowns.put(spell, cooldown);
    }

    public HashMap<AbstractSpell, Integer> getSpellCooldowns() {
        return spellCooldowns;
    }

    public boolean isOnCooldown(AbstractSpell spell) {
        return spellCooldowns.containsKey(spell) && spellCooldowns.get(spell) > 0;
    }

    public void tick() {
        spellCooldowns.forEach((spell, cooldown) -> {
            spellCooldowns.replace(spell, cooldown - 1);

            if (cooldown <= 0) spellCooldowns.remove(spell, cooldown);
        });
    }

    public void copyFrom(PlayerSpellCooldowns source) {
        this.spellCooldowns = new HashMap<>(source.getSpellCooldowns());
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag spellList = new ListTag();

        for (Map.Entry<AbstractSpell, Integer> entry : spellCooldowns.entrySet()) {
            CompoundTag spellData = new CompoundTag();
            spellData.putString("Spell", entry.getKey().getSpellId());
            spellData.putInt("Cooldown", entry.getValue());
            spellList.add(spellData);
        }

        nbt.put("SpellCooldowns", spellList);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        ListTag spellList = nbt.getList("SpellCooldowns", 10);
        spellCooldowns.clear();

        for (int i = 0; i < spellList.size(); i++) {
            CompoundTag spellData = spellList.getCompound(i);
            String spellName = spellData.getString("Spell");
            int cooldown = spellData.getInt("Cooldown");

            AbstractSpell spell = SpellRegistry.getSpell(spellName);
            if (spell != null) {
                spellCooldowns.put(spell, cooldown);
            }
        }
    }

    public void syncData(ServerPlayer player) {
        ModMessages.sendToClient(new SpellCooldownsSyncS2CPacket(this.spellCooldowns), player);
    }
}