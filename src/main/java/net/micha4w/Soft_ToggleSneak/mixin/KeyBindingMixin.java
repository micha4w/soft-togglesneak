package net.micha4w.Soft_ToggleSneak.mixin;

import net.micha4w.Soft_ToggleSneak.iface.IKeybinding;
import net.micha4w.Soft_ToggleSneak.ToggleSneakClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements IKeybinding {

    @Shadow
    private boolean pressed;

    @Shadow
    public abstract boolean isPressed ();

    public boolean isPressed (boolean actualValue) {
        if (actualValue) {
            return pressed;
        } else {
            return this.isPressed();
        }
    }

    @Inject(at = @At("HEAD"), method = "isPressed()Z", cancellable = true)
    public void onIsPressed(CallbackInfoReturnable<Boolean> info) {
        if ( ToggleSneakClient.isActivated() && (Object) this == MinecraftClient.getInstance().options.sneakKey ) {
            info.setReturnValue(ToggleSneakClient.isSneaking);
        }
    }
}
