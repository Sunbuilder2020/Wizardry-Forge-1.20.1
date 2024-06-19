package net.sunbuilder2020.wizardry.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class RayTraceUtils {
    public static Entity getTargetEntity(Player player, double range) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookDirection = player.getLookAngle();
        Vec3 endPosition = eyePosition.add(lookDirection.scale(range));

        // Perform block ray trace first to find the endpoint
        ClipContext context = new ClipContext(eyePosition, endPosition, Block.COLLIDER, Fluid.NONE, player);
        HitResult blockHitResult = player.level().clip(context);

        if (blockHitResult.getType() != HitResult.Type.MISS) {
            endPosition = blockHitResult.getLocation();
        }

        // Perform entity ray trace
        AABB boundingBox = player.getBoundingBox().expandTowards(lookDirection.scale(range)).inflate(1.0D, 1.0D, 1.0D);
        Predicate<Entity> filter = entity -> !entity.isSpectator() && entity.isPickable();
        EntityHitResult entityHitResult = getEntityHitResult(player.level(), player, eyePosition, endPosition, boundingBox, filter);

        if (entityHitResult != null) {
            return entityHitResult.getEntity();
        }

        return null;
    }

    @Nullable
    private static EntityHitResult getEntityHitResult(Level level, Entity projectile, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter) {
        double closestDistance = Double.MAX_VALUE;
        Entity closestEntity = null;
        Vec3 hitVec = null;

        for (Entity entity : level.getEntities(projectile, boundingBox, filter)) {
            AABB entityBoundingBox = entity.getBoundingBox().inflate(entity.getPickRadius());
            Optional<Vec3> optionalHitVec = entityBoundingBox.clip(startVec, endVec);

            if (optionalHitVec.isPresent()) {
                double distance = startVec.distanceToSqr(optionalHitVec.get());

                if (distance < closestDistance) {
                    closestEntity = entity;
                    hitVec = optionalHitVec.get();
                    closestDistance = distance;
                }
            }
        }

        return closestEntity == null ? null : new EntityHitResult(closestEntity, hitVec);
    }
}
