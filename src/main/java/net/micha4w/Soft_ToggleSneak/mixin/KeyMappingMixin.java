package net.micha4w.Soft_ToggleSneak.mixin;

import net.micha4w.Soft_ToggleSneak.iface.IKeybinding;
import net.micha4w.Soft_ToggleSneak.ToggleSneakClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements IKeybinding {

    @Shadow
    private boolean isDown;

    @Shadow
    public abstract boolean isDown();

    public boolean isPressed (boolean actualValue) {
        if (actualValue) {
            return isDown;
        } else {
            return this.isDown();
        }
    }

    @Inject(at = @At("HEAD"), method = "isDown()Z", cancellable = true)
    public void onIsPressed(CallbackInfoReturnable<Boolean> info) {
        if ( ToggleSneakClient.isActivated() && (Object) this == Minecraft.getInstance().options.keyShift) {
            info.setReturnValue(ToggleSneakClient.isSneaking);
        }
    }
}
