package dev.tizu.hexcessible.neo;

import dev.tizu.hexcessible.Hexcessible;
import dev.tizu.hexcessible.HexcessibleConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Hexcessible.MOD_ID, dist = Dist.CLIENT)
public final class HexcessibleNeoForge {
    public HexcessibleNeoForge(ModContainer container) {
        Hexcessible.init();
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (IConfigScreenFactory) (ignored, parent) ->
                        AutoConfig.getConfigScreen(HexcessibleConfig.class, parent).get()
        );
    }
}
