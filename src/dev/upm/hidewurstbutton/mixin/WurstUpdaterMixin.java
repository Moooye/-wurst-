package dev.upm.hidewurstbutton.mixin;

import net.wurstclient.update.WurstUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 屏蔽 Wurst 的更新提示聊天消息（「Wurst 7.56.1 is now available...」）。
 * onUpdate() 是更新检查结果发到聊天框的入口，直接取消即可。
 * require=0：Wurst 未安装或未来方法改名时静默跳过，不影响游戏。
 */
@Mixin(WurstUpdater.class)
public abstract class WurstUpdaterMixin {

    @Inject(method = "onUpdate()V", at = @At("HEAD"), require = 0)
    private void hidewurst$silenceUpdateNotice(CallbackInfo ci) {
        ci.cancel();
    }
}
