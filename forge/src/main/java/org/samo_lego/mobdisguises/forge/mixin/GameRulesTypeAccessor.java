package org.samo_lego.mobdisguises.mixin;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.IntRule.class)
public interface GameRulesTypeAccessor {
    @Invoker("create")
    static GameRules.Type<GameRules.IntRule> invokeCreate(int i) {
        throw new AssertionError();
    }
}
