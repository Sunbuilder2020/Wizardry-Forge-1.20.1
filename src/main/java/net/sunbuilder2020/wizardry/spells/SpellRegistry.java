package net.sunbuilder2020.wizardry.spells;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;
import net.sunbuilder2020.wizardry.spells.spells.AcupunctureSpell;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class SpellRegistry {
    public static final ResourceKey<Registry<AbstractSpell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Wizardry.MOD_ID, "spells"));
    private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, Wizardry.MOD_ID);
    public static final Supplier<IForgeRegistry<AbstractSpell>> REGISTRY = SPELLS.makeRegistry(() -> new RegistryBuilder<AbstractSpell>().disableSaving().disableOverrides());

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static AbstractSpell getSpell(String spellId) {
        return getSpell(new ResourceLocation(spellId));
    }

    public static List<AbstractSpell> getEnabledSpells() {
        return SpellRegistry.REGISTRY.get()
                .getValues()
                .stream()
                .toList();
    }

    public static AbstractSpell getSpell(ResourceLocation resourceLocation) {
        var spell = REGISTRY.get().getValue(resourceLocation);
        if (spell == null) {
            //return noneSpell;
        }
        return spell;
    }

    public static AbstractSpell getActiveSpell(Player player) {
        AtomicReference<AbstractSpell> activeSpell = new AtomicReference<>(null);

        player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> {
            for (String spell : playerSpells.getSpells()) {
                if (SpellRegistry.isValidSpell(spell)) {
                    activeSpell.set(SpellRegistry.getSpell(spell));

                    break;
                }
            }
        });

        return activeSpell.get();
    }

    public static boolean isValidSpell(String spellId) {
        ResourceLocation spellResourceLocation = new ResourceLocation(spellId);
        return REGISTRY.get().containsKey(spellResourceLocation);
    }

    public static final RegistryObject<AbstractSpell> ACUPUNCTURE_SPELL = registerSpell(new AcupunctureSpell());
}