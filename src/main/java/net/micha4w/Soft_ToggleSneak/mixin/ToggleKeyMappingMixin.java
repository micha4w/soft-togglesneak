package net.micha4w.Soft_ToggleSneak.mixin;

import ch.micha4w.Soft_ToggleSneak.ToggleSneakClient;
import net.minecraft.client.ToggleKeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToggleKeyMapping.class)
public abstract class ToggleKeyMappingMixin {

    @Inject(at = @At("HEAD"), method = "isDown()Z", cancellable = true)
    public void onIsDown(CallbackInfoReturnable<Boolean> info) {
        if ( (Object) this == ToggleSneakClient.sneakKeyMapping && ToggleSneakClient.isActivated() ) {
            info.setReturnValue(ToggleSneakClient.isSneaking);
        }
    }
}
