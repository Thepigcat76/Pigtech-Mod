package com.portingdeadmods.examplemod;

import java.util.List;

import com.portingdeadmods.portingdeadlibs.api.config.ConfigValue;

public final class ExampleModConfig {
    @ConfigValue(key = "log_dirt_block", comment = "Whether to log the dirt block on common setup", name = "Log Dirt Block")
    public static boolean logDirtBlock = true;

    @ConfigValue(comment = "What you want the introduction message to be for the magic number", name = "Magic number introduction")
    public static String magicNumberIntroduction = "The magic number is...";
}
