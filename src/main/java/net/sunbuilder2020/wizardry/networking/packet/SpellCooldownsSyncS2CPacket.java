package net.sunbuilder2020.wizardry.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.sunbuilder2020.wizardry.client.screen.ClientSpellCooldownsData;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SpellCooldownsSyncS2CPacket {
    private final HashMap<AbstractSpell, Integer> spellCooldowns;

    public SpellCooldownsSyncS2CPacket(HashMap<AbstractSpell, Integer> spellCooldowns) {
        this.spellCooldowns = new HashMap<>(spellCooldowns);
    }

    public SpellCooldownsSyncS2CPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        this.spellCooldowns = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String spellId = buf.readUtf();
            int cooldown = buf.readInt();
            AbstractSpell spell = SpellRegistry.getSpell(spellId); // Assuming you have a SpellRegistry to get spells by ID
            if (spell != null) {
                spellCooldowns.put(spell, cooldown);
            }
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(spellCooldowns.size());
        for (Map.Entry<AbstractSpell, Integer> entry : spellCooldowns.entrySet()) {
            buf.writeUtf(entry.getKey().getSpellId());
            buf.writeInt(entry.getValue());
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellCooldownsData.setSpellCooldowns(spellCooldowns); // Assuming ClientSpellCooldownsData is a class that handles client-side spell cooldowns
        });
        return true;
    }
}