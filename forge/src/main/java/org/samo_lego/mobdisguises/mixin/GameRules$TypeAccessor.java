package org.samo_lego.mobdisguises.mixin;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.world.GameRules$Type;")
public interface GameRules$TypeAccessor {
    @Invoker("Lnet/minecraft/world/GameRules$IntRule;create(I)Lnet/minecraft/world/GameRules$Type;")
    static GameRules.Type<GameRules.IntRule> invokeCreate(int i) {
        throw new AssertionError();
    }
}
