package eu.pb4.polymer.core.impl.client.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import eu.pb4.polymer.core.impl.PolymerImpl;
import eu.pb4.polymer.core.impl.PolymerImplUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.Predicate;

@ApiStatus.Internal
public class EmiCompatibility implements EmiPlugin {
    public static final boolean IS_1_0_0 = FabricLoader.getInstance().getModContainer("emi").map(x -> {
        try {
            return x.getMetadata().getVersion().compareTo(Version.parse("1.0.0-")) >= 0;
        } catch (Throwable e) {
            return false;
        }
    }).orElse(false);

    private static final Predicate<EmiStack> SHOULD_REMOVE = (stack) -> PolymerImplUtils.isPolymerControlled(stack.getItemStack());

    static {
        CompatUtils.registerSyncReload(EmiCompatibility::tryReloading);
    }

    private static void tryReloading() {
        try {
            Class.forName(IS_1_0_0 ? "dev.emi.emi.runtime.EmiReloadManager" : "dev.emi.emi.EmiReloadManager").getMethod("reload").invoke(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(EmiRegistry registry) {
        if (PolymerImpl.IS_CLIENT) {
            update(registry);
        }
    }

    private static void update(EmiRegistry registry) {
        if (registry == null) {
            return;
        }
        synchronized (registry) {
            try {
                ((List<EmiStack>) Class.forName(IS_1_0_0 ? "dev.emi.emi.registry.EmiStackList" : "dev.emi.emi.EmiStackList").getField("stacks").get(null)).removeIf(SHOULD_REMOVE);
                CompatUtils.iterateItems(stack -> registry.addEmiStack(EmiStack.of(stack)));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
