package dev.tizu.hexcessible.smartsig;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import at.petrak.hexcasting.api.casting.math.HexAngle;
import at.petrak.hexcasting.api.casting.math.HexDir;
import dev.tizu.hexcessible.Utils;
import dev.tizu.hexcessible.entries.PatternEntries;
import net.neoforged.fml.ModList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class HexicalMacro implements SmartSig.Conditional {

    @Override
    public boolean enabled() {
        return ModList.get().isLoaded("hexical");
    }

    @Override
    public @Nullable List<PatternEntries.Entry> get(String query) {
        return getAllMacros().stream().map(m -> getFor(Utils.angle(m))).toList();
    }

    @Override
    public @Nullable PatternEntries.Entry get(List<HexAngle> sig) {
        var all = getAllMacros();
        for (var macro : all) {
            if (macro.equals(Utils.angle(sig)))
                return getFor(sig);
        }
        return null;
    }

    private List<String> getAllMacros() {
        var player = Minecraft.getInstance().player;
        if (player == null)
            return List.of();

        var inventory = player.getInventory();

        var targetItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("hexical", "grimoire"));
        return Stream.of(inventory.items, inventory.offhand, inventory.armor,
                player.getEnderChestInventory().getItems())
                .flatMap(Collection::stream)
                .filter(stack -> stack.is(targetItem))
                .map(stack -> stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag())
                .filter(root -> root.contains("expansions"))
                .map(root -> root.getCompound("expansions"))
                .flatMap(expansions -> expansions.getAllKeys().stream())
                .distinct()
                .sorted()
                .toList();
    }

    private PatternEntries.Entry getFor(List<HexAngle> sig) {
        var i18nkey = Component.translatable("hexcessible.smartsig.grimoire").getString();
        return new PatternEntries.Entry("hexical:grimoire_macro/" + Utils.angle(sig),
                i18nkey, () -> false, HexDir.EAST, List.of(sig), List.of(), 0);
    }
}
