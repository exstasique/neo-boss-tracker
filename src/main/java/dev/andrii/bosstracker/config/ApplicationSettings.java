package dev.andrii.bosstracker.config;

import java.time.ZoneId;

public class ApplicationSettings {

    private final String token;
    private final ZoneId timezone;
    private final int updateInterval;

    public ApplicationSettings(NeoBossTrackerBotConfig config) {
        this.token = config.getRequired("discord.token");
        this.timezone = ZoneId.of(config.getRequired("timezone"));
        this.updateInterval = Integer.parseInt(
            config.getRequired("update.interval.seconds")
        );
    }

    public String getToken() {
        return token;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }
}