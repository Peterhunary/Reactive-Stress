package com.Peterhun.create_reactive_stress;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.HashMap;
import java.util.Map;

public final class Config {

    public static final ForgeConfigSpec SPEC;
    public static final Map<String, ForgeConfigSpec.DoubleValue> MULTIPLIERS = new HashMap<>();

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("reactive_stress");

        addMultiplier(builder, "Press", 2.5);
        addMultiplier(builder, "Mixer", 2.0);
        addMultiplier(builder, "MillStone", 2.0);
        addMultiplier(builder, "Saw", 2.0);
        addMultiplier(builder, "CrushingWheel", 3.0);

        builder.pop();

        SPEC = builder.build();
    }

    private static void addMultiplier(ForgeConfigSpec.Builder builder, String key, double defaultValue) {
        ForgeConfigSpec.DoubleValue value = builder
                .comment("Multiplier for " + key + " stress calculations")
                .defineInRange(key, defaultValue, 1.0, 100.0);

        MULTIPLIERS.put(key, value);
    }
}
