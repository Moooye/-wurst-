# wurst-Xmenu

一个 Minecraft 26.3 Fabric 客户端小 mod：屏蔽 Wurst 客户端对游戏界面的干扰，恢复原版体验。

## 功能

- **隐藏暂停菜单的「Wurst Options」按钮**（Wurst 7.56 的按钮文本为 `literal(" Options")`，配合贴图显示）
- **恢复被 Wurst 隐藏的原版图标行**（反馈/举报图标，以及插在该行的 ModMenu 按钮）
- **屏蔽游戏内左上角的 Wurst logo**（标志贴图 + 版本号）
- **屏蔽「聊天消息可以被举报」系统通知**（`toast.wurst.nochatreports.unsafe_server`）

## 原理

| Mixin | 目标 | 作用 |
|---|---|---|
| `WurstOptionsOtfMixin` | `WurstOptionsOtf#isVisibleInGameMenu` | 强制返回 false，Wurst 不再加按钮/挪动/隐藏图标行（根治按钮错乱） |
| `PauseScreenMixin` | `PauseScreen#createPauseMenu` / `extractRenderState` | 兜底：移除 Wurst 按钮、还原布局、恢复被隐藏的 20x20 图标 |
| `ScreenAccessor` | `Screen` | 暴露 children/renderables/narratables 三个控件列表 |
| `WurstLogoMixin` | `WurstLogo#render` | 取消游戏内 HUD logo 渲染 |
| `ToastManagerMixin` | `ToastManager#addToast` | 按 token 拦截 `UNSECURE_SERVER_WARNING` 系统通知 |

## 环境

- Minecraft **26.3**（未混淆版本，官方映射）
- Fabric Loader ≥ 0.19.5
- Java 25（编译用），无需 Fabric API

## 构建

无需 Gradle，直接用 JDK 25 + 官方 client.jar 编译：

```powershell
javac --release 25 -cp "libs\26.3-client.jar;libs\sponge-mixin-0.17.4.jar;libs\brigadier.jar;<Wurst jar>" -d build (gci src -Recurse -Filter *.java).FullName
jar --create --file wurst-Xmenu-26.3.jar -C build .
jar --update --file wurst-Xmenu-26.3.jar -C assets wurst-xmenu-icon.png
```

成品 jar 在仓库根目录：`wurst-Xmenu-26.3.jar`，放进 `.minecraft/mods` 即可。

## 说明

- Wurst 未安装时所有针对 Wurst 的 mixin 自动跳过，不影响游戏
- 拦截 `UNSECURE_SERVER_WARNING` toast 会同时屏蔽原版同款「聊天消息无法验证」通知

## License

MIT
