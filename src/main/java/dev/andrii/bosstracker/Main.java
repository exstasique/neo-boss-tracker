package dev.andrii.bosstracker;

import dev.andrii.bosstracker.config.NeoBossTrackerBotConfig;
import dev.andrii.bosstracker.config.ApplicationSettings;
import dev.andrii.bosstracker.factory.NeoBossTrackerBotFactory;

public class Main {

    public static void main(String[] args) {
        final ApplicationSettings settings = new ApplicationSettings(new NeoBossTrackerBotConfig());
        final NeoBossTrackerBotApplication application = new NeoBossTrackerBotFactory(settings).create();
        application.start();
    }
}