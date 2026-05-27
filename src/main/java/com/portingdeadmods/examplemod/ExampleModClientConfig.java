package com.portingdeadmods.examplemod;

import com.portingdeadmods.portingdeadlibs.api.config.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ExampleModClientConfig {
    @ConfigValue(name = "A magic number")
    public static final int magicNumber = 42;

}
