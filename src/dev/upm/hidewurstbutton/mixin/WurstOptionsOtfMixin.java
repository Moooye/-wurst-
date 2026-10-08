package dev.upm.hidewurstbutton.mixin;

import net.wurstclient.other_features.WurstOptionsOtf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 根治暂停菜单问题：Wurst 的按钮添加与图标行隐藏都以
 * WurstOptionsOtf.isVisibleInGameMenu() 为开关，强制返回 false 后
 * Wurst 不会向暂停菜单添加按钮、也不会隐藏原版图标行（ModMenu 按钮恢复）。
 * WurstOptionsOtf 是普通类（非 mixin 类），可以安全注入。
 */
@Mixin(value = WurstOptionsOtf.class, remap = false)
public abstract class WurstOptionsOtfMixin {

    @Inject(method = "isVisibleInGameMenu()Z", at = @At("HEAD"), cancellable = true, remap = false)
    private void hidewurst$neverInGameMenu(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
