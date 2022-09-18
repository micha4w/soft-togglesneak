package net.micha4w.Soft_ToggleSneak.mixin;

import net.micha4w.Soft_ToggleSneak.iface.IKeyMapping;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements IKeyMapping {
    @Shadow
    boolean isDown;

    @Shadow
    public abstract boolean isDown ();

    @Override
    public boolean isDown ( boolean actualValue ) {
        if ( actualValue ) {
            return isDown;
        } else {
            return this.isDown();
        }
    }
}
