package dev.andrii.bosstracker.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

public class NeoBossTrackerBotConfig {

    private final Properties properties;

    public NeoBossTrackerBotConfig() {
        this.properties = loadProperties();
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream in = Files.newInputStream(Path.of("config/config.properties"))) {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config/config.properties", e);
        }

        return properties;
    }

    public String getRequired(String key) {
        String value = get(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                "Missing required property: " + key
            );
        }

        return value;
    }

    public String get(String key) {
        String value = getEnvironmentValue(key);

        if (value != null && !value.isBlank()) {
            return value;
        }

        return properties.getProperty(key);
    }

    private String getEnvironmentValue(String key) {
        String envKey = key
            .replace('.', '_')
            .toUpperCase(Locale.ROOT);

        return System.getenv(envKey);
    }
}