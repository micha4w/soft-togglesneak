package net.micha4w.Soft_ToggleSneak.mixin;

import net.micha4w.Soft_ToggleSneak.IKeybinding;
import net.micha4w.Soft_ToggleSneak.ToggleSneakClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements IKeybinding {

    @Shadow @Final
    private String translationKey;
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
        if ( ToggleSneakClient.isActivated() && translationKey.equals("key.sneak") ) {
            info.setReturnValue(ToggleSneakClient.isSneaking);
        }
    }

    @Inject(at = @At("RETURN"), method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V")
    public void onInit(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo info) {
        if ( translationKey.equals("key.sneak") ) {
            ToggleSneakClient.initKeyBind((KeyBinding) (Object) this);
        }
    }
}
