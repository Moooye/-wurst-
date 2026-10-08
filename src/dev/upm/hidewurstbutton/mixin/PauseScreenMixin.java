package dev.upm.hidewurstbutton.mixin;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 还原 Wurst 对暂停菜单的改动：
 * 1. 移除「Wurst Options」按钮（文本匹配，只命中这一个控件）；
 * 2. 把被 Wurst 下移的按钮上移一行，恢复原版布局；
 * 3. 取消 Wurst 对原版图标行的隐藏（visible=false），恢复 ModMenu 按钮。
 * 原版控件除被隐藏的 20x20 小图标外一律不触碰。
 */
@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin {

    /**
     * 暂停菜单创建完毕后清理一次（Wurst 在 createPauseMenu 的 TAIL 添加按钮，
     * 若本 mixin 先于 Wurst 执行，这里可能扑空，由下面的每帧兜底保证效果）。
     */
    @Inject(method = "createPauseMenu()V", at = @At("TAIL"))
    private void hidewurst$removeAfterCreate(CallbackInfo ci) {
        hidewurst$restore();
    }

    /**
     * 每帧渲染提取前兜底：无论 Wurst 何时把按钮加回来，渲染前都会被移除。
     */
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void hidewurst$removeBeforeExtract(CallbackInfo ci) {
        hidewurst$restore();
    }

    private void hidewurst$restore() {
        ScreenAccessor acc = (ScreenAccessor) this;
        List<?> renderables = acc.hidewurst$getRenderables();
        hidewurst$unhideIconRow(renderables);
        AbstractWidget target = null;
        for (Object o : renderables) {
            if (o instanceof AbstractWidget w && isWurstButton(w)) {
                target = w;
                break;
            }
        }
        if (target == null) {
            return; // 每帧兜底调用时通常已无目标，直接返回避免重复排版
        }
        int gapY = target.getY();
        renderables.remove(target);
        acc.hidewurst$getChildren().remove(target);
        acc.hidewurst$getNarratables().remove(target);
        // 挪到屏幕外：Wurst 每帧会用缓存的按钮引用画 WURST 贴图（只判空不看列表）
        target.setX(-9999);
        target.setY(-9999);
        // Wurst 插入按钮时把下方原版按钮整体下移了一行，这里上移还原
        hidewurst$shiftBelowUp(renderables, gapY);
    }

    /**
     * Wurst 的 hideIconButtonRow 把图标行（反馈/举报 + ModMenu 按钮）设为不可见。
     * 这些都是 20x20 的小图标控件；原版其他控件正常状态均为可见，
     * 以「不可见 + 尺寸不超过 20」为条件恢复，不会误伤其他控件。
     */
    private static void hidewurst$unhideIconRow(List<?> renderables) {
        for (Object o : renderables) {
            if (o instanceof AbstractWidget w && !w.visible
                    && w.getWidth() <= 20 && w.getHeight() <= 20) {
                w.visible = true;
            }
        }
    }

    private static void hidewurst$shiftBelowUp(List<?> renderables, int gapY) {
        for (Object o : renderables) {
            if (o instanceof AbstractWidget w) {
                int wy = w.getY();
                if (wy > gapY) {
                    w.setY(wy - 24); // 一行按钮高 20 + 间距 4
                }
            }
        }
    }

    private static boolean isWurstButton(AbstractWidget w) {
        String s = w.getMessage().getString();
        if (s == null) {
            return false;
        }
        String lower = s.toLowerCase(Locale.ROOT);
        // Wurst 7.56：按钮文本是 literal(" Options")，WURST 标志是贴图
        if (lower.contains("wurst")) {
            return true;
        }
        // 原版按钮均为可翻译组件（"选项…"/"Options..."等），不会命中 equals("options")
        return lower.trim().equals("options");
    }
}
