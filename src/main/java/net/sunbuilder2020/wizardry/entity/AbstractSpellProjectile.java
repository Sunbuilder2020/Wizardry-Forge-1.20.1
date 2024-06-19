package net.sunbuilder2020.wizardry.entity;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Objects;

public abstract class AbstractSpellProjectile extends Projectile {
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(AbstractSpellProjectile.class, EntityDataSerializers.INT);
    private int expireTicks = 20 * 20;
    private float damage;

    public abstract float getSpeed();

    protected AbstractSpellProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLOR, 0xFFFFFF); // Default color: white
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setTrailColor(Color trailColor) {
        this.entityData.set(COLOR, trailColor.getRGB());
    }

    public Color getTrailColor() {
        return new Color(this.entityData.get(COLOR));
    }

    public void shoot(Vec3 rotation) {
        setDeltaMovement(rotation.scale(getSpeed()));
    }

    public void trailParticles() {
        Color color = getTrailColor();
        float red = color.getRed() / 255.0F;
        float green = color.getGreen() / 255.0F;
        float blue = color.getBlue() / 255.0F;

        this.level().addParticle(new DustParticleOptions(new Vector3f(red, green, blue), 1.0F),
                this.getRandomX(0.5D), this.getY(), this.getRandomZ(0.5D), 0, 0, 0);
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return this.getOwner() != null && !pTarget.equals(this.getOwner());
    }

    @Override
    public void tick() {
        super.tick();

        // Expire the projectile after a certain number of ticks
        if (this.tickCount > this.expireTicks) {
            this.discard();
            return;
        }

        // Only spawn particles on the client side
        if (this.level().isClientSide) {
            this.trailParticles();
        }

        Vec3 startVec = this.position();
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 endVec = startVec.add(deltaMovement);

        HitResult hitResult = this.level().clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if (hitResult.getType() != HitResult.Type.MISS) {
            endVec = hitResult.getLocation();
        }

        HitResult entityHitResult = ProjectileUtil.getEntityHitResult(this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);

        if (entityHitResult != null) {
            hitResult = entityHitResult;
        }

        if (hitResult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitResult)) {
            this.onHit(hitResult);
        }

        // Move the projectile
        this.setPos(this.getX() + deltaMovement.x, this.getY() + deltaMovement.y, this.getZ() + deltaMovement.z);
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);

        // Apply gravity if necessary
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) hitResult);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) hitResult);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);

        this.discard();
    }

    @Override
    public boolean shouldBeSaved() {
        return super.shouldBeSaved() && !Objects.equals(getRemovalReason(), RemovalReason.UNLOADED_TO_CHUNK);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("damage", this.getDamage());
        tag.putInt("age", this.tickCount);
        tag.putInt("color", this.getTrailColor().getRGB());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.damage = tag.getFloat("damage");
        this.tickCount = tag.getInt("age");
        this.setTrailColor(new Color(tag.getInt("color")));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }
}
