package dev.upm.hidewurstbutton.mixin;

import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 屏蔽 Wurst 的「聊天消息可以被举报」系统通知：
 * Wurst 的 ClientPacketListenerMixin（mixin 类无法直接注入）通过
 * ToastManager.addToast 添加 token 为 UNSECURE_SERVER_WARNING 的 SystemToast。
 * 在 addToast 入口按 token 拦截即可精准挡下该通知。
 */
@Mixin(ToastManager.class)
public abstract class ToastManagerMixin {

    @Inject(method = "addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V",
            at = @At("HEAD"), cancellable = true)
    private void hidewurst$blockUnsafeServerToast(Toast toast, CallbackInfo ci) {
        if (toast instanceof SystemToast st
                && st.getToken() == SystemToast.SystemToastId.UNSECURE_SERVER_WARNING) {
            ci.cancel();
        }
    }
}
