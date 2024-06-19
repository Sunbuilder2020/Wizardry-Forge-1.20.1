package net.sunbuilder2020.wizardry.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.entity.spellProjectiles.hurtProjectile.HurtProjectile;

public class ModEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Wizardry.MOD_ID);

    public static final RegistryObject<EntityType<HurtProjectile>> HURT_PROJECTILE = ENTITIES.register("hurt_projectile",
            () -> EntityType.Builder.<HurtProjectile>of(HurtProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(128)
                    .build(new ResourceLocation(Wizardry.MOD_ID, "hurt_projectile").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}