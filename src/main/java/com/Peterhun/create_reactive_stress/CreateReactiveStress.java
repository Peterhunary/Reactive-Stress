package com.Peterhun.create_reactive_stress;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.Peterhun.create_reactive_stress.UtilityHelperClass.createReactiveStress$CachedCall;
import static com.Peterhun.create_reactive_stress.UtilityHelperClass.createReactiveStress$LoadConfig;


@Mod("create_reactive_stress")
public final class CreateReactiveStress {

    public static final String MODID = "create_reactive_stress";
    public static final CreateRegistrate REGISTRATE=CreateRegistrate.create(MODID);

    public CreateReactiveStress(IEventBus ModEventBus, ModContainer container) {
        ModEventBus.addListener(this::commonSetup);
        REGISTRATE.registerEventListeners(ModEventBus);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        createReactiveStress$LoadConfig();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        createReactiveStress$LoadConfig();
    }

    @SubscribeEvent
    public void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            createReactiveStress$CachedCall();
        }
    }
}
