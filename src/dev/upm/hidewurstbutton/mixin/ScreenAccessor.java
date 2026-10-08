package dev.upm.hidewurstbutton.mixin;

import java.util.List;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Accessor("children")
    List<GuiEventListener> hidewurst$getChildren();

    @Accessor("renderables")
    List<Renderable> hidewurst$getRenderables();

    @Accessor("narratables")
    List<NarratableEntry> hidewurst$getNarratables();
}
