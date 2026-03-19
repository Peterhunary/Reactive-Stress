package com.Peterhun.create_reactive_stress;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

public final class Config {

    public static final ModConfigSpec SPEC;
    public static final Map<String, ModConfigSpec.ConfigValue<Double>> MULTIPLIERS = new HashMap<>();


    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("reactive_stress");

        addMultiplier(builder, "Press", 2.5);
        addMultiplier(builder, "Mixer", 2.0);
        addMultiplier(builder, "MillStone", 2.0);
        addMultiplier(builder, "Saw", 2.0);
        addMultiplier(builder, "CrushingWheel", 3);

        builder.pop();

        SPEC = builder.build();
    }

    private static void addMultiplier(ModConfigSpec.Builder builder, String key, double defaultValue) {
        ModConfigSpec.ConfigValue<Double> value =
                builder.comment("Multiplier for " + key + " stress calculations")
                        .defineInRange(key, defaultValue, 1.0, 100.0);

        MULTIPLIERS.put(key, value);
    }
}