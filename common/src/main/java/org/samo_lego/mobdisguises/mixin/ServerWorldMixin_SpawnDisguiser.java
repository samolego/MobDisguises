package org.samo_lego.mobdisguises.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nucleoid.disguiselib.casts.EntityDisguise;

import java.util.List;
import java.util.stream.Collectors;

import static org.samo_lego.mobdisguises.MobDisguises.DISGUISED_MOB_SPAWN_CHANCE;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin_SpawnDisguiser {

    @Shadow public abstract ServerWorld toServerWorld();

    @Unique
    private final List<EntityType<?>> mobdisguises$DISGUISE_POSSIBILITIES = Registry.ENTITY_TYPE
            .stream()
            .filter(type ->
                    type.isSummonable() &&
                    !type.getSpawnGroup().equals(SpawnGroup.MISC)
            )
            .collect(Collectors.toList());

    @Inject(
            method = "addEntity(Lnet/minecraft/entity/Entity;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;addEntity(Lnet/minecraft/entity/Entity;)V"
            )
    )
    private void onEntitySpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof MobEntity && !((EntityDisguise) entity).isDisguised()) {
            int randomChance = ((MobEntity) entity).getRandom().nextInt(100);
            if(this.toServerWorld().getGameRules().get(DISGUISED_MOB_SPAWN_CHANCE).get() > randomChance) {
                EntityType<?> disguise = this.mobdisguises$DISGUISE_POSSIBILITIES.get(((MobEntity) entity).getRandom().nextInt(mobdisguises$DISGUISE_POSSIBILITIES.size()));
                ((EntityDisguise) entity).disguiseAs(disguise);
            }
        }
    }
}
