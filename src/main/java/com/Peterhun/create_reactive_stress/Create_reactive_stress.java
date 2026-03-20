package com.Peterhun.create_reactive_stress;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static com.Peterhun.create_reactive_stress.UtilityHelperClass.createReactiveStress$CachedCall;
import static com.Peterhun.create_reactive_stress.UtilityHelperClass.createReactiveStress$LoadConfig;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Create_reactive_stress.MODID)
public class Create_reactive_stress {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "create_reactive_stress";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public Create_reactive_stress() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        createReactiveStress$LoadConfig();
        LOGGER.info("C: Reactive stress has been initialised");
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
