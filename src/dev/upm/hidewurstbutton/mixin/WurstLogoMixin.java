package dev.upm.hidewurstbutton.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.wurstclient.hud.WurstLogo;

/**
 * 屏蔽游戏内左上角的 Wurst logo（含版本号文字和 WURST 贴图）。
 * 仅 cancel render 方法，不影响 HackListHUD 和 TabGui。
 */
@Mixin(WurstLogo.class)
public abstract class WurstLogoMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void hidewurst$cancelLogoRender(GuiGraphicsExtractor g, CallbackInfo ci) {
        ci.cancel();
    }
}
